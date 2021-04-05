package client;

import game.TileType;
import utility.FileUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PowerUpAnimator {
  private int speed = 5;
  private int spriteIndex = 0;
  private int counter = 0;
  private TileType powerUpType;
  private BufferedImage currentImg;
  private List<BufferedImage> sprites = new ArrayList<>();

  public PowerUpAnimator(TileType powerUpType) {
    this.powerUpType = powerUpType;
    String filePath;
    switch (this.powerUpType) {
      case POWERUP_MEGA_BLAST_RANGE:
        filePath = SpritePath.POWERUP_MEGA_BLAST_RANGE;
        break;
      case POWERUP_HEALTH_UP:
        filePath = SpritePath.POWERUP_HEALTH_UP;
        break;
      case POWERUP_NO_BOMB_DROP_COOLDOWN:
        filePath = SpritePath.POWERUP_NO_COOLDOWN;
        break;
      case POWERUP_SPEED_BOOST:
        filePath = SpritePath.POWERUP_SPEED_BOOST;
        break;
      default:
        return;
    }
    BufferedImage spriteSheet;
    try {
      spriteSheet = FileUtils.readImageFromResource(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    int rows = 6;
    int size = spriteSheet.getWidth() / 6;
    for (int i = 0; i < rows; i++) {
      sprites.add(spriteSheet.getSubimage(i * size, 0, size, size));
    }
  }

  public void animate() {
    this.counter++;
    if (this.counter >= this.speed) {
      this.counter = 0;
      nextFrame();
    }
  }

  private void nextFrame() {
    for (int i = 0; i < sprites.size(); i++) {
      if (spriteIndex == i) {
        this.currentImg = this.sprites.get(i);
      }
    }
    this.spriteIndex++;
    if (this.spriteIndex > this.sprites.size()) {
      this.spriteIndex = 0;
    }
  }

  public void drawAnimation(Graphics2D g, int x, int y) {
    animate();
    g.drawImage(
        currentImg,
        x * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        y * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        WindowConstants.WINDOW_SIZE_MULTIPLIER,
        WindowConstants.WINDOW_SIZE_MULTIPLIER,
        null);
  }

  public TileType getPowerUpType() {
    return powerUpType;
  }
}
