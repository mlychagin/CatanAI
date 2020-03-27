package com.SpringField.engine.util;

import org.junit.Test;

import java.util.Random;

public class MutablePairTest {

    @Test
    public void basicTest() {
        Random random = new Random();
        MutablePair p1 = new MutablePair(random.nextInt(1000), random.nextInt(1000));
        MutablePair p2 = new MutablePair(random.nextInt(1000), random.nextInt(1000));
        MutablePair p1Copy = new MutablePair();
        p1.clone(p1Copy);
        MutablePair p2Copy = new MutablePair();
        p2.clone(p2Copy);

        if (!p1.equals(p1Copy) || p1.hashCode() != p1Copy.hashCode()) {
            System.out.println("Failure");
            System.exit(1);
        }
        if (!p2.equals(p2Copy) || p2.hashCode() != p2Copy.hashCode()) {
            System.out.println("Failure");
            System.exit(1);
        }
        System.out.println("Success");
    }
}
