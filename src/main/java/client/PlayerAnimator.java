package client;

import game.GameConstants;
import game.utility.Move;
import game.utility.Position;
import utility.FileUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerAnimator {

  private final int speed = 5;
  private int spriteIndex = 0;
  private int counter = 0;

  private BufferedImage currentImg;
  private List<BufferedImage> currentWalk;
  private List<BufferedImage> walkingDown;
  private List<BufferedImage> walkingRight;
  private List<BufferedImage> walkingUp;
  private List<BufferedImage> walkingLeft;

  public PlayerAnimator(String filePath) {

    BufferedImage spriteSheet;
    try {
      spriteSheet = FileUtils.readImageFromResource(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    int subSquares = 4;
    int size = spriteSheet.getWidth() / 4;

    List<BufferedImage> allSprites = new ArrayList<>(subSquares * subSquares);
    for (int i = 0; i < subSquares; i++) {
      for (int j = 0; j < subSquares; j++) {
        allSprites.add(spriteSheet.getSubimage(j * size, i * size, size, size));
      }
    }

    this.walkingDown = allSprites.subList(0, subSquares);
    this.walkingRight = allSprites.subList(subSquares, subSquares * 2);
    this.walkingUp = allSprites.subList(subSquares * 2, subSquares * 3);
    this.walkingLeft = allSprites.subList(subSquares * 3, subSquares * 4);
    this.currentWalk = walkingDown;
  }

  public void animate() {
    this.counter++;
    if (this.counter >= this.speed) {
      this.counter = 0;
      nextFrame();
    }
  }

  private void nextFrame() {
    for (int i = 0; i < currentWalk.size(); i++) {
      if (spriteIndex == i) {
        this.currentImg = this.currentWalk.get(i);
      }
    }
    this.spriteIndex++;
    if (this.spriteIndex > this.currentWalk.size()) {
      this.spriteIndex = 0;
    }
  }

  public void drawAnimation(Graphics2D g, Position<Double> relativePosition, int x, int y) {
    animate();
    int xPixel =
        (int)
            ((relativePosition.getX() + x - GameConstants.TILE_CENTER)
                * WindowConstants.WINDOW_SIZE_MULTIPLIER);
    int yPixel =
        (int)
            ((relativePosition.getY() + y - GameConstants.TILE_CENTER)
                * WindowConstants.WINDOW_SIZE_MULTIPLIER);
    g.drawImage(
        currentImg,
        xPixel,
        yPixel,
        WindowConstants.WINDOW_SIZE_MULTIPLIER,
        WindowConstants.WINDOW_SIZE_MULTIPLIER,
        null);
  }

  public void setDirection(Move direction) {
    switch (direction) {
      case UP:
        if (!currentWalk.equals(walkingUp)) {
          this.currentWalk = walkingUp;
          nextFrame();
        }
        break;
      case DOWN:
        if (!currentWalk.equals(walkingDown)) {
          this.currentWalk = walkingDown;
          nextFrame();
        }
        break;
      case LEFT:
        if (!currentWalk.equals(walkingLeft)) {
          this.currentWalk = walkingLeft;
          nextFrame();
        }
        break;
      case RIGHT:
        if (!currentWalk.equals(walkingRight)) {
          this.currentWalk = walkingRight;
          nextFrame();
        }
        break;
    }
  }
}
