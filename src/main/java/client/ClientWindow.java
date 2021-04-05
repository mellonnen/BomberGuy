package client;

import communication.ClientStatus;
import game.entity.Player;

import java.util.Map;

public interface ClientWindow {

  void createGameWindow();

  void showClientStatus(ClientStatus status);

  void updateLobbyPanel(Map<Player, Boolean> playerStatus);

  void removeStartWindow();

}
