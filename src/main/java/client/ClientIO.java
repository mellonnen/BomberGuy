package client;

import communication.ClientStatus;
import communication.NetworkConstants;
import communication.PacketType;
import game.utility.Move;

import java.io.IOException;
import java.net.*;
import java.util.Map;

public class ClientIO implements ClientCommander {

  private ClientWindow clientWindow;
  private ServerHandler serverHandler;
  private GameGridClient gameGridClient;

  private String clientName;
  private Action clientAction = new Action();

  public ClientIO(ClientWindow clientWindow, GameGridClient gameGridClient) {
    this.clientWindow = clientWindow;
    this.gameGridClient = gameGridClient;
  }

  public void startServerConnection(String serverIP) {
    serverHandler = new ServerHandler(serverIP);
    serverHandler.start();
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public void setReady() {
    serverHandler.sendMessage(PacketType.READY + Serialization.serializeReadyPlayer(clientName));
  }

  private class ServerHandler extends Thread {
    private InetAddress ipAdress;
    private DatagramSocket socket;
    private boolean inLobby;

    public ServerHandler(String ipAdress) {
      this.inLobby = true;
      try {
        this.socket = new DatagramSocket();
        this.ipAdress = InetAddress.getByName(ipAdress);
      } catch (SocketException | UnknownHostException e) {
        e.printStackTrace();
      }
      sendMessage(Character.toString(PacketType.CONNECT));
    }

    public void run() {
      while (true) {
        byte[] data = new byte[NetworkConstants.MAX_BYTES];
        DatagramPacket packet =
            new DatagramPacket(data, data.length, ipAdress, NetworkConstants.SERVER_SEND_PORT);
        try {
          socket.receive(packet);
          parsePacket(packet.getData());
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public void parsePacket(byte[] data) throws InterruptedException {
      String receivedMessage = new String(data).trim().substring(1);
      char type = new String(data).trim().charAt(0);

      switch (type) {
        case PacketType.LOBBY:
          clientWindow.updateLobbyPanel(Deserialization.deserializeLobby(receivedMessage));
          break;
        case PacketType.UPDATE:
          if (inLobby) {
            Thread.sleep(WindowConstants.DEFAULT_ATTENTION_DELAY * 3);
            clientWindow.removeStartWindow();
            clientWindow.createGameWindow();
            inLobby = false;
          }
          Map<String, ClientStatus> playerStatuses =
              Deserialization.deserializePlayers(receivedMessage);
          switch (playerStatuses.get(clientName)) {
            case ALIVE:
              break;
            case DEAD:
              clientWindow.showClientStatus(ClientStatus.DEAD);
              break;
            case WON:
              clientWindow.showClientStatus(ClientStatus.WON);
              break;
          }
          gameGridClient.updateGrid(Deserialization.deserializeGrid(receivedMessage));
          break;
      }
    }

    public void sendMessage(String message) {
      DatagramPacket packet =
          new DatagramPacket(
              message.getBytes(), message.length(), ipAdress, NetworkConstants.SERVER_RECEIVE_PORT);
      try {
        socket.send(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void requestMove(Move move) {
    clientAction.setMove(move);
    serverHandler.sendMessage(PacketType.ACTION + Serialization.serializeAction(clientAction));
    clientAction.reset();
  }

  @Override
  public void requestBombDrop(boolean dropBomb) {
    clientAction.setBombDropped(dropBomb);
    serverHandler.sendMessage(PacketType.ACTION + Serialization.serializeAction(clientAction));
    clientAction.reset();
  }
}
