package com.SpringField.engine;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.BoardStateConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class BoardState {
    protected BoardStateConfig config;
    protected Player[] players;
    protected Vertex[] vertices;
    protected byte[] edges;
    protected byte[] resourceCardPool;
    protected byte[] devCardPool;
    protected byte[] devCardsAcquiredThisTurn;
    protected byte playerWithLargestArmy;
    protected byte playerWithLongestRoad;
    protected byte currentLongestRoad;
    protected byte playerTurn;
    protected byte robberTile;
    protected byte turnNumber;
    protected Random r;
    private HashSet<Byte> seenVerticies = new HashSet<>();
    private HashSet<Byte> seenEdges = new HashSet<>();


    protected BoardState() {
    }

    public BoardState(int numPlayers) throws IOException {
        r = new Random();
        this.config = new BoardStateConfig(null, numPlayers, r.nextInt());
        initialize(numPlayers);
    }


    public BoardState(BoardStateConfig config, int numPlayers) {
        this.config = config;
        initialize(numPlayers);
        r = new Random();
    }

    public BoardState(BoardStateConfig config, int numPlayers, long seed) {
        this.config = config;
        initialize(numPlayers);
        r = new Random(seed);
    }

    private BoardState(BoardStateConfig config, Player[] players, Vertex[] vertices, byte[] edges,
            byte[] resourceCardPool, byte[] devCardPool, byte[] devCardsAcquiredThisTurn, byte playerWithLargestArmy,
            byte playerWithLongestRoad, byte currentLongestRoad, byte playerTurn, byte robberTile, byte turnNumber) {
        this.config = config;
        this.players = players;
        this.vertices = vertices;
        this.edges = edges;
        this.resourceCardPool = resourceCardPool;
        this.devCardPool = devCardPool;
        this.devCardsAcquiredThisTurn = devCardsAcquiredThisTurn;
        this.playerWithLargestArmy = playerWithLargestArmy;
        this.playerWithLongestRoad = playerWithLongestRoad;
        this.currentLongestRoad = currentLongestRoad;
        this.playerTurn = playerTurn;
        this.robberTile = robberTile;
        this.turnNumber = turnNumber;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public byte[] getEdges() {
        return edges;
    }

    public Player[] getPlayers() {
        return players;
    }

    public BoardStateConfig getConfig() {
        return config;
    }

    public byte[] getResourceCardPool() {
        return resourceCardPool;
    }

    public byte getPlayerTurn() {
        return playerTurn;
    }

    public byte getRobberTile() {
        return robberTile;
    }

    public byte getPlayerWithLargestArmy() {
        return playerWithLargestArmy;
    }

    public byte getPlayerWithLongestRoad() {
        return playerWithLongestRoad;
    }

    public byte getCurrentLongestRoad() {
        return currentLongestRoad;
    }

    public byte getTurnNumber() {
        return turnNumber;
    }

    public Player getCurrentPlayer() {
        return players[playerTurn];
    }

    protected void initialize(int numPlayers) {
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
        }
        vertices = new Vertex[DEFAULT_NUM_VERTICES];
        for (int i = 0; i < DEFAULT_NUM_VERTICES; i++) {
            vertices[i] = new Vertex(vertexToPort[i]);
        }
        edges = new byte[DEFAULT_NUM_EDGES];
        for (int i = 0; i < DEFAULT_NUM_EDGES; i++) {
            edges[i] = UNASSIGNED_PLAYER;
        }
        for (int i = 0; i < config.getTilesResource().length; i++) {
            if (config.getTilesResource()[i] == DESERT) {
                robberTile = (byte) i;
                break;
            }
        }
        resourceCardPool = new byte[] { DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT,
                DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT };
        devCardPool = new byte[] { DEFAULT_NUM_KNIGHT, DEFAULT_NUM_VICTORY, DEFAULT_NUM_ROAD_BUILDING,
                DEFAULT_NUM_MONOPOLY, DEFAULT_NUM_YEAR_OF_PLENTY };
        devCardsAcquiredThisTurn = new byte[] { 0, 0, 0, 0 };
        playerWithLargestArmy = UNASSIGNED_PLAYER;
        playerWithLongestRoad = UNASSIGNED_PLAYER;
        currentLongestRoad = 0;
        playerTurn = 0;
        turnNumber = 0;
    }

    private boolean inSettlementPhase() {
        return turnNumber < players.length * 2;
    }

    private byte numDevCardsAvailable() {
        byte total = 0;
        for (byte amount : devCardPool) {
            total += amount;
        }
        return total;
    }

    public boolean canBuildRoad(byte edgeId, boolean buy) {
        if (!inSettlementPhase() && buy && !getCurrentPlayer().canBuyRoad()) {
            return false;
        }
        return canBuildRoadHelper(edgeId, UNASSIGNED_EDGE);
    }

    private boolean canBuildRoadHelper(byte edgeId, byte ghostEdge) {
        if (edges[edgeId] != UNASSIGNED_EDGE) {
            return false;
        }
        for (byte v : edgeToVertex[edgeId]) {
            Vertex vertex = vertices[v];
            if(vertex.isSettled()){
                if(vertex.getPlayerId() == playerTurn){
                    return true;
                }
                continue;
            }
            for (byte e : vertexToEdge[v]) {
                if (edges[e] == playerTurn) {
                    return true;
                }
                if (ghostEdge != UNASSIGNED_EDGE && e == ghostEdge) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canBuildSettlement(byte vertexId) {
        if (!inSettlementPhase() && !getCurrentPlayer().canBuySettlement()) {
            return false;
        }
        if (vertices[vertexId].isSettled()) {
            return false;
        }
        boolean foundRoad = inSettlementPhase();
        for (byte e : vertexToEdge[vertexId]) {
            if (edges[e] == playerTurn) {
                foundRoad = true;
            }
            for (byte v : edgeToVertex[e]) {
                Vertex adjacentVertex = vertices[v];
                if (adjacentVertex.isSettled()) {
                    return false;
                }
            }
        }
        // TODO Why does Intellij say this is always false?
        if (!foundRoad) {
            return false;
        }
        return true;
    }

    public boolean canBuildCity(byte vertexId) {
        if (inSettlementPhase() || !getCurrentPlayer().canBuyCity()) {
            return false;
        }
        Vertex v = vertices[vertexId];
        if (v.getBuilding() != STATUS_SETTLEMENT) {
            return false;
        }
        return v.getPlayerId() == playerTurn;
    }

    public boolean canPlayRobber(byte tileId, byte playerIdSteal) {
        if (inSettlementPhase() || playerTurn == playerIdSteal) {
            return false;
        }
        boolean found = false;
        for (byte v : tileToVertex[tileId]) {
            if (vertices[v].getPlayerId() == playerIdSteal) {
                found = true;
            }
        }
        if (!found) {
            return false;
        }
        return robberTile != tileId;
    }

    public boolean canBuyDevCard() {
        if (inSettlementPhase() || numDevCardsAvailable() == 0) {
            return false;
        }
        return getCurrentPlayer().canBuyDevCard();
    }

    public boolean canPlayKnightCard(byte tileId) {
        if (!canPlayDevCard(KNIGHT)) {
            return false;
        }
        return tileId != robberTile;
    }

    public boolean canPlayRoadBuilding(byte e1, byte e2) {
        if (!canPlayDevCard(ROAD_BUILDING)) {
            return false;
        }
        if (!canBuildRoadHelper(e1, UNASSIGNED_EDGE)) {
            return false;
        }
        return canBuildRoadHelper(e2, e1);
    }

    public boolean canPlayMonopoly() {
        return canPlayDevCard(MONOPOLY);
    }

    public boolean canPlayYearOfPlenty(byte r1, byte r2) {
        if (resourceCardPool[r1] == 0) {
            return false;
        }
        if (resourceCardPool[r2] == 0) {
            return false;
        }
        return canPlayDevCard(YEAR_OF_PLENTY);
    }

    public boolean canPlayDevCard(byte type) {
        if (inSettlementPhase()) {
            return false;
        }
        Player p = getCurrentPlayer();
        if (p.getDevCards()[type] <= devCardsAcquiredThisTurn[type]) {
            return false;
        }
        if (!p.canPlayDevCard(type)) {
            return false;
        }
        return true;
    }

    public boolean canTradeBank(byte playerResource, byte bankResource) {
        if (inSettlementPhase()) {
            return false;
        }
        return getCurrentPlayer().canTradeBank(playerResource) && resourceCardPool[bankResource] > 0;
    }

    public boolean canTradePlayer(byte playerId, byte[] giving) {
        if (inSettlementPhase()) {
            return false;
        }
        return players[playerId].canPlayerTrade(giving);
    }

    public void buildRoad(byte edgeId) throws IOException {
        buildRoadHelper(edgeId, !inSettlementPhase());
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ROAD_COMMAND);
            dos.writeByte(edgeId);
        }
    }

    protected void buildRoadHelper(byte edgeId, boolean pay) {
        if (!canBuildRoad(edgeId, pay)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        edges[edgeId] = playerTurn;
        p.buyRoad(pay);
        updateLargestRoad(edgeId);
        if (pay) {
            resourceCardPool[WOOD]++;
            resourceCardPool[BRICK]++;
        }
    }

    public void buildSettlement(byte vertexId) throws IOException {
        if (!canBuildSettlement(vertexId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        Vertex v = vertices[vertexId];
        p.buySettlement(!inSettlementPhase());
        v.setPlayerId(playerTurn);
        v.setBuilding(STATUS_SETTLEMENT);
        if (v.getPort() != UNASSIGNED_PORT) {
            p.addPort(v.getPort());
        }
        if (!inSettlementPhase()) {
            resourceCardPool[WOOD]++;
            resourceCardPool[BRICK]++;
            resourceCardPool[SHEEP]++;
            resourceCardPool[HAY]++;
        }
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(SETTLEMENT_COMMAND);
            dos.writeByte(vertexId);
        }
    }

    public void buildCity(byte vertexId) throws IOException {
        if (!canBuildCity(vertexId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Vertex v = vertices[vertexId];
        Player p = getCurrentPlayer();
        p.buyCity();
        v.setPlayerId(playerTurn);
        v.setBuilding(STATUS_CITY);
        resourceCardPool[HAY] += 2;
        resourceCardPool[ROCK] += 3;
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(CITY_COMMAND);
            dos.writeByte(vertexId);
        }
    }

    public byte playRobber(byte tileId, byte playerIdSteal) throws IOException {
        if (!canPlayRobber(tileId, playerIdSteal)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        robberTile = tileId;
        byte type = players[playerIdSteal].stealResource(r);
        if (type != INVALID_RESOURCE) {
            p.addResource(type, (byte) 1);
        }
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ROBBER_COMMAND);
            dos.writeByte(tileId);
            dos.writeByte(playerIdSteal);
        }
        return type;
    }

    public byte buyDevCard() throws IOException {
        if (!canBuyDevCard()) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        byte type = getRandomSlot(devCardPool, r);
        p.buyDevCard(type);
        devCardPool[type]--;
        if (type != VICTORY) {
            devCardsAcquiredThisTurn[type]++;
        }
        resourceCardPool[SHEEP]++;
        resourceCardPool[HAY]++;
        resourceCardPool[ROCK]++;
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(DEV_CARD_COMMAND);
        }
        return type;
    }

    public byte playKnightCard(byte tileId, byte playerIdSteal) throws IOException {
        if (!canPlayKnightCard(tileId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(KNIGHT);
        updateLargestArmy();
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(KNIGHT_COMMAND);
        }
        return playRobber(tileId, playerIdSteal);
    }

    public void playRoadBuilding(byte e1, byte e2) throws IOException {
        if (!canPlayRoadBuilding(e1, e2)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(ROAD_BUILDING);
        buildRoadHelper(e1, false);
        buildRoadHelper(e1, false);
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ROAD_BUILDING_COMMAND);
            dos.writeByte(e1);
            dos.writeByte(e2);
        }
    }

    /*
     * Currently returns total amount stolen. For proper info we should return the amount stolen from each player as
     * well.
     */
    public byte playMonopoly(byte resourceType) throws IOException {
        if (!canPlayMonopoly()) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(MONOPOLY);
        byte totalStolen = 0;
        for (int i = 0; i < players.length; i++) {
            if (playerTurn == i) {
                break;
            }
            totalStolen += players[i].stealAllResource(resourceType);
        }
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(MONOPOLY_COMMAND);
            dos.writeByte(resourceType);
        }
        return totalStolen;
    }

    public void playYearOfPlenty(byte r1, byte r2) throws IOException {
        if (!canPlayYearOfPlenty(r1, r2)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(YEAR_OF_PLENTY);
        p.addResource(r1, (byte) 1);
        p.addResource(r2, (byte) 1);
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(YEAR_OF_PLENTY_COMMAND);
            dos.writeByte(r1);
            dos.writeByte(r2);
        }
    }

    public void tradeBank(byte playerResource, byte bankResource) throws IOException {
        getCurrentPlayer().tradeBank(playerResource, bankResource);
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(TRADE_BANK_COMMAND);
            dos.writeByte(playerResource);
            dos.writeByte(bankResource);
        }
    }

    public void tradePlayer(byte playerId, byte[] giving, byte[] receiving) throws IOException {
        if (!canTradePlayer(playerTurn, giving) || !canTradePlayer(playerId, receiving)) {
            throw new RuntimeException("Invalid Transaction");
        }
        getCurrentPlayer().tradePlayer(giving, receiving);
        players[playerId].tradePlayer(receiving, giving);
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(TRADE_PLAYER_COMMAND);
            dos.writeByte(playerId);
            config.writeByteArray(giving);
            config.writeByteArray(receiving);
        }
    }

    public byte advanceTurn() throws IOException {
        if (computeVictoryPoints(playerTurn) >= VICTORY_POINTS_REQ_WIN) {
            return WIN_CONDITION;
        }
        Arrays.fill(devCardsAcquiredThisTurn, (byte) 0);
        if (inSettlementPhase()) {
            if (turnNumber < players.length - 1) {
                playerTurn++;
            } else if (turnNumber > players.length - 1 && turnNumber < players.length * 2 - 1) {
                playerTurn--;
            }
        } else {
            playerTurn++;
            if (playerTurn == players.length) {
                playerTurn = 0;
            }
        }
        turnNumber++;
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ADVANCE_TURN_COMMAND);
        }
        return rollDice();
    }

    private byte rollDice() {
        if (inSettlementPhase()) {
            return NO_DICE_ROLL;
        }
        byte roll = (byte) (r.nextInt(6) + r.nextInt(6) + 2);
        if (roll == 7) {
            return roll;
        }
        for (int tileNum = 0; tileNum < tilesResource.length; tileNum++) {
            if (tileNum == robberTile) {
                continue;
            }
            byte resourceType = tilesResource[tileNum];
            if (resourceType == DESERT) {
                continue;
            }
            if (roll == tilesNumber[tileNum]) {
                for (int vertexNum : tileToVertex[tileNum]) {
                    Vertex v = vertices[vertexNum];
                    if (v.isSettled()) {
                        Player p = players[v.getPlayerId()];
                        byte resourceAmount;
                        switch (v.getBuilding()) {
                        case SETTLEMENT:
                            resourceAmount = 1;
                            break;
                        case CITY:
                            resourceAmount = 2;
                            break;
                        default:
                            throw new RuntimeException("Invalid Transaction");
                        }
                        if (resourceCardPool[resourceType] < resourceAmount) {
                            resourceAmount = resourceCardPool[resourceType];
                        }
                        p.addResource(resourceType, resourceAmount);
                    }
                }
            }
        }
        return roll;
    }

    private byte computeArmy(byte playerId) {
        return players[playerId].getKnightsPlayed();
    }

    private void updateLargestArmy() {
        if (playerTurn == playerWithLargestArmy) {
            return;
        }
        if (computeArmy(playerTurn) > computeArmy(playerWithLargestArmy)) {
            playerWithLargestArmy = playerTurn;
        }
    }

    /*
     * Longest Road Algorithm
     */
    private void updateLargestRoad(byte edgeId) {
        seenEdges.add(edgeId);
        byte maxRoadLength = 1;
        for (byte n : edgeToVertex[edgeId]) {
            maxRoadLength += transverseVertex(seenVerticies, seenEdges, n, (byte) 0);
        }
        if (maxRoadLength > currentLongestRoad) {
            currentLongestRoad = maxRoadLength;
            playerWithLongestRoad = playerTurn;
        }
        seenVerticies.clear();
        seenEdges.clear();
        //System.out.println("ALGORITHM FINISHED\n\n\n\n\n\n\n\n");
    }


    private byte transverseVertex(HashSet<Byte> seenVerticies, HashSet<Byte> seenEdges, byte vertexId, byte currentRoadLength) {
        //System.out.println("Explore Vertex : " + vertexId);
        if(seenVerticies.contains(vertexId)){
            //System.out.println("RETURN");
            return currentRoadLength;
        }
        seenVerticies.add(vertexId);
        Vertex v = vertices[vertexId];
        if (v.isSettled() && v.getPlayerId() != playerTurn) {
            //System.out.println("RETURN");
            return currentRoadLength;
        }
        byte maxRoadLength = currentRoadLength;
        byte[] outgoingEdges = vertexToEdge[vertexId];
        for (byte edgeId : outgoingEdges) {
            //System.out.println("Explore Edge : "+ edgeId);
            if (seenEdges.contains(edgeId)) {
                //System.out.println("Edge Skipped");
                continue;
            }
            seenEdges.add(edgeId);
            if (edges[edgeId] == playerTurn) {
                byte nextNodeId = -1;
                for (byte n : edgeToVertex[edgeId]) {
                    if (n != vertexId) {
                        nextNodeId = n;
                    }
                }
                if (nextNodeId == -1) {
                    throw new RuntimeException("Serious Issue Found - Contact Mikhail");
                }
                byte roadLength = transverseVertex(seenVerticies, seenEdges, nextNodeId, ++currentRoadLength);
                if (roadLength > maxRoadLength) {
                    maxRoadLength = roadLength;
                }
            }
        }
        //System.out.println("RETURN");
        return maxRoadLength;
    }

    public byte computeVictoryPoints(byte playerId) {
        byte points = players[playerId].getNumVictoryPoints();
        if (playerWithLargestArmy == playerId) {
            points += 2;
        }
        if (playerWithLongestRoad == playerId) {
            points += 2;
        }
        return points;
    }

