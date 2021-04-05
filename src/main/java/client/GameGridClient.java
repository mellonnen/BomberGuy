package client;

import game.GameConstants;
import game.TileType;
import game.utility.Position;
import utility.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static game.TileType.*;

public class GameGridClient extends JPanel {

  private TileGraphic[][] gameGrid;

  private Random globalRandom = new Random();
  private Map<String, PlayerAnimator> playerAnimators = new HashMap<>();
  private Map<Position<Integer>, BombAnimator> bombAnimators = new HashMap<>();
  private Map<Position<Integer>, BlastAnimator> blastAnimators = new HashMap<>();
  private Map<Position<Integer>, PowerUpAnimator> powerUpAnimators = new HashMap<>();

  private List<BufferedImage> floorTiles = new ArrayList<>();
  private BufferedImage permanentWall;
  private BufferedImage temporaryWall;

  public GameGridClient() throws IOException {
    this.permanentWall = FileUtils.readImageFromResource(SpritePath.PERMANENT_WALL);
    this.temporaryWall = FileUtils.readImageFromResource(SpritePath.TEMPORARY_WALL);
    BufferedImage floorSheet = FileUtils.readImageFromResource(SpritePath.FLOOR);
    int cols = 5;
    for (int i = 0; i <= 4; i++) {
      floorTiles.add(
          floorSheet.getSubimage(
              i * floorSheet.getWidth() / cols,
              0,
              floorSheet.getWidth() / cols,
              floorSheet.getHeight()));
    }
  }

  public void updateGrid(TileGraphic[][] gameGrid) {
    this.gameGrid = gameGrid;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (gameGrid == null) return;
    Graphics2D g2d = (Graphics2D) g;

    for (int x = 0; x < GameConstants.GRID_SIZE; x++) {
      for (int y = 0; y < GameConstants.GRID_SIZE; y++) {
        Position<Integer> position = new Position<>(x, y);
        // always draw the floor at the bottom layer
        drawSquare(
            floorTiles.get(Math.abs(((x - 2 * y) * (2 * x * y)) % floorTiles.size())), x, y, g2d);

        switch (gameGrid[x][y].getEntityType()) {
          case PERMANENT_WALL:
            drawSquare(permanentWall, x, y, g2d);
            break;
          case TEMPORARY_WALL:
            drawSquare(temporaryWall, x, y, g2d);
            break;
        }

        if (powerUpAnimators.containsKey(position)
            && !(gameGrid[x][y]
                     .getEventType()
                     .equals(powerUpAnimators.get(position).getPowerUpType()))) {
          powerUpAnimators.remove(position);
        }

        switch (gameGrid[x][y].getEventType()) {
          case BOMB:
            bombAnimators.entrySet().removeIf(entry -> entry.getValue().getTimesAnimated() > 0);
            if (!bombAnimators.containsKey(position)) {
              bombAnimators.put(position, new BombAnimator());
            }
            bombAnimators.get(position).drawAnimation(g2d, x, y);
            break;
          case BLAST:
            if (!blastAnimators.containsKey(position)) {
              blastAnimators.put(position, new BlastAnimator(globalRandom));
            }
            blastAnimators.get(position).drawAnimation(g2d, x, y);
            break;
          case POWERUP_MEGA_BLAST_RANGE:
            addPowerupAnimator(position, POWERUP_MEGA_BLAST_RANGE, g2d);
            break;
          case POWERUP_HEALTH_UP:
            addPowerupAnimator(position, POWERUP_HEALTH_UP, g2d);
            break;
          case POWERUP_SPEED_BOOST:
            addPowerupAnimator(position, POWERUP_SPEED_BOOST, g2d);
            break;
          case POWERUP_NO_BOMB_DROP_COOLDOWN:
            addPowerupAnimator(position, POWERUP_NO_BOMB_DROP_COOLDOWN, g2d);
            break;
        }
      }
    }

    for (int a = 0; a < GameConstants.GRID_SIZE; a++) {
      for (int b = 0; b < GameConstants.GRID_SIZE; b++) {
        if (gameGrid[a][b].hasPlayer()) {
          String playerName = gameGrid[a][b].getPlayerName();
          if (!playerAnimators.containsKey(playerName)) {
            playerAnimators.put(playerName, new PlayerAnimator(gameGrid[a][b].getPlayerSprite()));
          }
          PlayerAnimator playerAnimator = playerAnimators.get(playerName);
          playerAnimator.setDirection(gameGrid[a][b].getPlayerDirection());
          playerAnimator.drawAnimation(g2d, gameGrid[a][b].getRelativePosition(), a, b);
        }
      }
    }

    // draw borders around map
    g2d.setColor(Color.black);
    g2d.draw3DRect(
        0,
        0,
        GameConstants.GRID_SIZE * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        GameConstants.GRID_SIZE * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        false);
    g2d.dispose();
  }

  private void drawSquare(BufferedImage image, int x, int y, Graphics2D g2d) {
    g2d.drawImage(
        image,
        x * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        y * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        WindowConstants.WINDOW_SIZE_MULTIPLIER,
        WindowConstants.WINDOW_SIZE_MULTIPLIER,
        null);
  }

  private void addPowerupAnimator(Position<Integer> pos, TileType tileType, Graphics2D g2d) {
    if (!powerUpAnimators.containsKey(pos)) {
      powerUpAnimators.put(pos, new PowerUpAnimator(tileType));
    }
    powerUpAnimators.get(pos).drawAnimation(g2d, pos.getX(), pos.getY());
  }
}
