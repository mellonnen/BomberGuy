package server;

import client.Action;
import game.utility.Move;
import org.json.JSONObject;

import static communication.SerializationConstants.*;

public class Deserialization {


  public static Action deserializeAction(String serializedData) {
    JSONObject clientRequest = new JSONObject(serializedData);

    Action clientAction = new Action();
    clientAction.setMove(clientRequest.getEnum(Move.class, CLIENT_MOVE));
    clientAction.setBombDropped(clientRequest.getBoolean(CLIENT_BOMB_DROP));
    return clientAction;
  }

  public static String deserializeName(String serializedData) {
    JSONObject clientRequest = new JSONObject(serializedData);
    return clientRequest.getString(CLIENT_NAME);
  }


}