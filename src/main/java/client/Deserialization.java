package client;

import communication.ClientStatus;
import game.GameConstants;
import game.TileType;
import game.entity.Player;
import game.utility.Move;
import game.utility.Position;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static communication.SerializationConstants.*;

public class Deserialization {

  public static Map<String, ClientStatus> deserializePlayers(String serializedData) {
    JSONObject serverRequest = new JSONObject(serializedData);
    JSONArray players = serverRequest.getJSONArray(PLAYER_ARRAY);
    Map<String, ClientStatus> playerStatuses = new HashMap<>();
    for (int i = 0; i < players.length(); i++) {
      JSONObject player = players.getJSONObject(i);
      String playerName = player.getString(PLAYER_NAME);
      ClientStatus clientStatus = Enum.valueOf(ClientStatus.class, player.getString(CLIENT_STATUS));
      playerStatuses.put(playerName, clientStatus);
    }
    return playerStatuses;
  }

  public static TileGraphic[][] deserializeGrid(String serializedData) {
    JSONObject serverRequest = new JSONObject(serializedData);
    JSONArray serverGrid = serverRequest.getJSONArray(GRID_ARRAY);

    TileGraphic[][] tiles = new TileGraphic[GameConstants.GRID_SIZE][GameConstants.GRID_SIZE];
    for (int i = 0; i < serverGrid.length(); i++) {
      JSONObject row = (JSONObject) serverGrid.get(i);
      int x = row.getInt(TILE_X);
      int y = row.getInt(TILE_Y);

      TileType entityType = TileType.NONE;
      TileType eventType = TileType.NONE;
      if (row.has(ENTITY_TYPE)) {
        entityType = Enum.valueOf(TileType.class, row.getString(ENTITY_TYPE));
      }
      if (row.has(EVENT_TYPE)) {
        eventType = Enum.valueOf(TileType.class, row.getString(EVENT_TYPE));
      }
      if (row.has(PLAYER)) {
        JSONObject playerObj = row.getJSONObject(PLAYER);
        String playerName = playerObj.getString(PLAYER_NAME);
        Color playerColor = Color.decode(playerObj.getString(PLAYER_COLOR));
        Move direction = Enum.valueOf(Move.class, playerObj.getString(PLAYER_DIRECTION));
        Position<Double> relativePosition =
            new Position<>(
                playerObj.getDouble(PLAYER_RELATIVE_X), playerObj.getDouble(PLAYER_RELATIVE_Y));
        tiles[x][y] =
            new TileGraphic(
                entityType, eventType, playerName, direction, relativePosition, playerColor);
      } else {
        tiles[x][y] = new TileGraphic(entityType, eventType);
      }
    }
    return tiles;
  }

  public static Map<Player, Boolean> deserializeLobby(String serializedData) {
    JSONObject serverRequest = new JSONObject(serializedData);
    JSONArray players = serverRequest.getJSONArray(PLAYER_ARRAY);
    Map<Player, Boolean> playerStatuses = new HashMap<>();
    for (int i = 0; i < players.length(); i++) {
      JSONObject playerStatus = (JSONObject) players.get(i);
      if (playerStatus.getString(PLAYER_NAME).isEmpty()) continue;
      if (!playerStatus.has(PLAYER_COLOR)) continue;
      Player player =
          new Player(
              playerStatus.getString(PLAYER_NAME), new Color(playerStatus.getInt(PLAYER_COLOR)));
      playerStatuses.put(player, playerStatus.getBoolean(PLAYER_READY_STATUS));
    }
    return playerStatuses;
  }
}
