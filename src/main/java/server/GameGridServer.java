package server;

import client.Action;
import game.GameConstants;
import game.Tile;
import game.entity.Player;
import game.event.Blast;
import game.event.Bomb;
import game.powerup.PowerUp;
import game.utility.GridTools;
import game.utility.Move;
import game.utility.Position;
import game.utility.PowerUpApplicator;

import java.awt.*;
import java.util.List;
import java.util.*;

public class GameGridServer {

  private final Tile[][] gameGrid;
  private final Map<String, Color> playerColors = new HashMap<>();
  private final Stack<Color> colors = new Stack<>();
  private final List<Player> deadPlayers = new ArrayList<>();
  private int playerCount = 0;

  public GameGridServer() {
    gameGrid = new Tile[GameConstants.GRID_SIZE][GameConstants.GRID_SIZE];
    GridTools.generateEmptyTiles(gameGrid);
    GridTools.generatePermanentWalls(gameGrid);
    GridTools.generateTemporaryWalls(gameGrid);
    colors.push(Color.yellow);
    colors.push(Color.green);
    colors.push(Color.blue);
    colors.push(Color.red);
  }

  public Tile[][] getGameGrid() {
    return gameGrid;
  }

  public List<Player> getDeadPlayers() {
    return deadPlayers;
  }

  public Map<String, Color> getPlayerColors() {
    return playerColors;
  }

  public void createPlayer(String name) {
    playerCount++;
    Player player = new Player(name, colors.pop(), playerCount <= 2 ? Move.DOWN : Move.UP);
    playerColors.put(player.getName(), player.getColor());
    Position<Integer> spawnPosition = GridTools.getSpawnPositionInQuadrant(playerCount);
    gameGrid[spawnPosition.getX()][spawnPosition.getY()].setEntity(player);
  }

  public void updateGrid() {
    for (int x = 0; x < gameGrid.length; x++) {
      for (int y = 0; y < gameGrid.length; y++) {
        if (gameGrid[x][y].hasPlayer()) {
          Player player = ((Player) gameGrid[x][y].getEntity());
          player.updateBombDropCooldown();
          if (player.hasTimedPowerUp()) {
            player.updateTimedPowerUp();
          }
          if (player.getHitPoints() <= 0) {
            deadPlayers.add(player);
            gameGrid[x][y].setEntity(null);
          }
          if (gameGrid[x][y].getEvent() instanceof PowerUp) {
            PowerUp powerUp = (PowerUp) gameGrid[x][y].getEvent();
            gameGrid[x][y].setEvent(null);
            PowerUpApplicator.applyPowerUp(player, powerUp);
          } else if (gameGrid[x][y].getEvent() instanceof Blast) {
            if (player.isInvulnerable()) {
              player.decrementInvulnerability();
            } else {
              player.adjustHitPoints(GameConstants.DEFAULT_BOMB_DAMAGE);
              player.setInvulnerable();
            }
          }
        }
        if (gameGrid[x][y].hasEvent()) {
          if (gameGrid[x][y].getEvent().getTimer() == 0) {
            if (gameGrid[x][y].getEvent() instanceof Bomb) {
              Bomb bomb = ((Bomb) gameGrid[x][y].getEvent());
              GridTools.setBombBlast(gameGrid, new Position<>(x, y), bomb);
            } else {
              gameGrid[x][y].setEvent(null);
            }
          } else {
            gameGrid[x][y].getEvent().update();
          }
        }
        if (GridTools.isPositionWalkable(gameGrid, new Position<>(x, y))
            && !gameGrid[x][y].hasEvent()) {
          GridTools.randomSpawnPowerUp(gameGrid, new Position<>(x, y));
        }
      }
    }
  }

