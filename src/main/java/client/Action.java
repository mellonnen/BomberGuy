package client;

import game.utility.Move;

public class Action {

  private Move move;
  private boolean bombDropped;

  public Action() {
    reset();
  }

  public void setMove(Move move) {
    this.move = move;
  }

  public void setBombDropped(boolean dropBomb) {
    bombDropped = dropBomb;
  }

  public Move getMove() {
    return move;
  }

  public boolean isBombDropped() {
    return bombDropped;
  }

  public void reset() {
    this.move = Move.NONE;
    bombDropped = false;
  }
}
