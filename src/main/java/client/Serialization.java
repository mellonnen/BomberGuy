package client;

import org.json.JSONObject;

import static communication.SerializationConstants.*;

public class Serialization {

  public static String serializeAction(Action action) {
    JSONObject json = new JSONObject();
    json.put(CLIENT_MOVE, action.getMove());
    json.put(CLIENT_BOMB_DROP, action.isBombDropped());

    return json.toString();
  }

  public static String serializeReadyPlayer(String playerName) {
    JSONObject json = new JSONObject();
    json.put(CLIENT_NAME, playerName);

    return json.toString();
  }
}