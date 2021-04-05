package game;

import game.entity.Entity;
import game.entity.PermanentWall;
import game.entity.Player;
import game.entity.TemporaryWall;
import game.event.Blast;
import game.event.Bomb;
import game.event.Event;
import game.powerup.HealthUp;
import game.powerup.MegaBlastRange;
import game.powerup.NoBombDropCooldown;
import game.powerup.SpeedBoost;

public class Tile {

  private Event event;
  private Entity entity;

  public Tile() {
  }

  public Tile(TileType tileType) {
    switch (tileType) {
      case PERMANENT_WALL:
        this.entity = new PermanentWall();
        break;
      case TEMPORARY_WALL:
        this.entity = new TemporaryWall();
        break;
    }
  }

  public boolean hasEvent() {
    return event != null;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public boolean hasEntity() {
    return this.entity != null;
  }

  public boolean hasPlayer() {
    return this.entity instanceof Player;
  }

  public Entity getEntity() {
    return this.entity;
  }

  public TileType getEntityType() {
    if (this.entity instanceof PermanentWall) return TileType.PERMANENT_WALL;
    if (this.entity instanceof TemporaryWall) return TileType.TEMPORARY_WALL;
    return TileType.NONE;
  }

  public TileType getEventType() {
    if (this.event instanceof MegaBlastRange) return TileType.POWERUP_MEGA_BLAST_RANGE;
    if (this.event instanceof HealthUp) return TileType.POWERUP_HEALTH_UP;
    if (this.event instanceof NoBombDropCooldown) return TileType.POWERUP_NO_BOMB_DROP_COOLDOWN;
    if (this.event instanceof SpeedBoost) return TileType.POWERUP_SPEED_BOOST;
    if (this.event instanceof Bomb) return TileType.BOMB;
    if (this.event instanceof Blast) return TileType.BLAST;
    return TileType.NONE;
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  // check if tile is walkable (is there a bomb or a player or a wall here?)
  public boolean isWalkable() {
    return this.entity == null && !(this.event instanceof Bomb);
  }

}
