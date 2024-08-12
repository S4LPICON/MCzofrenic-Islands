package mi.s4lpicon.mczofrenicislands.islandsManager;

import mi.s4lpicon.mczofrenicislands.fileManagement.JsonUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;

/*
NO BORRAR ESTA INFORMATION HASTA NO SER IMPLEMENTADA
modo para descargar un mundo

World world = Bukkit.getWorld("nombre_del_mundo");
if (world != null) {
    Bukkit.unloadWorld(world, false); // false para no guardar los cambios en el mundo antes de descargarlo
}


modo para saber cuantos jugadores hay en un mundo
World world = Bukkit.getWorld("nombre_del_mundo");
if (world != null) {
    int playerCount = world.getPlayers().size();
    // Aquí puedes usar playerCount como desees
}
 */

public class IslandsManager implements Listener {

    public static ArrayList<PlayerIsland> activeIslands = new ArrayList<>();

    public static void getDevInfo(Player player) {
        StringBuilder active = new StringBuilder();
        for (PlayerIsland island : activeIslands) {
            active.append(island.getOwnerName()).append(", ");
        }

        // Elimina la última coma y espacio si hay islas activas
        if (active.length() > 0) {
            active.setLength(active.length() - 2);
        }

        player.sendMessage("Active Islands: " + active);
    }

    @SuppressWarnings("deprecation")
    public static void buildIsland(Player player){
        File carpeta = new File("PlayerIslands/"+player.getName());
        if(carpeta.exists() && carpeta.isDirectory()){
            player.sendMessage(ChatColor.RED+"Ya tienes un mundo creado no se puede crear otro!");
            return;
        }
        PlayerIsland island = new PlayerIsland(player.getName(), 1, "Esta Isla es la polla", "Gigante");
        activeIslands.add(island);
        // Guardar el objeto en un archivo JSON
        sendPlayerToWorld(player, player.getName());
        JsonUtils.guardarJugadorIslaEnJson(island);
        player.sendMessage("You have created an island!");
    }

    public static PlayerIsland vefAndLoadIsland(Player owner){
        if (Bukkit.getWorld("PlayerIslands/" + owner.getName()) == null){
            return null;
        }
        int pos = findIsland(owner);
        if (pos == -1){
            loadIsland(owner, owner.getName());
            return activeIslands.get(findIsland(owner));
        }else {
            return activeIslands.get(pos);
        }
    }

    public static void banPlayerOfIsland(Player owner, String bannedPlayer){
        PlayerIsland island = vefAndLoadIsland(owner);
        if (island != null) {
            island.banPlayer(bannedPlayer);
        }else {
            owner.sendMessage("you don't have an active island!");
        }
    }

    public static void unBanPlayerOfIsland(Player owner, String unBannedPlayer){
        PlayerIsland island = vefAndLoadIsland(owner);
        if (island != null) {
            island.unBanPlayer(unBannedPlayer);
        }else {
            owner.sendMessage("you don't have an active island!");
        }
    }

    @SuppressWarnings("deprecation")
    public static void sendPlayerToWorld(Player player, String worldName) {
        Player playerIslandOwner = Bukkit.getPlayer(worldName);
        int islandPos = findIsland(playerIslandOwner);

        if( islandPos == -1 && !(player.getName().equals(worldName))){
            player.sendMessage("¡Error, the island is disabled");
            return;
        }

        if (islandPos != -1) {
            if (activeIslands.get(islandPos).findBannedPlayer(player.getName()) != -1) {
                player.sendMessage("¡Error, you are banned from this island!");
                return;
            }
        }

        World world;
        // Get the world by name
        if (worldName.equals(player.getName())) {
            world = Bukkit.getWorld("PlayerIslands/" + worldName);
        } else {
            world = Bukkit.getWorld(worldName);
        }

        double x =0, z =0, y=0;

        // If the world is not loaded, try to load it
        if (world == null) {
            world = loadIsland(player, worldName);
            player.sendMessage(ChatColor.RED + "The world was unloaded!"
                    + ChatColor.GREEN + "\nLoading World...");
            if (world == null) {
                player.sendMessage("The world could not be loaded " + worldName + ".");
                return;
            }
        }
        PlayerIsland island = JsonUtils.leerJugadorIslaDesdeJson("plugins/MCzofrenic-Islands/"+player.getName()+"_island.json");
        if(island != null && islandPos != -1) {
            activeIslands.set(islandPos, island); //cargar el archivo por si existen cambios
            x = activeIslands.get(islandPos).getSpawnX();
            y = activeIslands.get(islandPos).getSpawnY();
            z = activeIslands.get(islandPos).getSpawnZ();
        }

        // Set the location in the world (0, 0, 0)
        Location location = new Location(world, x, y, z);

        // Tp the player to the specified location
        player.teleport(location);
        player.sendMessage("You have teleported to the " + worldName + "'s island!");
    }
    public static void sendPlayerToSpawn(Player player){
        String spawnName = "LaCapital";
        double x = 10,y= -59,z=23;
        // Set the location in the world (0, 0, 0)
        World world = Bukkit.getWorld(spawnName);
        Location location = new Location(world, x, y, z);
        location.setYaw(-180);
        location.setPitch(0);

        // Tp the player to the specified location
        player.teleport(location);
    }

    public static World loadIsland(Player player, String worldName) {
        PlayerIsland island = JsonUtils.leerJugadorIslaDesdeJson("plugins/MCzofrenic-Islands/"+player.getName()+"_island.json");
        activeIslands.add(island);
        player.sendMessage("Active islands before load world: "+activeIslands);
        // Check if the world exists before loading it
        World world = Bukkit.getWorld("PlayerIslands/" + worldName);
        if (world == null) {
            // Check if the world directory exists
            if (new File(Bukkit.getWorldContainer(), "PlayerIslands/" + worldName).exists()) {

                world = Bukkit.createWorld(new WorldCreator("PlayerIslands/" + worldName));

                player.sendMessage("The world " + worldName + " has been loaded!");
            } else {
                player.sendMessage("The world " + worldName + " does not exist.");
            }
        } else {
            player.sendMessage("The world " + worldName + " is already loaded.");
        }
        return world;
    }

    public static int findIsland(Player player){
        for (PlayerIsland island : activeIslands){
            if (island != null && (island.getOwnerName().equals(player.getName()))) {
                return activeIslands.indexOf(island);
            }
        }
        return -1;
    }

    public static void setIslandSpawn(Player player){
        if (!(player.getWorld().getName().equals("PlayerIslands/"+player.getName()))){
            player.sendMessage("Debes estar en tu propia isla para utilizar este comando!");
            return;
        }

        int posIsland = findIsland(player);
        if (posIsland == -1){
            PlayerIsland island = JsonUtils.leerJugadorIslaDesdeJson("plugins/MCzofrenic-Islands/"+player.getName()+"_island.json");
            activeIslands.add(island);
            player.sendMessage("No fue posible encontrar tu isla");
            return;
        }
        PlayerIsland isla = activeIslands.get(posIsland);


        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        isla.setSpawnX(x);
        isla.setSpawnY(y);
        isla.setSpawnZ(z);
        JsonUtils.guardarJugadorIslaEnJson(isla);
        player.sendMessage("Has designado el spawn con éxito!");
    }
}
