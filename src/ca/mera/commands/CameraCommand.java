package ca.mera.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.mera.CameraAPI;
import ca.mera.CameraController;
import ca.mera.util.Executor;
import ca.mera.util.Messages;

public class CameraCommand extends Executor {

    private HashMap<UUID, List<Location>> creators;

    /**
     * MainCommand constructor
     */
    public CameraCommand() {
	super("/camera help", "Main camera argument", "", true);
	this.creators = new HashMap<UUID, List<Location>>();
    }

    /**
     * Get the commands help message
     * 
     * @return String[] object
     */
    private String[] getHelpMessage() {
	return new String[] { "", Messages.prefix + " -> Sub command list: ",
		"", Messages.helpCommand, Messages.planCommand,
		Messages.setPointCommand, Messages.cancelCommand,
		Messages.createCommand, Messages.deleteCommand,
		Messages.undoCommand, Messages.playCommand,
		Messages.interruptCommand, "" };
    }

    @Override
    public boolean handler(CommandSender sender, String label, String[] args) {
	Player player = (Player) sender;
	if (label.equalsIgnoreCase("camera") && (args.length >= 1)) {
	    if (args[0].equalsIgnoreCase("help")) {
		if (player.hasPermission("cameraapi.help")) {
		    // Get the help messages
		    for (String message : this.getHelpMessage()) {
			// For each help message, display it to the sender
			player.sendMessage(message);
		    }
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("list")) {
		if (player.hasPermission("cameraapi.list")) {
		    if (args.length == 2) {
			if (this.isInt(args[1])) {
			    this.showList(player, Integer.parseInt(args[1]), 10);
			} else {
			    player.sendMessage(Messages.pageArgMustBeInteger);
			}
		    } else {
			this.showList(player, 0, 10);
		    }
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("plan")) {
		if (player.hasPermission("cameraapi.plan")) {
		    this.plan(player);
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("setpoint")) {
		if (player.hasPermission("cameraapi.setpoint")) {
		    this.setPoint(player, player.getLocation());
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("cancel")) {
		if (player.hasPermission("cameraapi.cancel")) {
		    this.cancel(player);
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("create")) {
		if (player.hasPermission("cameraapi.create")) {
		    if (args.length == 2) {
			this.create(player, args[1].toLowerCase());
		    } else {
			player.sendMessage(Messages.specifyNameUsage);
		    }
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("delete")) {
		if (player.hasPermission("cameraapi.delete")) {
		    if (args.length == 2) {
			this.delete(player, args[1].toLowerCase());
		    } else {
			player.sendMessage(Messages.specifyNameUsage);
		    }
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("undo")) {
		if (player.hasPermission("cameraapi.undo")) {
		    this.undo(player);
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("play")) {
		if (player.hasPermission("cameraapi.play")) {
		    if (args.length == 3) {
			if (CameraAPI.getInstance().doesPathExist(
				args[1].toLowerCase())) {
			    if (this.isInt(args[2])) {
				long time = Integer.parseInt(args[2]) * 20;
				CameraController controller;
				try {
				    controller = CameraAPI
					    .getInstance()
					    .createController(
						    player,
						    time,
						    CameraAPI
							    .getInstance()
							    .loadPath(
								    args[1].toLowerCase()));
				    controller.setCommandable(false);
				    controller.setDamageable(true);
				    CameraAPI.getInstance().travel(controller);
				} catch (Exception e) {
				    e.printStackTrace();
				}
			    } else {
				player.sendMessage(Messages.playArgMustBeInteger);
			    }
			} else {
			    player.sendMessage(Messages.pathDoesntExist);
			}
		    } else {
			player.sendMessage(Messages.incorrectUsage);
		    }
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else if (args[0].equalsIgnoreCase("interrupt")) {
		if (player.hasPermission("cameraapi.interrupt")) {
		    if (CameraAPI.getInstance().stopTravel(player)) {
			player.sendMessage(Messages.stoppingSequence);
		    } else {
			player.sendMessage(Messages.notRunningSequence);
		    }
		} else {
		    player.sendMessage(Messages.noPermission);
		}
	    } else {
		return false;
	    }
	    return true;
	} else {
	    if (player.hasPermission("cameraapi.help")) {
		// Get the help messages
		for (String message : this.getHelpMessage()) {
		    // For each help message, display it to the sender
		    player.sendMessage(message);
		}
	    } else {
		player.sendMessage(Messages.noPermission);
	    }
	    return true;
	}
    }

    /**
     * Check if a String value is an integer value
     * 
     * @param arg
     *            The String value you wish to check
     * @return Whether or not the String value is an integer or not
     */
    private boolean isInt(String arg) {
	try {
	    Integer.parseInt(arg);
	    return true;
	} catch (NumberFormatException e) {
	    System.out.println("An error occured whilst trying to format "
		    + arg + " into an integer value.");
	}
	return false;
    }

    /**
     * Show a list of path routes
     * 
     * @param page
     *            The page number
     */
    private void showList(Player player, int page, int length) {
	player.sendMessage("");
	player.sendMessage(ChatColor.GRAY + "---- ---- " + Messages.prefix
		+ " Page > " + String.valueOf(page) + " ---- ----");

	List<String> pathRoutes = new ArrayList<String>();
	for (File f : CameraAPI.getInstance().getDataFolder().listFiles()) {
	    if (f.getName().contains(".camera_path.yml")) {
		pathRoutes.add(f.getName().replace(".camera_path.yml", ""));
	    }
	}
	int i = 0, k = 0;
	for (String route : pathRoutes) {
	    i++;
	    if ((((page * length) + k + 1) == i)
		    && (i != ((page * length) + length + 1))) {
		k++;
		player.sendMessage(ChatColor.AQUA + " - " + ChatColor.DARK_AQUA
			+ route);
	    }
	}
	player.sendMessage("");
	player.sendMessage(ChatColor.GRAY
		+ "---- ---- ---- ---- ---- ---- ---- ----");
	player.sendMessage("");
    }

    /**
     * Plan a path route for the camera to follow
     * 
     * @param player
     *            The player who triggered the command
     * @return Whether the player was added to the planner or not
     */
    private boolean plan(Player player) {
	if (!this.creators.containsKey(player.getUniqueId())) {
	    // Create a new list to store the locations of each path
	    // point
	    this.creators.put(player.getUniqueId(), new ArrayList<Location>());
	    player.sendMessage(Messages.nowPlanningPath);
	    return true;
	} else {
	    player.sendMessage(Messages.alreadyPlanningPath);
	}
	return false;
    }

    /**
     * Add a point to a player's path planning
     * 
     * @param player
     *            The player who triggered the command
     * @param location
     *            The location you wish to add to the path
     * @return Whether or not the point was added successfully or not
     */
    private boolean setPoint(Player player, Location location) {
	if (this.creators.containsKey(player.getUniqueId())) {
	    // Add the location to the player's point list
	    List<Location> list = this.creators.get(player.getUniqueId());
	    list.add(location);

	    player.sendMessage(Messages.pointPlaced.replace("%ID%",
		    Integer.toString(list.size())));
	    return true;
	} else {
	    player.sendMessage(Messages.notPlanningPath);
	}
	return false;
    }

    /**
     * Cancel the planning of a path route
     * 
     * @param player
     *            The player who triggered the command
     * @return Whether or not planning was cancelled or not
     */
    private boolean cancel(Player player) {
	if (this.creators.containsKey(player.getUniqueId())) {
	    this.creators.remove(player.getUniqueId());
	    player.sendMessage(Messages.pathPlanningCancelled);

	    return true;
	} else {
	    player.sendMessage(Messages.notPlanningPath);
	}
	return false;
    }

    /**
     * Create a path and save it into a configuration file
     * 
     * @param player
     *            The player who triggered the command
     * @param name
     *            The name of the path route
     * @return Boolean value whether or not the path was created
     */
    private boolean create(Player player, String name) {
	if (this.creators.containsKey(player.getUniqueId())) {
	    if (this.creators.get(player.getUniqueId()).size() > 1) {
		if (CameraAPI.getInstance().doesPathExist(name.toLowerCase())) {
		    player.sendMessage(Messages.pathAlreadyExists);
		} else {
		    try {
			List<Location> list = this.creators.get(player
				.getUniqueId());
			CameraAPI.getInstance().savePath(name.toLowerCase(),
				list.toArray(new Location[list.size()]));
			player.sendMessage(Messages.pathCreated);
			return true;
		    } catch (Exception e) {
			player.sendMessage(Messages.pathCreationFailed);
		    }
		    return false;
		}
	    } else {
		player.sendMessage(Messages.notEnoughPoints);
	    }
	} else {
	    player.sendMessage(Messages.notPlanningPath);
	}
	return false;
    }

    /**
     * Delete a path route from the configuration folder
     * 
     * @param player
     *            Player who triggered the command
     * @param name
     *            The name of the path route
     * @return Boolean value whether or not the path was deleted
     */
    private boolean delete(Player player, String name) {
	try {
	    if (CameraAPI.getInstance().deletePath(name.toLowerCase())) {
		player.sendMessage(Messages.pathDeleted);
		return true;
	    }
	} catch (Exception e) {
	    player.sendMessage(Messages.pathDeletionFailed);
	}
	return false;
    }

    /**
     * Remove last point added to the list
     * 
     * @param player
     *            Player who added the point to the list
     * @return Boolean value whether or not the point was successfully removed
     */
    private boolean undo(Player player) {
	if (this.creators.containsKey(player.getUniqueId())) {
	    if (this.creators.get(player.getUniqueId()).size() > 0) {
		List<Location> list = this.creators.get(player.getUniqueId());
		list.remove(list.size() - 1);
		player.sendMessage(Messages.pathPointRemoved.replace("%ID%",
			Integer.toString(list.size() + 1)));
		return true;
	    } else {
		player.sendMessage(Messages.cannotUndo);
	    }
	} else {
	    player.sendMessage(Messages.notPlanningPath);
	}
	return false;
    }
}
