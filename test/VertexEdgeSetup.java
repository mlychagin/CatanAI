import engine.GameEngine;
import engine.MutablePair;
import engine.VertexNode;

public class VertexEdgeSetup {
    private static GameEngine scenario = new GameEngine();

    public static void main(String args[]){
        System.out.print("VertexEdgeSetup: ");
        scenario.initGame(4);
        VertexNode[] vertices = GameEngine.vertices;
        for(int i = 0; i < vertices.length; i++){
            VertexNode v = vertices[i];
            for(MutablePair p : v.listEdges){
                if(!vertices[p.getSecond()].listEdges.contains(new MutablePair(p.getFirst(), i))){
                    System.out.println("Failure");
                    System.exit(1);
                }
            }
        }
        System.out.println("Success");
    }
}