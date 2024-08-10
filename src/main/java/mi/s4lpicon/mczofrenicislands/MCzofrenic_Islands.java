package mi.s4lpicon.mczofrenicislands;

import mi.s4lpicon.mczofrenicislands.commandsManager.CommandsManager;
import mi.s4lpicon.mczofrenicislands.commandsManager.CommandsTabCompleter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MCzofrenic_Islands extends JavaPlugin {

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        // plugin startup logic
        Objects.requireNonNull(this.getCommand("islands")).setExecutor(new CommandsManager());
        // Register the TabCompleter
        Objects.requireNonNull(getCommand("islands")).setTabCompleter(new CommandsTabCompleter());
        // Log events
        PluginManager pm = getServer().getPluginManager();
        //pm.registerEvents(new Manager(), this);
        getLogger().info(ChatColor.YELLOW + "The plugin has been successfully enabled");

    }

    @Override
    public void onDisable() {
        // Logica cuando el plugin es deshabilitado
        getLogger().info("The plugin has been successfully disabled");
    }
}
