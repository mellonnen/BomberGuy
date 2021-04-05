package server;

import communication.ClientData;
import communication.ClientStatus;
import game.GameConstants;
import game.Tile;
import game.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static communication.SerializationConstants.*;

public class Serialization {

  public static String serializeUpdate(Tile[][] gameGrid, List<ClientData> clients) {
    JSONObject update = new JSONObject();
    JSONArray tiles = new JSONArray();
    for (int x = 0; x < GameConstants.GRID_SIZE; x++) {
      for (int y = 0; y < GameConstants.GRID_SIZE; y++) {
        JSONObject tile = new JSONObject();
        tile.put(TILE_X, x);
        tile.put(TILE_Y, y);
        if (gameGrid[x][y].hasEntity()) {
          tile.put(ENTITY_TYPE, gameGrid[x][y].getEntityType().toString());
        }
        if (gameGrid[x][y].hasEvent()) {
          tile.put(EVENT_TYPE, gameGrid[x][y].getEventType().toString());
        }
        if (gameGrid[x][y].hasPlayer()) {
          JSONObject playerObj = new JSONObject();
          Player player = (Player) gameGrid[x][y].getEntity();
          playerObj.put(PLAYER_NAME, player.getName());
          playerObj.put(
              PLAYER_COLOR,
              String.format(
                  "#%02x%02x%02x",
                  player.getColor().getRed(),
                  player.getColor().getGreen(),
                  player.getColor().getBlue()));
          playerObj.put(PLAYER_DIRECTION, player.getDirection().toString());
          playerObj.put(PLAYER_RELATIVE_X, player.getRelativePosition().getX());
          playerObj.put(PLAYER_RELATIVE_Y, player.getRelativePosition().getY());
          tile.put(PLAYER, playerObj);
        }
        tiles.put(tile);
      }
    }

    JSONArray players = new JSONArray();
    for (ClientData clientData : clients) {
      JSONObject playerData = new JSONObject();
      ClientStatus status = ClientStatus.ALIVE;
      if (clientData.isDead()) status = ClientStatus.DEAD;
      if (clientData.hasWon()) status = ClientStatus.WON;
      playerData.put(CLIENT_NAME, clientData.getName());
      playerData.put(CLIENT_STATUS, status.toString());
      players.put(playerData);
    }

    update.put(PLAYER_ARRAY, players);
    update.put(GRID_ARRAY, tiles);
    return update.toString();
  }

  public static String serializeLobby(List<ClientData> clients, Map<String, Color> playerColors) {
    JSONObject json = new JSONObject();
    JSONArray players = new JSONArray();
    clients.forEach(
        client -> {
          JSONObject player = new JSONObject();
          player.put(PLAYER_NAME, client.getName());
          if (playerColors.containsKey(client.getName())) {
            player.put(PLAYER_COLOR, playerColors.get(client.getName()).getRGB());
          }
          player.put(PLAYER_READY_STATUS, client.isReady());
          players.put(player);
        });
    json.put(PLAYER_ARRAY, players);
    return json.toString();
  }
}
