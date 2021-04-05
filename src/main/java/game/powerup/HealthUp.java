package game.powerup;

import game.GameConstants;

public class HealthUp extends PowerUp {

  private int healthBoost;

  public HealthUp() {
    this.healthBoost = GameConstants.DEFAULT_POWERUP_HEALTH_BOOST;
  }

  public int getHealthBoost() {
    return healthBoost;
  }

}