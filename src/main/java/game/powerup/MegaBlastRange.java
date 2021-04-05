package game.powerup;

import java.util.Random;

public class MegaBlastRange extends TimedPowerUp {

  private double blastBoost;

  public MegaBlastRange(Random r) {
    double randRange = r.nextDouble();
    if (randRange <= 0.4) {
      this.blastBoost = 2;
    } else if (randRange <= 0.6) {
      this.blastBoost = 2.35;
    } else if (randRange <= 0.7) {
      this.blastBoost = 3;
    } else if (randRange <= 0.8) {
      this.blastBoost = 3.5;
    } else if (randRange <= 0.9) {
      this.blastBoost = 3.7;
    } else if (randRange <= 1) {
      this.blastBoost = 5;
    }
  }

  public double getBlastBoost() {
    return blastBoost;
  }

}
