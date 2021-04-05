package client;

import utility.FileUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlastAnimator {
  private Random r;
  private int speed = 6;
  private int spriteIndex = 0;
  private int counter = 0;
  private BufferedImage currentImg;
  private List<BufferedImage> sprites = new ArrayList<>();

  public BlastAnimator(Random r) {
    this.r = r;
    BufferedImage spriteSheet;
    try {
      spriteSheet = FileUtils.readImageFromResource(SpritePath.BLAST);
    }
    catch (IOException e) {
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
      int nextSpriteIndex = r.nextInt(sprites.size());
      while (this.spriteIndex == nextSpriteIndex) {
        nextSpriteIndex = r.nextInt(sprites.size());
      }
      this.spriteIndex = nextSpriteIndex;
    }
  }

  public void drawAnimation(Graphics2D g, int x, int y) {
    animate();
    g.drawImage(currentImg, x * WindowConstants.WINDOW_SIZE_MULTIPLIER, y * WindowConstants.WINDOW_SIZE_MULTIPLIER,
        WindowConstants.WINDOW_SIZE_MULTIPLIER, WindowConstants.WINDOW_SIZE_MULTIPLIER, null);
  }


}
