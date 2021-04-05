package game.utility;

import java.util.Objects;

public class Position<T> {

  private T x;
  private T y;

  public Position(T x, T y) {
    this.x = x;
    this.y = y;
  }

  public T getX() {
    return x;
  }

  public T getY() {
    return y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Position<?> position = (Position<?>) o;
    return x.equals(position.x) &&
           y.equals(position.y);
  }

  @Override
  public String toString() {
    return "Position [x=" + x + ", y=" + y + "]";
  }
}
