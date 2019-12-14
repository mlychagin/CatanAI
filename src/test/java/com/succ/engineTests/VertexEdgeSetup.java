package com.succ.engineTests;

import com.succ.engine.GameEngine;
import com.succ.engine.MutablePair;
import com.succ.engine.VertexNode;

public class VertexEdgeSetup {
  private static GameEngine scenario = new GameEngine();

  public static void main(String args[]) {
    System.out.print("com.succ.engineTests.VertexEdgeSetup: ");
    scenario.initGame(4);
    VertexNode[] vertices = GameEngine.vertices;
    for (int i = 0; i < vertices.length; i++) {
      VertexNode v = vertices[i];
      for (MutablePair p : v.listEdges) {
        if (!vertices[p.getSecond()].listEdges.contains(new MutablePair(p.getFirst(), i))) {
          System.out.println("Failure");
          System.exit(1);
        }
      }
    }
    System.out.println("Success");
  }
}