  public void executePlayerAction(String name, Action playerAction) {
    Position<Integer> playerPosition = GridTools.getPlayerPositionByName(gameGrid, name);
    Position<Double> relativePlayerPosition =
        GridTools.getPlayerRelativePositionByName(gameGrid, name);
    if (playerPosition.equals(new Position<>(-1, -1))) return;

    Player player = (Player) gameGrid[playerPosition.getX()][playerPosition.getY()].getEntity();
    Move move = playerAction.getMove();
    player.setDirection(move);

    if (playerAction.isBombDropped() && player.hasBombDrop()) {
      player.resetBombDropCooldown();
      gameGrid[playerPosition.getX()][playerPosition.getY()].setEvent(
          new Bomb(player.getRangeMultiplier()));
    }

    double walkDistance = player.getWalkDistance();
    Position<Double> newRelativePosition;
    Position<Integer> newPosition;
    switch (move) {
      case UP:
        newPosition = new Position<>(playerPosition.getX(), playerPosition.getY() - 1);
        if (!GridTools.isPositionWalkable(gameGrid, newPosition)) {
          player.setRelativePosition(GameConstants.TILE_CENTER, GameConstants.TILE_CENTER);
          return;
        }
        if (0 > relativePlayerPosition.getY() - walkDistance) {
          newRelativePosition =
              new Position<>(
                  GameConstants.TILE_CENTER, (1 + relativePlayerPosition.getY()) - walkDistance);
          movePlayer(player, playerPosition, newPosition, newRelativePosition);
        } else {
          player.setRelativePosition(
              GameConstants.TILE_CENTER, relativePlayerPosition.getY() - walkDistance);
        }
        break;
      case DOWN:
        newPosition = new Position<>(playerPosition.getX(), playerPosition.getY() + 1);
        if (!GridTools.isPositionWalkable(gameGrid, newPosition)) {
          player.setRelativePosition(GameConstants.TILE_CENTER, GameConstants.TILE_CENTER);
          return;
        }
        if (1 < relativePlayerPosition.getY() + walkDistance) {
          newRelativePosition =
              new Position<>(
                  GameConstants.TILE_CENTER, (relativePlayerPosition.getY() - 1) + walkDistance);
          movePlayer(player, playerPosition, newPosition, newRelativePosition);
        } else {
          player.setRelativePosition(
              GameConstants.TILE_CENTER, relativePlayerPosition.getY() + walkDistance);
        }
        break;
      case LEFT:
        newPosition = new Position<>(playerPosition.getX() - 1, playerPosition.getY());
        if (!GridTools.isPositionWalkable(gameGrid, newPosition)) {
          player.setRelativePosition(GameConstants.TILE_CENTER, GameConstants.TILE_CENTER);
          return;
        }
        if (0 > relativePlayerPosition.getX() - walkDistance) {
          newRelativePosition =
              new Position<>(
                  (1 + relativePlayerPosition.getX()) - walkDistance, GameConstants.TILE_CENTER);
          movePlayer(player, playerPosition, newPosition, newRelativePosition);
        } else {
          player.setRelativePosition(
              relativePlayerPosition.getX() - walkDistance, GameConstants.TILE_CENTER);
        }
        break;
      case RIGHT:
        newPosition = new Position<>(playerPosition.getX() + 1, playerPosition.getY());
        if (!GridTools.isPositionWalkable(gameGrid, newPosition)) {
          player.setRelativePosition(GameConstants.TILE_CENTER, GameConstants.TILE_CENTER);
          return;
        }
        if (1 < relativePlayerPosition.getX() + walkDistance) {
          newRelativePosition =
              new Position<>(
                  (relativePlayerPosition.getX() - 1) + walkDistance, GameConstants.TILE_CENTER);
          movePlayer(player, playerPosition, newPosition, newRelativePosition);
        } else {
          player.setRelativePosition(
              relativePlayerPosition.getX() + walkDistance, GameConstants.TILE_CENTER);
        }
        break;
      case NONE:
        break;
      default:
        throw new IllegalStateException("Incorrect direction: " + move);
    }
  }

  private void movePlayer(
      Player player,
      Position<Integer> currentPos,
      Position<Integer> nextPos,
      Position<Double> newRelativePos) {
    if (GridTools.isPositionWalkable(gameGrid, nextPos)) {
      player.setRelativePosition(newRelativePos.getX(), newRelativePos.getY());
      gameGrid[currentPos.getX()][currentPos.getY()].setEntity(null);
      gameGrid[nextPos.getX()][nextPos.getY()].setEntity(player);
    }
  }
}
