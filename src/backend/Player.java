package backend;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    public static int MATERIALS_LENGTH = 6;
    public static int BUILD_LENGTH = 3;
    public static int DEV_LENGTH = 5;
    /*
     * 0 = Desert
     * 1 = Wood
     * 2 = Brick
     * 3 = Sheep
     * 4 = Hay
     * 5 = Rock
     */
    int[] materials = new int[]{0, 0, 0, 0, 0, 0};
    int[] production = new int[]{0, 0, 0, 0, 0, 0};

    /*
     * 0 = Roads
     * 1 = Settlements
     * 2 = Cities
     */
    int[] buildMaterials = new int[]{20, 5, 4};

    /*
     * 0 = Knight
     * 1 = Victory
     * 2 = Road Building
     * 3 = Monopoly
     * 4 = Year of Plenty
     */
    int[] devCardsHolding = new int[]{0, 0, 0, 0, 0};
    int[] devCardsSeen = new int[]{0, 0, 0, 0, 0};
    ArrayList<Integer> currentCities = GameEngine.getIntArray();

    int currentRoad;
    int currentArmy;
    int victoryPoints;
    int playerNumber;

    Player() {
        currentRoad = 0;
        currentArmy = 0;
        victoryPoints = 0;
    }

    public void addResources(BoardState boardState, int dieRoll) {
        boolean robbed = false;
        int[] robbedTiles = GameEngine.resourceDependencies[boardState.robberTile];
        for (Integer slot : currentCities) {
            for (int i = 1; i < robbedTiles.length; i++) {
                if (robbedTiles[i] == slot) {
                    robbed = true;
                }
            }
            if (robbed) {
                robbed = false;
                continue;
            }
            VertexNode node = boardState.vertexes[slot];
            int strength = node.city.getSecond();
            for (MutablePair pair : node.resources) {
                if (pair.getSecond() == dieRoll) {
                    materials[pair.getFirst()] += strength;
                }
            }
        }
    }

    //Todo: Add error checking
    public void buildSettlement(BoardState boardState, int slot, boolean pay) {
        VertexNode node = boardState.vertexes[slot];
        if (pay) {
            materials[1] = -1;
            materials[2] = -1;
            materials[3] = -1;
            materials[4] = -1;
        }
        buildMaterials[1] -= 1;
        addProduction(node);
        node.city.set(playerNumber, 1);
        currentCities.add(slot);
        System.out.println("backend.Player(" + playerNumber + ") : BuildSettlement(" + slot + ")");
    }

    //Todo: Add error checking
    public void buildCity(BoardState boardState, int slot) {
        VertexNode node = boardState.vertexes[slot];
        materials[4] = -2;
        materials[5] = -3;
        addProduction(node);
        node.city.setSecond(2);
    }

    void addProduction(VertexNode node) {
        for (MutablePair pair : node.resources) {
            production[pair.getFirst()] += GameEngine.getRarity(pair.getSecond());
        }
        production[0] = 0;
    }

    public void buyDevCard(int kind) {
        materials[3] = -1;
        materials[4] = -1;
        materials[5] = -1;
    }

    @Override
    public String toString() {
        return "\"" + playerNumber + "\" : { " +
                "\"materials\" : " + Arrays.toString(materials) +
                ", \"production\" : " + Arrays.toString(production) +
                ", \"buildMaterials\" : " + Arrays.toString(buildMaterials) +
                ", \"devCardsHolding\" : " + Arrays.toString(devCardsHolding) +
                ", \"devCardsSeen\" : " + Arrays.toString(devCardsSeen) +
                ", \"currentCities\" : " + Arrays.toString(currentCities.toArray()) +
                ", \"currentRoad\" : " + currentRoad +
                ", \"currentArmy\" : " + currentArmy +
                ", \"victoryPoints\" : " + victoryPoints +
                "}";
    }

    public Player clone(Player player) {
        System.arraycopy(materials, 0, player.materials, 0, MATERIALS_LENGTH);
        System.arraycopy(production, 0, player.production, 0, MATERIALS_LENGTH);
        System.arraycopy(buildMaterials, 0, player.buildMaterials, 0, BUILD_LENGTH);
        System.arraycopy(devCardsHolding, 0, player.devCardsHolding, 0, DEV_LENGTH);
        System.arraycopy(devCardsSeen, 0, player.devCardsSeen, 0, DEV_LENGTH);
        player.currentCities.addAll(currentCities);
        player.currentRoad = currentRoad;
        player.currentArmy = currentArmy;
        player.victoryPoints = victoryPoints;
        player.playerNumber = playerNumber;
        return player;
    }

    public void reset() {
        currentCities.clear();
        GameEngine.playerPool.add(this);
    }

    //TODO----------------------------------------------------------------------


    //Ideas: Error check to see if buildRoad/buildSettlement/buildCity can actually build

    /*
     * Builds a road given two Vertexes
     */
    public void buildRoad(BoardState boardState, int startSlot, int endSlot) {
        materials[1] -= 1;
        materials[2] -= 2;
        VertexNode node1 = boardState.vertexes[startSlot];
        VertexNode node2 = boardState.vertexes[endSlot];

        for (MutablePair pair : node1.listEdges) {
            if (pair.getSecond() == endSlot && pair.getFirst() == -1) {
                pair.setFirst(playerNumber);
            }
        }

        for (MutablePair pair : node2.listEdges) {
            if (pair.getSecond() == startSlot && pair.getFirst() == -1) {
                pair.setFirst(playerNumber);
            }
        }

    }


    /*
     * Draws a dev card from the bank and adds it to current player's hand
     *    True = Draw successful
     *    False = Draw unsuccessful
     */
    public boolean drawDevCard(int[] devCardPool) {
        return false;
    }

    /*
     * Calculate longestRoad that the player has
     */
    public int calculateLongestRoad() {
        return -1;
    }

}
