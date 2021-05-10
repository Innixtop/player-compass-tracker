package me.snoh666.manhunt;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CompassTracker extends BukkitRunnable {
    private final JavaPlugin plugin;
    private int counter = 0;
    private boolean hasSentMessage = false;

    public CompassTracker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (counter > 10) {
            this.cancel();
            return;
        }
        if (!this.hasSentMessage) {
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                player.sendMessage("One compass tick, track. Player: " + player.getName());
                UUID closestPlayerUid = this.getClosestPlayer(player.getLocation(), player.getUniqueId());
                Location closestPlayerLoc = getPlayerFromUid(closestPlayerUid).getLocation();
                player.setCompassTarget(closestPlayerLoc);
            }
            this.hasSentMessage = true;
        }
        this.counter++;
    }

    private UUID getClosestPlayer(Location loc, UUID exceptPlayerId) {
        UUID closestPlayer = null;
        double distanceToClosestPlayer = 0.0D;
        double xLoc = loc.getX();
        double yLoc = loc.getY();
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (player.getUniqueId() != exceptPlayerId) {
                double p2xLoc = player.getLocation().getX();
                double p2yLoc = player.getLocation().getY();
                double distance = Math.sqrt((p2yLoc - yLoc) * (p2yLoc - yLoc) + (p2xLoc- xLoc) * (p2xLoc- xLoc));
                if (closestPlayer == null) {
                    distanceToClosestPlayer = distance;
                    closestPlayer = player.getUniqueId();
                } else {
                    if (distance < distanceToClosestPlayer) {
                        distanceToClosestPlayer = distance;
                    }
                }
            }
        }

        return closestPlayer;
    }

    private Player getPlayerFromUid(UUID uid) {
        return plugin.getServer().getPlayer(uid);
    }
}