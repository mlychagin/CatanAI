package com.SpringField.engine.util;

import org.junit.Test;

import java.util.Arrays;

import static com.SpringField.engine.util.Util.*;

public class MapReferencing {

    @Test
    public void printVertexToEdge() {
        initializeStaticInstance();
        System.out.println(Arrays.deepToString(vertexToEdge));
    }

    @Test
    public void printVertexToVertex() {
        initializeStaticInstance();
        System.out.println(Arrays.deepToString(vertexToVertex));
    }

    @Test
    public void printEdgeToEdge() {
        initializeStaticInstance();
        System.out.println(Arrays.deepToString(edgeToEdge));
    }

}