//    /*
//     * TO XML - Fields needed: Vertexes - PlayerID - Building - Port
//     *
//     * Edges (byte arr) TilesResource TilesNumber
//     *
//     * Stack: https://stackoverflow.com/questions/7373567/how-to-read-and-write-xml-files
//     */
//    public void toXML(String xml) {
//        // Setup holder variables
//        Vertex[] vertices = getVertices();
//        Document dom;
//        Element e = null;
//
//        // instance of a DocumentBuilderFactory
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//
//        try {
//            // use factory to get an instance of document builder
//            DocumentBuilder db = dbf.newDocumentBuilder();
//
//            // create instance of DOM and string
//            dom = db.newDocument();
//            String data;
//
//            // create the root element
//            Element root = dom.createElement("boardstate");
//            dom.appendChild(root);
//            // Element Vertexes = dom.createElement("Vertexes");
//            // Element Edges = dom.createElement("Edges");
//            // Element tileResource = dom.createElement("tilesResources");
//            // Element tileNumber = dom.createElement("TilesNumber");
//
//            // Nothing like 4 "for" loops to hold your code together
//            byte holder = 0;
//            Element vertexes = dom.createElement("vertexes");
//            for (Vertex v : vertices) {
//                // Parse each vertex's information into a dom element
//                data = Byte.toString((v.getPlayerId())) + " " + Byte.toString(v.getBuilding()) + " "
//                        + Byte.toString(v.getPort());
//                e = dom.createElement("v" + holder);
//                e.appendChild(dom.createTextNode(data));
//
//                // Append each of them to teh vertex's child
//                vertexes.appendChild(e);
//
//                // Accumulator
//                holder++;
//            }
//            root.appendChild(vertexes);
//
//            // Same as above ^^^ but for edges
//            byte ctr = 0;
//            Element edge1 = dom.createElement("edges");
//            for (byte edge : edges) {
//                data = Byte.toString(edge);
//                e = dom.createElement("e" + ctr);
//                e.appendChild(dom.createTextNode(data));
//                edge1.appendChild(e);
//                ctr++;
//            }
//            root.appendChild(edge1);
//
//            // // Again the same but tiles
//            // int limiter = 0;
//            // Element tileResource = dom.createElement("tilesResources");
//            // for (byte b: tilesResource){
//            // data = Byte.toString(b);
//            // e = dom.createElement();
//            // e.appendChild(dom.createTextNode(data)); // <<<<---------- error is happening here with xml formatting
//            // idk
//            // tileResource.appendChild(e);
//            // limiter++;
//            // }
//            // root.appendChild(tileResource);
//            //
//            // // Numbers
//            // int limit = 0;
//            // Element tileNumber = dom.createElement("TilesNumber");
//            // for (byte r: tilesNumber){
//            // data = Byte.toString(r);
//            // e = dom.createElement("");
//            // e.appendChild(dom.createTextNode(data));
//            // tileNumber.appendChild(e);
//            // limit++;
//            // root.appendChild(tileNumber);
//            // }
//
//            // Try downloading the file
//            try {
//                Transformer tr = TransformerFactory.newInstance().newTransformer();
//                tr.setOutputProperty(OutputKeys.INDENT, "yes");
//                tr.setOutputProperty(OutputKeys.METHOD, "xml");
//                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "Boardstate.dtd");
//                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//
//                // send DOM to fil
//                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(xml)));
//            } catch (TransformerException n) {
//                n.printStackTrace();
//            } catch (IOException ioe) {
//                System.out.println(ioe.getMessage());
//            }
//        } catch (ParserConfigurationException pce) {
//            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
//        }
//    }
//
//    /*
//     * TODO: fromXML
//     */
//    public void fromXML(String file) {
//
//    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bOutput);
        config.serialize(output);
        output.writeByte(players.length);
        for (Player p : players) {
            p.serialize(output);
        }
        output.writeByte(vertices.length);
        for (Vertex v : vertices) {
            v.serialize(output);
        }
        writeByteArray(output, edges);
        writeByteArray(output, resourceCardPool);
        writeByteArray(output, devCardPool);
        writeByteArray(output, devCardsAcquiredThisTurn);
        output.writeByte(playerWithLargestArmy);
        output.writeByte(playerWithLongestRoad);
        output.writeByte(currentLongestRoad);
        output.writeByte(playerTurn);
        output.writeByte(robberTile);
        output.writeByte(turnNumber);
        output.flush();
        output.close();
        byte[] result = bOutput.toByteArray();
        bOutput.close();
        return result;
    }

    public static BoardState deSerialize(ObjectInputStream input) throws IOException {
        BoardStateConfig config = BoardStateConfig.deSerialize(input);
        byte lengthPlayers = input.readByte();
        Player[] players = new Player[lengthPlayers];
        for (byte i = 0; i < lengthPlayers; i++) {
            players[i] = Player.deSerialize(input);
        }
        byte lengthVertices = input.readByte();
        Vertex[] vertices = new Vertex[lengthVertices];
        for (byte i = 0; i < lengthVertices; i++) {
            vertices[i] = Vertex.deSerialize(input);
        }
        byte[] edges = readByteArray(input);
        byte[] resourceCardPool = readByteArray(input);
        byte[] devCardPool = readByteArray(input);
        byte[] devCardsAcquiredThisTurn = readByteArray(input);
        byte playerWithLargestArmy = input.readByte();
        byte playerWithLongestRoad = input.readByte();
        byte currentLongestRoad = input.readByte();
        byte playerTurn = input.readByte();
        byte robberTile = input.readByte();
        byte turnNumber = input.readByte();
        return new BoardState(config, players, vertices, edges, resourceCardPool, devCardPool, devCardsAcquiredThisTurn,
                playerWithLargestArmy, playerWithLongestRoad, currentLongestRoad, playerTurn, robberTile, turnNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BoardState that = (BoardState) o;

        if (playerWithLargestArmy != that.playerWithLargestArmy)
            return false;
        if (playerWithLongestRoad != that.playerWithLongestRoad)
            return false;
        if (currentLongestRoad != that.currentLongestRoad)
            return false;
        if (playerTurn != that.playerTurn)
            return false;
        if (robberTile != that.robberTile)
            return false;
        if (turnNumber != that.turnNumber)
            return false;
        if (!Arrays.deepEquals(players, that.players))
            return false;
        if (!Arrays.deepEquals(vertices, that.vertices))
            return false;
        if (!Arrays.equals(edges, that.edges))
            return false;
        if (!Arrays.equals(resourceCardPool, that.resourceCardPool))
            return false;
        if (!Arrays.equals(devCardPool, that.devCardPool))
            return false;
        return Arrays.equals(devCardsAcquiredThisTurn, that.devCardsAcquiredThisTurn);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(players);
        result = 31 * result + Arrays.hashCode(vertices);
        result = 31 * result + Arrays.hashCode(edges);
        result = 31 * result + Arrays.hashCode(resourceCardPool);
        result = 31 * result + Arrays.hashCode(devCardPool);
        result = 31 * result + Arrays.hashCode(devCardsAcquiredThisTurn);
        result = 31 * result + (int) playerWithLargestArmy;
        result = 31 * result + (int) playerWithLongestRoad;
        result = 31 * result + (int) currentLongestRoad;
        result = 31 * result + (int) playerTurn;
        result = 31 * result + (int) robberTile;
        result = 31 * result + (int) turnNumber;
        return result;
    }
}
