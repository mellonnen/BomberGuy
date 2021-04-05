package game.utility;

import game.GameConstants;
import game.Tile;
import game.TileType;
import game.entity.PermanentWall;
import game.entity.Player;
import game.entity.TemporaryWall;
import game.event.Blast;
import game.event.Bomb;
import game.powerup.*;

import java.util.*;

public class GridTools {

  private final static Random globalRandom = new Random();

  /**
   * Generates empty tiles everywhere
   */
  public static void generateEmptyTiles(Tile[][] gameGrid) {
    for (int x = 0; x < GameConstants.GRID_SIZE; x++) {
      for (int y = 0; y < GameConstants.GRID_SIZE; y++) {
        gameGrid[x][y] = new Tile();
      }
    }
  }

  /**
   * Generates permanent walls at odd positions
   */
  public static void generatePermanentWalls(Tile[][] gameGrid) {
    for (int x = 1; x < GameConstants.GRID_SIZE; x += 2) {
      for (int y = 1; y < GameConstants.GRID_SIZE; y += 2) {
        gameGrid[x][y] = new Tile(TileType.PERMANENT_WALL);
      }
    }
  }

  public static void generateTemporaryWalls(Tile[][] gameGrid) {
    for (int x = 0; x < GameConstants.GRID_SIZE; x++) {
      for (int y = 0; y < GameConstants.GRID_SIZE; y++) {
        boolean addWall = gameGrid[x][y].isWalkable() &&
                          globalRandom.nextDouble() <= GameConstants.TEMPORARY_WALL_SPAWN_CHANCE &&
                          !inSpawnPositionBounds(new Position<>(x, y));
        if (addWall) {
          // add current position as temporary wall
          gameGrid[x][y] = new Tile(TileType.TEMPORARY_WALL);
          // try to find some unoccupied/walkable neighbors for it
          ArrayList<Position<Integer>> neighbors =
              getWalkableNeighbors(gameGrid, new Position<>(x, y), randomizeTemporaryWallLength());
          // add the found neighbors to the grid
          for (Position<Integer> neighbor : neighbors) {
            gameGrid[neighbor.getX()][neighbor.getY()] = new Tile(TileType.TEMPORARY_WALL);
          }
        }
      }
    }
  }

  private static int randomizeTemporaryWallLength() {
    int range = globalRandom.nextInt(100);
    if (range < 10) {
      return 4;
    } else if (range < 35) {
      return 3;
    } else if (range < 60) {
      return 2;
    } else {
      return 1;
    }
  }

  public static void randomSpawnPowerUp(Tile[][] gameGrid, Position<Integer> pos) {
    double chance = globalRandom.nextDouble();
    if (chance <= GameConstants.POWERUP_SPAWN_CHANCE) {
      List<PowerUp> powerUps =
          List.of(new HealthUp(), new MegaBlastRange(globalRandom), new SpeedBoost(), new NoBombDropCooldown());
      gameGrid[pos.getX()][pos.getY()].setEvent(powerUps.get(globalRandom.nextInt(powerUps.size())));
    }
  }

  public static boolean inBounds(Position<Integer> position) {
    return position.getX() < GameConstants.GRID_SIZE &&
           position.getX() >= 0 &&
           position.getY() < GameConstants.GRID_SIZE &&
           position.getY() >= 0;
  }

  public static boolean inSpawnPositionBounds(Position<Integer> position) {
    return (inSpawnPositionBounds(position, 0) ||
            inSpawnPositionBounds(position, GameConstants.GRID_SIZE - GameConstants.MIN_WALKABLE_NEIGHBORS_SPAWN));
  }

  private static boolean inSpawnPositionBounds(Position<Integer> position, int start) {
    for (int x = start; x < start + GameConstants.MIN_WALKABLE_NEIGHBORS_SPAWN; x++) {
      if (position.getX() == x && (position.getY() == 0 || position.getY() == GameConstants.GRID_SIZE - 1)) {
        return true;
      }
    }
    for (int y = start; y < start + GameConstants.MIN_WALKABLE_NEIGHBORS_SPAWN; y++) {
      if (position.getY() == y && position.getX() == 0 ||
          position.getY() == y && position.getX() == GameConstants.GRID_SIZE - 1) {
        return true;
      }
    }
    return false;
  }

  public static Position<Integer> getSpawnPositionInQuadrant(int quadrant) {
    return List.of(GameConstants.topLeftCorner,
        GameConstants.topRightCorner,
        GameConstants.bottomRightCorner,
        GameConstants.bottomLeftCorner).get(quadrant - 1);
  }

