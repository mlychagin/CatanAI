package engine;

public class Util {
  public static int MATERIALS_LENGTH = 6;
  public static int BUILD_LENGTH = 3;
  public static int DEV_LENGTH = 5;

  public static int[] tilesResource =
      new int[] {0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5};
  public static int[] tilesNumber =
      new int[] {2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12};
  public static int[][] vertexDependencies =
      new int[][] {
        new int[] {0, 1, 8},
        new int[] {1, 0, 2},
        new int[] {2, 1, 3, 10},
        new int[] {3, 2, 4},
        new int[] {4, 3, 5, 12},
        new int[] {5, 4, 6},
        new int[] {6, 5, 14},
        new int[] {7, 8, 17},
        new int[] {8, 0, 7, 9},
        new int[] {9, 8, 10, 19},
        new int[] {10, 2, 9, 11},
        new int[] {11, 10, 12, 21},
        new int[] {12, 4, 11, 13},
        new int[] {13, 12, 14, 23},
        new int[] {14, 6, 13, 15},
        new int[] {15, 14, 25},
        new int[] {16, 17, 27},
        new int[] {17, 7, 16, 18},
        new int[] {18, 17, 19, 29},
        new int[] {19, 9, 18, 20},
        new int[] {20, 19, 21, 31},
        new int[] {21, 11, 20, 22},
        new int[] {22, 21, 23, 33},
        new int[] {23, 13, 22, 24},
        new int[] {24, 23, 25, 35},
        new int[] {25, 15, 24, 26},
        new int[] {26, 25, 37},
        new int[] {27, 16, 28},
        new int[] {28, 27, 29, 38},
        new int[] {29, 18, 28, 30},
        new int[] {30, 29, 31, 40},
        new int[] {31, 20, 30, 32},
        new int[] {32, 31, 33, 42},
        new int[] {33, 22, 32, 34},
        new int[] {34, 33, 35, 44},
        new int[] {35, 24, 34, 36},
        new int[] {36, 35, 37, 46},
        new int[] {37, 26, 36},
        new int[] {38, 28, 39},
        new int[] {39, 38, 40, 47},
        new int[] {40, 30, 39, 41},
        new int[] {41, 40, 42, 49},
        new int[] {42, 32, 41, 43},
        new int[] {43, 42, 44, 51},
        new int[] {44, 34, 43, 45},
        new int[] {45, 44, 46, 53},
        new int[] {46, 36, 45},
        new int[] {47, 39, 48},
        new int[] {48, 47, 49},
        new int[] {49, 41, 48, 50},
        new int[] {50, 49, 51},
        new int[] {51, 43, 50, 52},
        new int[] {52, 51, 53},
        new int[] {53, 45, 52}
      };
  public static int[][] resourceDependencies =
      new int[][] {
        new int[] {0, 0, 1, 2, 8, 9, 10},
        new int[] {1, 2, 3, 4, 10, 11, 12},
        new int[] {2, 4, 5, 6, 12, 13, 14},
        new int[] {3, 7, 8, 9, 17, 18, 19},
        new int[] {4, 9, 10, 11, 19, 20, 21},
        new int[] {5, 11, 12, 13, 21, 22, 23},
        new int[] {6, 13, 14, 15, 23, 24, 25},
        new int[] {7, 16, 17, 18, 27, 28, 29},
        new int[] {8, 18, 19, 20, 29, 30, 31},
        new int[] {9, 20, 21, 22, 31, 32, 33},
        new int[] {10, 22, 23, 24, 33, 34, 35},
        new int[] {11, 24, 25, 26, 35, 36, 37},
        new int[] {12, 28, 29, 30, 38, 39, 40},
        new int[] {13, 30, 31, 32, 40, 41, 42},
        new int[] {14, 32, 33, 34, 42, 43, 44},
        new int[] {15, 34, 35, 36, 44, 45, 46},
        new int[] {16, 39, 40, 41, 47, 48, 49},
        new int[] {17, 41, 42, 43, 49, 50, 51},
        new int[] {18, 43, 44, 45, 51, 52, 53},
      };
}
