package client;

import game.TileType;
import game.utility.Move;
import game.utility.Position;

import java.awt.*;

public class TileGraphic {

  private TileType entityType;
  private TileType eventType;
  private String playerName;
  private Move playerDirection;
  private Position<Double> relativePosition;
  private Color playerColor;

  public TileGraphic(TileType entityType, TileType eventType) {
    this.entityType = entityType;
    this.eventType = eventType;
  }

  public TileGraphic(TileType entityType, TileType eventType, String playerName, Move playerDirection,
                     Position<Double> relativePosition,
                     Color playerColor) {
    this.entityType = entityType;
    this.eventType = eventType;
    this.playerName = playerName;
    this.playerDirection = playerDirection;
    this.relativePosition = relativePosition;
    this.playerColor = playerColor;
  }

  public Boolean hasPlayer() {
    return this.playerName != null && this.playerDirection != null;
  }

  public String getPlayerName() {
    return playerName;
  }

  public Move getPlayerDirection() {
    return playerDirection;
  }

  public TileType getEntityType() {
    return this.entityType;
  }

  public TileType getEventType() {
    return this.eventType;
  }

  public String getPlayerSprite() {
    if (Color.red.equals(this.playerColor)) {
      return SpritePath.RED_GUY;
    } else if (Color.blue.equals(this.playerColor)) {
      return SpritePath.BLUE_GUY;
    } else if (Color.green.equals(this.playerColor)) {
      return SpritePath.GREEN_GUY;
    } else if (Color.yellow.equals(this.playerColor)) {
      return SpritePath.YELLOW_GUY;
    }
    return SpritePath.RED_GUY;
  }

  public Position<Double> getRelativePosition() {
    return relativePosition;
  }
}