  public static boolean isPositionWalkable(Tile[][] gameGrid, Position<Integer> position) {
    return inBounds(position) && gameGrid[position.getX()][position.getY()].isWalkable();
  }

  public static Position<Double> getPlayerRelativePositionByName(Tile[][] gameGrid, String name) {
    return Arrays.stream(gameGrid)
                 .flatMap(Arrays::stream)
                 .dropWhile(tile -> !(tile.hasEntity() && tile.getEntity() instanceof Player &&
                                      ((Player) tile.getEntity()).getName().equals(name)))
                 .findFirst()
                 .map(tile -> ((Player) tile.getEntity()).getRelativePosition())
                 .orElse(new Position<>(-1.0, -1.0));
  }

  /**
   * @param name The name of the player
   * @return Pair: (-1, -1) if player not found, otherwise player position
   */
  public static Position<Integer> getPlayerPositionByName(Tile[][] gameGrid, String name) {
    for (int x = 0; x < GameConstants.GRID_SIZE; x++) {
      for (int y = 0; y < GameConstants.GRID_SIZE; y++) {
        Tile tile = gameGrid[x][y];
        if (tile.hasEntity() && tile.getEntity() instanceof Player &&
            ((Player) tile.getEntity()).getName().equals(name)) {
          return new Position<>(x, y);
        }
      }
    }
    return new Position<>(-1, -1);
  }


  private static ArrayList<Position<Integer>> getWalkableNeighbors(Tile[][] gameGrid, Position<Integer> start,
                                                                   int limit) {
    ArrayList<Position<Integer>> neighbors = new ArrayList<>();
    LinkedList<Position<Integer>> queue = new LinkedList<>();
    queue.add(start);
    while (neighbors.size() <= limit) {
      // we couldn't create more neighbors, break out
      if (queue.isEmpty()) break;
      Position<Integer> current = queue.poll();

      List<Position<Integer>> neighborCandidates = new ArrayList<>(List.of(
          new Position<>(current.getX(), current.getY() - 1),
          new Position<>(current.getX(), current.getY() + 1),
          new Position<>(current.getX() - 1, current.getY()),
          new Position<>(current.getX() + 1, current.getY())));
      // make sure patterns are random by adding neighbors in different orders
      Collections.shuffle(neighborCandidates);

      for (Position<Integer> neighborCandidate : neighborCandidates) {
        // add the neighbor if it's in bounds and it is a walkable tile
        if (inBounds(neighborCandidate) &&
            !neighbors.contains(neighborCandidate) &&
            gameGrid[neighborCandidate.getX()][neighborCandidate.getY()].isWalkable() &&
            !inSpawnPositionBounds(neighborCandidate)) {
          neighbors.add(neighborCandidate);
          queue.add(neighborCandidate);
        }
      }
    }
    return neighbors;
  }

  public static void setBombBlast(Tile[][] gameGrid, Position<Integer> pos, Bomb bomb) {
    int range = bomb.getRange();
    gameGrid[pos.getX()][pos.getY()].setEvent(new Blast());
    setBlastDirectional(gameGrid, pos, range, 1, 0);
    setBlastDirectional(gameGrid, pos, range, -1, 0);
    setBlastDirectional(gameGrid, pos, range, 0, 1);
    setBlastDirectional(gameGrid, pos, range, 0, -1);
  }

  private static void setBlastDirectional(Tile[][] gameGrid, Position<Integer> pos, int range, int xMult, int yMult) {
    Tile currentTile;
    for (int i = 1; i < range; i++) {
      if (!GridTools.inBounds(new Position<>(pos.getX() + (xMult * i), pos.getY() + (yMult * i)))) break;
      currentTile = gameGrid[pos.getX() + (xMult * i)][pos.getY() + (yMult * i)];
      if (currentTile.getEntity() instanceof PermanentWall) break;
      if (currentTile.getEvent() instanceof Bomb) {
        Bomb bomb = (Bomb) currentTile.getEvent();
        setBombBlast(gameGrid, new Position<>(pos.getX() + (xMult * i), pos.getY() + (yMult * i)), bomb);
      }
      currentTile.setEvent(new Blast());
      if (currentTile.getEntity() instanceof TemporaryWall) {
        currentTile.setEntity(null);
        break;
      }
    }
  }


}
