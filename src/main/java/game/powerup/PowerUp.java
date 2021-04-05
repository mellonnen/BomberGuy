package game.powerup;

import game.GameConstants;
import game.event.Event;

public abstract class PowerUp implements Event {

  private int onGridTimer = GameConstants.POWERUP_ON_GRID_TIMER;
  private int timer = 0;

  @Override
  public void update() {
    this.onGridTimer--;
  }

  @Override
  public int getTimer() {
    return onGridTimer;
  }

}
