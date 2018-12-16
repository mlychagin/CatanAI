package backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BoardState {
    /*
     * 0 = backend.Player 1
     * 1 = backend.Player 2
     * ...
     */
    ArrayList<Player> playerList = new ArrayList<>();
    public VertexNode[] vertexes = new VertexNode[54];
    int[] devCardPool = new int[]{14, 5, 2, 2, 2};
    int playerTurn = 0;
    int robberTile;

    int[] resourceScarcity = new int[]{0, 0, 0, 0, 0, 0};

    BoardState() {
    }

    public void initBoard() {
        initVertexes();
        setVertexResources();
        setVertexDependencies();
        setResourceScarcity();
    }

    public void initVertexes() {
        for (int i = 0; i < vertexes.length; i++) {
            VertexNode node = GameEngine.getVertex();
            vertexes[i] = node;
        }
    }

    public void setVertexDependencies() {
        for (int[] numberSet : GameEngine.vertexDependencies) {
            VertexNode currentVertex = vertexes[numberSet[0]];
            for (int i = 1; i < numberSet.length; i++) {
                MutablePair pair = GameEngine.getPair();
                pair.set(-1, numberSet[i]);
                currentVertex.listEdges.add(pair);
            }
        }
    }

    public void setVertexResources() {
        int[] tilesResource = GameEngine.tilesResource;
        int[] tilesNumber = GameEngine.tilesNumber;
        int slotResource = 0;
        int slotNumber = 0;
        for (int i = 0; i < tilesResource.length; i++) {
            if (tilesResource[i] == 0) {
                slotResource = i;
                break;
            }
        }
        for (int i = 0; i < tilesNumber.length; i++) {
            if (tilesNumber[i] == 7) {
                slotNumber = i;
                break;
            }
        }
        int save = tilesResource[slotNumber];
        tilesResource[slotNumber] = 0;
        tilesResource[slotResource] = save;
        robberTile = slotNumber;

        for (int[] numberSet : GameEngine.resourceDependencies) {
            int currentTile = numberSet[0];
            MutablePair tile = GameEngine.getPair();
            tile.set(GameEngine.tilesResource[currentTile], GameEngine.tilesNumber[currentTile]);
            for (int i = 1; i < numberSet.length; i++) {
                vertexes[numberSet[i]].resources.add(tile);
            }
        }
    }

    public void setResourceScarcity() {
        for (int i = 0; i < GameEngine.tilesNumber.length; i++) {
            resourceScarcity[GameEngine.tilesResource[i]] += GameEngine.getRarity(GameEngine.tilesNumber[i]);
        }
        resourceScarcity[0] = 0;
    }

    public static void randomizeArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int randomPosition = GameEngine.randomGen.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }
    }

    public void initPlayers(int playerSize) {
        for (int i = 0; i < playerSize; i++) {
            playerList.add(GameEngine.getPlayer());
            playerList.get(i).playerNumber = i;
        }
    }

    public void startSettlement(int playerID) {
        ArrayList<MutablePair> topSettles = topSettles();
        topSettles.sort(Comparator.comparing(MutablePair::getFirst));
        int slot = topSettles.get(topSettles.size() - 1).getSecond();
        int roadSlot = vertexes[slot].getRandomAdjNode();
        playerList.get(playerID).buildSettlement(this, slot, false);
        if (roadSlot != -1) {
            playerList.get(playerID).buildRoad(this, slot, roadSlot);
        }
    }

    public ArrayList<MutablePair> topSettles() {
        Player currentPlayer = playerList.get(playerTurn);
        ArrayList<MutablePair> returnArray = new ArrayList<>();
        for (int i = 0; i < vertexes.length; i++) {
            VertexNode node = vertexes[i];
            if (!node.canBuildCity(this)) {
                continue;
            }
            MutablePair pair = GameEngine.getPair();
            double total = 0;

            int[] production = currentPlayer.production.clone();

            for (MutablePair resource : node.resources) {
                if (resourceScarcity[resource.getFirst()] != 0) {
                    double rarity = GameEngine.getRarity(resource.getSecond());
                    total += (rarity / resourceScarcity[resource.getFirst()]) * (rarity / (rarity + production[resource.getFirst()]));
                    production[resource.getFirst()] += (int) rarity;
                }
            }
            pair.set((int) (total * 100), i);
            returnArray.add(pair);
        }
        return returnArray;
    }

    public void applyDice(int dieRoll) {
        for (Player player : playerList) {
            player.addResources(this, dieRoll);
        }
    }

    @Override
    public String toString() {
        return "{ " +
                "\"playerList\" : {" + playerListToString() +
                "}, " + "\"vertexList\" : {" + vertexListToString() +
                "}, \"devCardPool\" : " + Arrays.toString(devCardPool) +
                ", \"playerTurn\" : " + playerTurn +
                ", \"robberTile\" : " + robberTile +
                ", \"resourceScarcity\" : " + Arrays.toString(resourceScarcity) +
                "}";
    }

    private String playerListToString() {
        StringBuilder returnString = new StringBuilder();
        boolean first = true;
        for (Player player : playerList) {
            if (!first) {
                returnString.append(",");
            }
            returnString.append(player.toString());
            first = false;
        }
        return returnString.toString();
    }

    private String vertexListToString() {
        StringBuilder returnString = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < vertexes.length; i++) {
            if (!first) {
                returnString.append(",");
            }
            returnString.append("\"").append(i).append("\": ").append(vertexes[i].toString());
            first = false;
        }
        return returnString.toString();
    }


    public void reset() {
        for (VertexNode vertexes : vertexes) {
            vertexes.reset();
        }
        for (Player player : playerList) {
            player.reset();
        }
        playerList.clear();
        GameEngine.boardPool.add(this);
    }

    public void clone(BoardState blankState) {
        for (Player player : playerList) {
            blankState.playerList.add(player.clone(GameEngine.getPlayer()));
        }
        for (int i = 0; i < vertexes.length; i++) {
            blankState.vertexes[i] = vertexes[i].clone(GameEngine.getVertex());
        }
        blankState.playerTurn = playerTurn;
        blankState.robberTile = robberTile;
        System.arraycopy(devCardPool, 0, blankState.devCardPool, 0, Player.DEV_LENGTH);
    }

    //TODO----------------------------------------------------------------------
    public void setRobber(int tileNumber) {

    }

    public void playerRobber() {

    }

    public void applyRobber(int tile, int person, int resource) {

    }

    public void shortestPath(VertexNode nodeStart, VertexNode nodeEnd) {

    }

    /*
     * Initialize the ports
     * Randomize, similar to how randomizing tiles and numbers works
     */
    public void initPorts() {

    }
}
