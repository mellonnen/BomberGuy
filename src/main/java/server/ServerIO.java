package server;

import client.Action;
import communication.ClientData;
import communication.NetworkConstants;
import communication.PacketType;
import game.entity.Player;

import java.io.IOException;
import java.net.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class ServerIO {

  private String ipAdress;
  private GameGridServer gameGridServer;
  private List<ClientData> clients;
  private DatagramSocket sendSocket;
  private DatagramSocket receiveSocket;

  private boolean inLobby;
  private boolean inGame;

  public ServerIO(GameGridServer gameGridServer) throws IOException {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress("google.com", 80));
      this.ipAdress = socket.getLocalAddress().getHostAddress();
    }
    System.out.println("Server IP: " + ipAdress);
    this.gameGridServer = gameGridServer;
    this.clients = new ArrayList<>();
    this.inLobby = true;
    this.inGame = false;

    try {
      this.sendSocket =
          new DatagramSocket(NetworkConstants.SERVER_SEND_PORT, InetAddress.getByName(ipAdress));
      this.receiveSocket =
          new DatagramSocket(NetworkConstants.SERVER_RECEIVE_PORT, InetAddress.getByName(ipAdress));
    } catch (SocketException e) {
      e.printStackTrace();
    }
    new UpdateHandler().start();
    new ReceiveHandler().start();

    new Timer()
        .scheduleAtFixedRate(
            new TimerTask() {
              @Override
              public void run() {
                System.out.println(
                    "SERVER > "
                        + (inLobby ? "in lobby" : (inGame ? "in game" : ""))
                        + " - "
                        + new Timestamp(new Date().getTime()));
              }
            },
            0,
            NetworkConstants.SERVER_STATUS_INTERVAL);
  }

  private class UpdateHandler extends Thread {

    public void run() {
      while (true) {

        String commonPayload = "";

        if (inLobby) {
          if (clients.size() > 1 && clients.stream().allMatch(ClientData::isReady)) {
            inLobby = false;
            inGame = true;
          }
          commonPayload =
              PacketType.LOBBY
                  + Serialization.serializeLobby(clients, gameGridServer.getPlayerColors());
        } else if (inGame) {
          gameGridServer.updateGrid();
          // Add dead players
          List<String> deadPlayerNames =
              gameGridServer.getDeadPlayers().stream()
                  .map(Player::getName)
                  .collect(Collectors.toList());
          clients.stream()
              .filter(clientData -> deadPlayerNames.contains(clientData.getName()))
              .forEach(ClientData::setDead);
          // Add possible winner
          if (gameGridServer.getDeadPlayers().size() >= clients.size() - 1) {
            clients.stream()
                .filter(clientData -> !clientData.isDead())
                .findFirst()
                .ifPresent(ClientData::setWon);
          }
          commonPayload =
              PacketType.UPDATE
                  + Serialization.serializeUpdate(gameGridServer.getGameGrid(), clients);
        }

        try {
          // send common data to all clients
          byte[] commonData = commonPayload.getBytes();
          for (ClientData client : clients) {
            DatagramPacket sendPacket =
                new DatagramPacket(
                    commonData,
                    commonData.length,
                    InetAddress.getByName(client.getIpAdress()),
                    client.getPort());
            sendSocket.send(sendPacket);
          }
          Thread.sleep(NetworkConstants.SERVER_UPDATE_INTERVAL);
        } catch (InterruptedException | IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private class ReceiveHandler extends Thread {

    public void run() {
      while (true) {
        byte[] data = new byte[NetworkConstants.MAX_BYTES];
        DatagramPacket receivedPacket = new DatagramPacket(data, data.length);
        try {
          receiveSocket.receive(receivedPacket);
          parsePacket(receivedPacket);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    private void parsePacket(DatagramPacket receivedPacket) {
      String rawMessage = new String(receivedPacket.getData()).trim();
      System.out.println(
          receivedPacket.getAddress().getHostAddress()
              + " ["
              + receivedPacket.getPort()
              + "]"
              + " sent: "
              + rawMessage
              + " - "
              + new Timestamp(new Date().getTime()));
      char type = rawMessage.charAt(0);
      String message = rawMessage.substring(1);

      switch (type) {
        case PacketType.CONNECT:
          clients.add(
              new ClientData(
                  receivedPacket.getAddress().getHostAddress(), receivedPacket.getPort()));
          break;
        case PacketType.READY:
          String playerName = Deserialization.deserializeName(message);
          getClient(receivedPacket).setReady();
          getClient(receivedPacket).setName(playerName);
          gameGridServer.createPlayer(playerName);
          break;
        case PacketType.ACTION:
          Action playerAction = Deserialization.deserializeAction(message);
          gameGridServer.executePlayerAction(getPlayerName(receivedPacket), playerAction);
          break;
      }
    }

    public ClientData getClient(DatagramPacket packet) {
      return clients.stream()
          .filter(clientData -> clientData.getPort() == packet.getPort())
          .findFirst()
          .get();
    }

    public String getPlayerName(DatagramPacket packet) {
      return getClient(packet).getName();
    }
  }
}
