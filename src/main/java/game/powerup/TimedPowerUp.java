package game.powerup;

import game.GameConstants;

public abstract class TimedPowerUp extends PowerUp {

  private int timer = GameConstants.POWERUP_ACTIVATION_TIMER;

  public boolean isActive() {
    return this.timer != 0;
  }

  public void updateActivationTimer() {
    if (--this.timer < 0) {
      this.timer = 0;
    }
  }

}
