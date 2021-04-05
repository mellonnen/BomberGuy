package client;

import utility.FileUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BombAnimator {

  private int speed = 55;
  private int spriteIndex = 0;
  private int counter = 0;
  private int timesAnimated = 0;

  private BufferedImage currentImg;
  private List<BufferedImage> sprites = new ArrayList<>();

  public BombAnimator() {
    BufferedImage spriteSheet;
    try {
      spriteSheet = FileUtils.readImageFromResource(SpritePath.BOMB);
    }
    catch (IOException e) {
      e.printStackTrace();
      return;
    }
    int cols = 3;
    int width = spriteSheet.getWidth() / cols;
    int height = spriteSheet.getHeight();
    for (int i = 0; i < cols; i++) {
      sprites.add(spriteSheet.getSubimage(i * width, 0, width, height));
    }
    currentImg = this.sprites.get(0);
    spriteIndex = 1;
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
      timesAnimated++;
    }
  }

  public int getTimesAnimated() {
    return timesAnimated;
  }

  public void drawAnimation(Graphics2D g, int x, int y) {
    animate();
    g.drawImage(currentImg, x * WindowConstants.WINDOW_SIZE_MULTIPLIER, y * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        WindowConstants.WINDOW_SIZE_MULTIPLIER, WindowConstants.WINDOW_SIZE_MULTIPLIER, null);
  }

}