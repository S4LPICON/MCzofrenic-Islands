package mi.s4lpicon.mczofrenicislands.commandsManager;

import mi.s4lpicon.mczofrenicislands.invitationManager.InvitationManager;
import mi.s4lpicon.mczofrenicislands.islandsManager.IslandsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandsManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("island")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length == 0) {
                    player.sendMessage("Correct use: /island <args>");
                    return false;
                }

                //THE SUBCOMMANDS
                if (args[0].equalsIgnoreCase("create")) {

                    IslandsManager.buildIsland(player);

                    return true;

                } else if (args[0].equalsIgnoreCase("tp")) {

                    if (args.length >= 2) {
                        String targetName = args[1].equals(player.getName()) ? player.getName() : args[1];
                        IslandsManager.sendPlayerToWorld(player,targetName);
                    } else {
                        //He sends him to his own island
                        IslandsManager.sendPlayerToWorld(player, player.getName());
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("setspawn")) {
                    IslandsManager.setIslandSpawn(player);
                    return true;

                } else if (args[0].equalsIgnoreCase("ban")) {
                    if (args.length >= 2) {
                        IslandsManager.banPlayerOfIsland(player, args[1]);

                    } else {
                        player.sendMessage("Write the name of a player to ban him!");
                    }
                    return true;

                }else if (args[0].equalsIgnoreCase("unban")) {
                    if (args.length >= 2) {
                        IslandsManager.unBanPlayerOfIsland(player, args[1]);
                    } else {
                        player.sendMessage("Write the name of a player to unban him!");
                    }
                    return true;

                }else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length >= 2) {
                        IslandsManager.removePlayerOfIsland(args[1], player.getName());
                    } else {
                        player.sendMessage("Write the name of a player to remove him of your island");
                    }
                    return true;

                }else if (args[0].equalsIgnoreCase("invite")) {
                    if (args.length >= 2) {
                        if (args.length >= 3) {
                            //IslandsManager.invitePlayerToIsland(args[1], player.getName(), Integer.parseInt(args[2]));
                            InvitationManager.sendInvitation(player, args[1],Integer.parseInt(args[2]));
                        }else {
                            player.sendMessage("Escribe el nivel de permisos para el jugador");
                        }
                    } else {
                        player.sendMessage("Write the name of a player to invite him of your island");
                    }
                    return true;

                }else if (args[0].equalsIgnoreCase("join")) {
                    if (args.length >= 2) {
                            InvitationManager.acceptInvitation(args[1],player);
                    } else {
                        player.sendMessage("Escribe el nombre de la persona que te invito para aceptarle la invitacion!");
                    }
                    return true;

                }else if (args[0].equalsIgnoreCase("leave")) {
                    if (args.length >= 2) {
                        IslandsManager.leaveIsland(player, args[1]); //acceptInvitation(args[1],player)
                    } else {
                        player.sendMessage("Escribe el nombre del dueño de la isla!");
                    }
                    return true;

                }else if (args[0].equalsIgnoreCase("member")) {
                    if (args.length >= 2) {
                        IslandsManager.leaveIsland(player, args[1]); //acceptInvitation(args[1],player)
                    } else {
                        player.sendMessage("Escribe el nombre del dueño de la isla!");
                    }
                    return true;

                }

                else {
                    player.sendMessage("Correct use: /island <command>");
                    return false;
                }
            } else {
                sender.sendMessage("This command can only be executed by one player.");
                return false;
            }
        }
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                IslandsManager.sendPlayerToSpawn(player);
                player.sendMessage("You have teleported to spawn!");
            }
        }

        if (command.getName().equalsIgnoreCase("devinfo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length >= 2 ) {
                    IslandsManager.getDevInfo(player, Integer.parseInt(args[1]));
                } else {
                    IslandsManager.getDevInfo(player);
                }
                return true;
            }
        }
        return false;
    }
}
