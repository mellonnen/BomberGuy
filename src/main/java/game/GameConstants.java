package game;

import communication.NetworkConstants;
import game.utility.Position;

/**
 * All times are in ms. 3000 / TICK_RATE corresponds to a 3s timer.
 */
public class GameConstants {

  public static final int TICK_RATE = NetworkConstants.SERVER_UPDATE_INTERVAL;
  public static final int GRID_SIZE = 13;
  public static final double TILE_CENTER = 0.5;

  public static final double DEFAULT_WALK_DISTANCE = 0.25;
  public static final double SPEED_BOOST_WALK_DISTANCE = 0.5;
  public static final int DEFAULT_HITPOINTS = 5;
  public static final int DEFAULT_BOMB_DAMAGE = -1;
  public static final int DEFAULT_BOMB_RANGE = 3;
  public static final int DEFAULT_BOMB_DROP_COOLDOWN = 3000 / TICK_RATE;

  public static final int DEFAULT_POWERUP_HEALTH_BOOST = 1;
  public static final int DEFAULT_BOMB_RANGE_MULTIPLIER = 1;
  public static final int INVULNERABILITY_PERIOD = 500 / TICK_RATE;

  public static final int BOMB_TIMER = 3000 / TICK_RATE;
  public static final int BLAST_TIMER = 1500 / TICK_RATE;
  public static final int POWERUP_ON_GRID_TIMER = 20000 / TICK_RATE;
  public static final int POWERUP_ACTIVATION_TIMER = 10000 / TICK_RATE;

  public static final double TEMPORARY_WALL_SPAWN_CHANCE = 0.1;
  public static final double POWERUP_SPAWN_CHANCE = 0.00002;
  public static final int MIN_WALKABLE_NEIGHBORS_SPAWN = 4;

  public static final Position<Integer> topLeftCorner = new Position<>(0, 0);
  public static final Position<Integer> topRightCorner = new Position<>(GRID_SIZE - 1, 0);
  public static final Position<Integer> bottomRightCorner = new Position<>(GRID_SIZE - 1, GRID_SIZE - 1);
  public static final Position<Integer> bottomLeftCorner = new Position<>(0, GRID_SIZE - 1);

}
