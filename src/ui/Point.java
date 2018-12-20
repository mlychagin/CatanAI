package ui;

public class Point {

  private int x, y;
  private int slot;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setSlot(int slot) {
    this.slot = slot;
  }

  public int getSlot() {
    return slot;
  }
}
