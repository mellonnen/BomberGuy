package game.utility;

import game.entity.Player;
import game.powerup.HealthUp;
import game.powerup.MegaBlastRange;
import game.powerup.NoBombDropCooldown;
import game.powerup.PowerUp;

public class PowerUpApplicator {

  public static void applyPowerUp(Player player, PowerUp powerUp) {
    // apply health-up directly, do not make the Player carry it
    if (!(powerUp instanceof HealthUp)) {
      player.resetPowerUp();
      player.setPowerUp(powerUp);
    }
    if (powerUp instanceof MegaBlastRange) {
      MegaBlastRange megaBlastRange = (MegaBlastRange) powerUp;
      player.setRangeMultiplier(megaBlastRange.getBlastBoost());
    } else if (powerUp instanceof HealthUp) {
      HealthUp healthUp = (HealthUp) powerUp;
      player.adjustHitPoints(healthUp.getHealthBoost());
    } else if (powerUp instanceof NoBombDropCooldown) {
      player.resetBombDropCooldown();
    }
  }
}