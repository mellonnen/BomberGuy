package client;

import game.entity.Player;
import utility.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SetupPanel extends JPanel {

  private Map<Player, Boolean> playerStatus;

  public SetupPanel() {
    playerStatus = new HashMap<>();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setBackground(new Color(0, 0, 0, 0));

    Map<String, BufferedImage> readyPlayers = new HashMap<>();
    playerStatus.forEach((player, status) -> {
      try {
        BufferedImage playerImage = FileUtils.readImageFromResource(getPlayerSprite(player));
        playerImage = playerImage.getSubimage(0, 0, playerImage.getWidth() / 4, playerImage.getHeight() / 4);
        readyPlayers.put(player.getName(), playerImage);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    });

    int startX = 0;
    for (Entry<String, BufferedImage> entry : readyPlayers.entrySet()) {
      BufferedImage image = entry.getValue();
      g2d.drawImage(image, startX, 15, this.getWidth() / 4, (int) (0.8 * this.getHeight()), null);

      String playerName = entry.getKey();
      g2d.setFont(WindowConstants.PLAYER_NAME_LOBBY_FONT);
      FontMetrics fontMetrics = g2d.getFontMetrics(WindowConstants.PLAYER_NAME_LOBBY_FONT);
      int textX = startX + ((this.getWidth() / 4) - fontMetrics.stringWidth(playerName)) / 2;
      g2d.drawString(playerName, textX, (float) (0.95 * this.getHeight()));

      startX += this.getWidth() / 4;
    }
  }

  private String getPlayerSprite(Player player) {
    if (Color.red.equals(player.getColor())) {
      return SpritePath.RED_GUY;
    } else if (Color.blue.equals(player.getColor())) {
      return SpritePath.BLUE_GUY;
    } else if (Color.green.equals(player.getColor())) {
      return SpritePath.GREEN_GUY;
    } else if (Color.yellow.equals(player.getColor())) {
      return SpritePath.YELLOW_GUY;
    }
    return SpritePath.RED_GUY;
  }

  public void setPlayerStatus(Map<Player, Boolean> playerStatus) {
    this.playerStatus = playerStatus;
    repaint();
  }
}
