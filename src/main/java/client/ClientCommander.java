package client;

import game.utility.Move;

public interface ClientCommander {

  void requestMove(Move move);

  void requestBombDrop(boolean dropBomb);

}
