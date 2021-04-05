package client;

import communication.ClientStatus;
import utility.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PlayerStatusPanel extends JPanel {

  private BufferedImage image;
  private int SET_WIDTH = 400;
  private int SET_HEIGHT = 105;

  public PlayerStatusPanel(ClientStatus status) {
    // set transparent background
    this.setBackground(new Color(0, 0, 0, 0));
    try {
      switch (status) {
        case DEAD:
          image = FileUtils.readImageFromResource(SpritePath.DEAD_TEXT);
          break;
        case WON:
          image = FileUtils.readImageFromResource(SpritePath.WIN_TEXT);
          break;
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, SET_WIDTH, SET_HEIGHT, null);
  }

  public int getWidth() {
    return SET_WIDTH;
  }

  public int getHeight() {
    return SET_HEIGHT;
  }

}
