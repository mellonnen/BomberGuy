package game.entity;

import game.GameConstants;
import game.powerup.NoBombDropCooldown;
import game.powerup.PowerUp;
import game.powerup.SpeedBoost;
import game.powerup.TimedPowerUp;
import game.utility.Move;
import game.utility.Position;

import java.awt.*;

public class Player implements Entity {

  private String name;
  private Color color;
  private Move direction;
  private Position<Double> relativePosition;
  private int hitPoints;
  private double walkDistance;
  private int bombDropCooldown;
  private PowerUp powerUp;
  private int invulnerabilityPeriod;
  private double rangeMultiplier;


  public Player(String name) {
    this.name = name;
    this.hitPoints = GameConstants.DEFAULT_HITPOINTS;
    this.walkDistance = GameConstants.DEFAULT_WALK_DISTANCE;
    this.bombDropCooldown = 0;
    this.direction = Move.DOWN;
    this.relativePosition = new Position<>(GameConstants.TILE_CENTER, GameConstants.TILE_CENTER);
    this.invulnerabilityPeriod = 0;
    this.rangeMultiplier = GameConstants.DEFAULT_BOMB_RANGE_MULTIPLIER;
  }

  public Player(String name, Color color) {
    this(name);
    this.color = color;
  }

  public Player(String name, Color color, Move direction) {
    this(name, color);
    this.direction = direction;
  }

  public String getName() {
    return name;
  }

  public int getHitPoints() {
    return hitPoints;
  }

  public void adjustHitPoints(int delta) {
    this.hitPoints = hitPoints + delta;
  }

  public void setPowerUp(PowerUp powerUp) {
    this.powerUp = powerUp;
  }

  public void setInvulnerable() {
    invulnerabilityPeriod = GameConstants.INVULNERABILITY_PERIOD;
  }

  public void decrementInvulnerability() {
    if (--invulnerabilityPeriod < 0) {
      this.invulnerabilityPeriod = 0;
    }
  }

  public boolean isInvulnerable() {
    return invulnerabilityPeriod > 0;
  }

  public Color getColor() {
    return this.color;
  }

  public Move getDirection() {
    return direction;
  }

  public void setDirection(Move direction) {
    this.direction = direction;
  }

  public double getRangeMultiplier() {
    return rangeMultiplier;
  }

  public void setRangeMultiplier(double rangeMultiplier) {
    this.rangeMultiplier = rangeMultiplier;
  }

  public void resetPowerUp() {
    this.powerUp = null;
    this.walkDistance = GameConstants.DEFAULT_WALK_DISTANCE;
    this.rangeMultiplier = GameConstants.DEFAULT_BOMB_RANGE_MULTIPLIER;
  }

  public boolean hasTimedPowerUp() {
    return this.powerUp != null && this.powerUp instanceof TimedPowerUp;
  }

  public void updateTimedPowerUp() {
    if (!hasTimedPowerUp()) return;
    TimedPowerUp timedPowerUp = (TimedPowerUp) this.powerUp;
    if (timedPowerUp.isActive()) {
      timedPowerUp.updateActivationTimer();
    } else {
      resetPowerUp();
    }
  }

  public void updateBombDropCooldown() {
    if (--this.bombDropCooldown < 0 || this.powerUp instanceof NoBombDropCooldown) {
      this.bombDropCooldown = 0;
    }
  }

  public void resetBombDropCooldown() {
    if (this.powerUp instanceof NoBombDropCooldown) {
      this.bombDropCooldown = 0;
    } else {
      this.bombDropCooldown = GameConstants.DEFAULT_BOMB_DROP_COOLDOWN;
    }
  }

  public boolean hasBombDrop() {
    return this.bombDropCooldown == 0;
  }

  public void setRelativePosition(double x, double y) {
    this.relativePosition = new Position<>(x, y);
  }

  public Position<Double> getRelativePosition() {
    return relativePosition;
  }

  public double getWalkDistance() {
    if (hasTimedPowerUp() && this.powerUp instanceof SpeedBoost) {
      return GameConstants.SPEED_BOOST_WALK_DISTANCE;
    }
    return walkDistance;
  }
}
