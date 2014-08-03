/**
 * A bukkit plugin which allows the user to neatly navigate 
 * their in-game player in a cinematic camera like way.
 *
 * @author Kieron Wiltshire
 * 	Email: kieron.wiltshire@outlook.com
 * 	Skype: kieron.wiltshire
 * @author CurseLuke
 * 
 * This plugin was inspired by the CameraStudio plugin.
 * The CameraStudio plugin was very neat except it wasn't
 * very developer friendly. It was originally created by
 * the ( crushedpixel.eu ) server team and was revamped
 * by Kieron Wiltshire and CurseLuke.
 *
 */
package ca.mera.util;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Executor implements CommandExecutor {

    private String usage;
    private String description;
    private String permission;
    private boolean playerOnly;

    /**
     * Create an instance of the Executor
     * 
     * @param usage
     *            how to use the command correctly
     * @param description
     *            the description of the command
     * @param permission
     *            permission required to use
     * @param playerOnly
     *            can be operated by both or by the player
     */
    protected Executor(String usage, String description, String permission,
	    boolean playerOnly) {
	this.usage = usage;
	this.description = description;
	this.permission = permission;
	this.playerOnly = playerOnly;
    }

    /**
     * Get the command usage
     * 
     * @return String value
     */
    public String getUsage() {
	return this.usage;
    }

    /**
     * Get the command description
     * 
     * @return String value
     */
    public String getDescription() {
	return this.description;
    }

    /**
     * Get the command permission node
     * 
     * @return String value
     */
    public String getPermission() {
	return this.permission;
    }

    /**
     * Check if the command is a player only command
     * 
     * @return Boolean value
     */
    public boolean isPlayerOnly() {
	return this.playerOnly;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
	    String[] args) {
	// Check if the command is a player only command
	if (this.playerOnly) {
	    // if it is then check if the sender was an instance of Player
	    if (sender instanceof Player) {
		// If the sender was a Player then initiate the command and
		// return true
		this.init(sender, cmd, label, args);
		return true;
	    } else {
		// Otherwise send a message to the player telling them that's a
		// player only command
		sender.sendMessage(ChatColor.RED
			+ "You must be a player in order to use that command.");
	    }
	} else {
	    // If it's not a player only command then initiate the command
	    // anyway and return true
	    this.init(sender, cmd, label, args);
	    return true;
	}
	// Return false
	return false;
    }

    /**
     * Checks to see if the command sender has the sufficient permissions and if
     * he typed the command correctly.
     * 
     * @param sender
     *            sender of the command
     * @param cmd
     *            command object
     * @param label
     *            command label
     * @param args
     *            arguments after the command label
     */
    private void init(CommandSender sender, Command cmd, String label,
	    String[] args) {
	// Check if the sender has permission to use the command
	if (!sender.hasPermission(this.permission)) {
	    // If he doesn't then send the sender a message
	    sender.sendMessage(ChatColor.RED
		    + "You do not have permission to use that command.");
	}
	// Check if the command was typed correctly
	if (!(this.handler(sender, label, args))) {
	    sender.sendMessage(ChatColor.RED + "Correct usage: " + this.usage);
	}
    }

    /**
     * Handler handles commands
     * 
     * @param sender
     *            sender of the command
     * @param label
     *            command label
     * @param args
     *            arguments after the command label
     * @return true if the command was successfully triggered
     */
    public abstract boolean handler(CommandSender sender, String label,
	    String[] args);

}
