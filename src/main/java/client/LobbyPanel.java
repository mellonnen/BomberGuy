package client;

import utility.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LobbyPanel extends JPanel {

  private BufferedImage image;
  private int width;
  private int height;

  public LobbyPanel(int width, int height) {
    this.width = width;
    this.height = height;
    // set transparent background
    this.setBackground(new Color(0, 0, 0, 0));
    try {
      image = FileUtils.readImageFromResource(SpritePath.LOBBY_SERVER);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, width, height, null);
  }

  public void setInLobby() {
    try {
      image = FileUtils.readImageFromResource(SpritePath.LOBBY_NAME);
      repaint();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setPressedReady() {
    try {
      image = FileUtils.readImageFromResource(SpritePath.LOBBY_NAME_PRESSED);
      repaint();
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          try {
            image = FileUtils.readImageFromResource(SpritePath.LOBBY_READY);
          }
          catch (IOException e) {
            e.printStackTrace();
          }
          repaint();
        }
      }, WindowConstants.DEFAULT_ATTENTION_DELAY);
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

}
