package game.event;

import game.GameConstants;

public class Blast implements Event {

  private int blastTimer = GameConstants.BLAST_TIMER;

  @Override
  public void update() {
    blastTimer--;
  }

  @Override
  public int getTimer() {
    return blastTimer;
  }
}
