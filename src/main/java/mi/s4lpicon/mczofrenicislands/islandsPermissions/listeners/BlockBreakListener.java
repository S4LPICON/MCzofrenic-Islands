package mi.s4lpicon.mczofrenicislands.islandsPermissions.listeners;

import mi.s4lpicon.mczofrenicislands.islandsManager.IslandsManager;
import mi.s4lpicon.mczofrenicislands.islandsPermissions.IslandsPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        String playerOwnerName = IslandsPermissions.getStringAfterSlash(player.getWorld().getName());
        Player islandOwner = Bukkit.getPlayer(playerOwnerName);
        assert islandOwner != null;
        IslandsManager.vefAndLoadIsland(islandOwner);
        int pos = IslandsManager.findIsland(islandOwner);
        if (pos == -1){
            player.sendMessage("Mega error detectado reportalo!"); //es imposible que el mundo en el que se encuentra, esté descargado
            return;
        }
        if (IslandsManager.activeIslands.get(pos).hasPermissions(player.getName()) <= 1
                && !(player.getName().equals(playerOwnerName))) {
            event.setCancelled(true); // Cancela el evento, evitando que el jugador rompa bloques
        }
    }
}