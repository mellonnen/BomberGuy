package client;

import game.utility.Move;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClientKeyListener implements KeyListener {

  private ClientCommander clientCommander;

  public ClientKeyListener(ClientCommander clientCommander) {
    this.clientCommander = clientCommander;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_UP:
        clientCommander.requestMove(Move.UP);
        break;
      case KeyEvent.VK_DOWN:
        clientCommander.requestMove(Move.DOWN);
        break;
      case KeyEvent.VK_RIGHT:
        clientCommander.requestMove(Move.RIGHT);
        break;
      case KeyEvent.VK_LEFT:
        clientCommander.requestMove(Move.LEFT);
        break;
      case KeyEvent.VK_SPACE:
        clientCommander.requestBombDrop(true);
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

}