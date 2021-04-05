package game.event;

import game.GameConstants;

public class Bomb implements Event {

  private int bombTimer = GameConstants.BOMB_TIMER;
  private int range;

  public Bomb(double rangeMultiplier) {
    this.range = (int) (GameConstants.DEFAULT_BOMB_RANGE * rangeMultiplier);
  }

  public void update() {
    this.bombTimer--;
  }

  @Override
  public int getTimer() {
    return bombTimer;
  }

  public int getRange() {
    return range;
  }

}
