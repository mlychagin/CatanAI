package backend;

public class MutablePair implements Comparable<MutablePair> {
  private int first;
  private int second;

  public MutablePair() {
    first = 0;
    second = 0;
  }

  public MutablePair(int first, int second) {
    this.first = first;
    this.second = second;
  }

  public int getFirst() {
    return first;
  }

  public int getSecond() {
    return second;
  }

  public void set(int first, int second) {
    this.first = first;
    this.second = second;
  }

  public void setFirst(int first) {
    this.first = first;
  }

  public void setSecond(int second) {
    this.second = second;
  }

  public MutablePair clone(MutablePair pair) {
    pair.first = first;
    pair.second = second;
    return pair;
  }

  @Override
  public String toString() {
    return "[" + first + "," + second + "]";
  }

  @Override
  public int compareTo(MutablePair o) {
    return o.first - first;
  }
}
