import engine.GameEngine;
import engine.MutablePair;
import engine.VertexNode;

public class VertexEdgeSetup {
    private static GameEngine scenario = new GameEngine();

    public static void main(String args[]){
        System.out.print("VertexEdgeSetup: ");
        scenario.initGame(4);
        VertexNode[] vertices = scenario.boardState.vertices;
        for(int i = 0; i < vertices.length; i++){
            VertexNode v = vertices[i];
            for(MutablePair p : v.listEdges){
                MutablePair pComp = new MutablePair(p.getFirst(), i);
                if(!vertices[p.getSecond()].listEdges.contains(pComp)){
                    VertexNode compNode = vertices[p.getSecond()];
                    System.out.println("Failure");
                    System.exit(1);
                }
            }
        }
        System.out.println("Success");
    }
}