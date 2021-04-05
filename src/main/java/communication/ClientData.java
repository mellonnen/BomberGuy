package communication;

public class ClientData {

  private String name;
  private String ipAdress;
  private int port;
  private boolean ready;
  private boolean dead;
  private boolean won;

  public ClientData(String name, String ipAdress, int port) {
    this.name = name;
    this.ipAdress = ipAdress;
    this.port = port;
    this.ready = false;
    this.dead = false;
    this.won = false;
  }

  public ClientData(String ipAdress, int port) {
    this.name = "";
    this.ipAdress = ipAdress;
    this.port = port;
    this.ready = false;
    this.dead = false;
    this.won = false;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIpAdress() {
    return ipAdress;
  }

  public boolean isReady() {
    return ready;
  }

  public void setReady() {
    this.ready = true;
  }

  public int getPort() {
    return port;
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead() {
    this.dead = true;
  }

  public boolean hasWon() {
    return won;
  }

  public void setWon() {
    this.won = true;
  }
}
