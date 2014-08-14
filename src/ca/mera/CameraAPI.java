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
package ca.mera;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.mera.commands.CameraCommand;
import ca.mera.events.TravelEndEvent;
import ca.mera.util.ConfigurationFile;

public class CameraAPI extends JavaPlugin implements Listener {

    // CameraAPI plugin instance
    private static CameraAPI instance;

    // A List of all of the players that are travelling
    private HashMap<UUID, CameraController> travellers;

    /**
     * When the plugin is enabled
     */
    @Override
    public void onEnable() {
	CameraAPI.instance = this;
	this.travellers = new HashMap<UUID, CameraController>();
	Bukkit.getPluginManager().registerEvents(this, this);
	this.getCommand("camera").setExecutor(new CameraCommand());
    }

    /**
     * Get the cameraAPI instance
     * 
     * @return CameraAPI object
     */
    public static CameraAPI getInstance() {
	return CameraAPI.instance;
    }

    /**
     * Get a set of each player which is currently travelling
     * 
     * @return
     */
    public Set<UUID> getTravellers() {
	return this.travellers.keySet();
    }

    /**
     * Check if the player is travelling
     * 
     * @param player
     *            Player you wish to check
     * @return boolean value based whether or not the player is travelling
     */
    public boolean isTravelling(Player player) {
	return this.travellers.containsKey(player.getUniqueId());
    }

    /**
     * Create a CameraController to control a player
     * 
     * @param player
     *            Player you wish to control
     * @param time
     *            The time it takes to complete the path
     * @param points
     *            The locations you wish to smoothly teleport to
     * @return CameraController object
     */
    public CameraController createController(Player player, long time,
	    Location... points) {
	return new CameraController(player, time, points);
    }

    /**
     * Set whether the player is travelling or not
     * 
     * @param player
     *            The player you wish to set the state of
     * @param isTravelling
     *            The state you wish to set
     */
    public void travel(CameraController controller) {
	// If the player is travelling then
	if (this.getTravellers().contains(controller.getPlayer().getUniqueId())) {
	    // Remove him from the HashMap
	    this.travellers.remove(controller.getPlayer().getUniqueId());
	}
	// Put him in the HashMap and initiate the runnable
	this.travellers.put(controller.getPlayer().getUniqueId(), controller);
	controller.start();
    }

    /**
     * Stop the player from travelling if the player is travelling
     * 
     * @param player
     *            Player you wish to stop the travelling of
     */
    public boolean stopTravel(Player player) {
	// If the player is travelling then
	if (this.getTravellers().contains(player.getUniqueId())) {
	    // Remove him from the HashMap
	    this.travellers.get(player.getUniqueId()).stop();
	    this.travellers.remove(player.getUniqueId());
	    return true;
	}
	return false;
    }

    /**
     * Stop the player from travelling if the player is travelling
     * 
     * @param controller
     *            The controller you wish to stop
     */
    public boolean stopTravel(CameraController controller) {
	return this.stopTravel(controller.getPlayer());
    }

    /**
     * Check if a path already exists
     * 
     * @param name
     *            The name of the path
     * @return Boolean value
     */
    public boolean doesPathExist(String name) {
	File pathFile = new File(this.getDataFolder(), name
		+ ".camera_path.yml");
	return pathFile.exists();
    }

    /**
     * Save locations into a config
     * 
     * @param name
     *            The name of the path
     * @param list
     *            The path points you wish to save
     * @throws Exception
     *             A path requires 2 or more locations
     */
    public boolean savePath(String name, Location... list) throws Exception {
	if (!this.doesPathExist(name)) {
	    // If there are 2 or more points existing
	    if (list.length > 1) {
		// Path file object
		ConfigurationFile pathFile = new ConfigurationFile(this,
			this.getDataFolder(), name + ".camera_path");

		// Create the file if it doesn't already exist
		if (!pathFile.doesFileExist()) {
		    pathFile.createFile();
		}

		int id = 0;
		for (Location location : list) {
		    pathFile.getConfigFile().set("points." + id + ".x",
			    location.getX());
		    pathFile.getConfigFile().set("points." + id + ".y",
			    location.getY());
		    pathFile.getConfigFile().set("points." + id + ".z",
			    location.getZ());
		    pathFile.getConfigFile().set("points." + id + ".yaw",
			    location.getYaw());
		    pathFile.getConfigFile().set("points." + id + ".pitch",
			    location.getPitch());
		    pathFile.getConfigFile().set("points." + id + ".world",
			    location.getWorld().getName());
		    id++;
		}
		pathFile.save();
		return true;
	    } else {
		throw new Exception(
			"The path file [ "
				+ name
				+ " ] couldn't be created. A path requires 2 or more locations.");
	    }
	}
	return false;
    }

    /**
     * Load locations from a config
     * 
     * @param name
     *            The name of the path you wish to load
     * @throws Exception
     */
    public Location[] loadPath(String name) throws Exception {
	if (this.doesPathExist(name)) {
	    // A List to store all of the path points
	    List<Location> points = new ArrayList<Location>();

	    // Path file object
	    ConfigurationFile pathFile = new ConfigurationFile(this,
		    this.getDataFolder(), name + ".camera_path");

	    // Load a path from the config file
	    for (String id : pathFile.getConfigFile()
		    .getConfigurationSection("points").getKeys(false)) {
		double x = pathFile.getConfigFile().getDouble(
			"points." + id + ".x");
		double y = pathFile.getConfigFile().getDouble(
			"points." + id + ".y");
		double z = pathFile.getConfigFile().getDouble(
			"points." + id + ".z");
		float yaw = pathFile.getConfigFile().getInt(
			"points." + id + ".yaw");
		float pitch = pathFile.getConfigFile().getInt(
			"points." + id + ".pitch");
		World world = Bukkit.getWorld(pathFile.getConfigFile()
			.getString("points." + id + ".world"));

		points.add(new Location(world, x, y, z, yaw, pitch));
	    }
	    return points.toArray(new Location[points.size()]);
	} else {
	    throw new Exception("The path [ " + name
		    + " ] doesn't exist. Failed to load path.");
	}
    }

    /**
     * Get all of the created path routes
     * 
     * @return String[] value
     */
    public String[] getPathRoutes() {
	List<String> routes = new ArrayList<String>();
	for (File file : this.getDataFolder().listFiles()) {
	    if (file.getName().contains(".camera_path.yml")) {
		routes.add(file.getName().replace(".camera_path.yml", ""));
	    }
	}
	return routes.toArray(new String[routes.size()]);
    }

    /**
     * Delete a path configuration file
     * 
     * @param name
     *            The name of the file
     * @throws Exception
     *             If the path file doesn't exist an exception is thrown
     */
    public boolean deletePath(String name) throws Exception {
	if (this.doesPathExist(name)) {
	    // Path file object
	    ConfigurationFile pathFile = new ConfigurationFile(this,
		    this.getDataFolder(), name + ".camera_path");

	    // Delete the file
	    pathFile.deleteFile();

	    return true;
	} else {
	    throw new Exception("The path [ " + name
		    + " ] doesn't exist. Failed to delete path.");
	}
    }

    /**
     * An event which prevents a player travelling after death
     * 
     * @param event
     *            PlayerDeathEvent object
     */
    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
	// If the player is travelling
	if (this.isTravelling(event.getEntity())) {
	    // Then stop the travel
	    this.stopTravel(event.getEntity());
	}
    }

    /**
     * An event which prevents a player travelling to take damage
     * 
     * @param event
     *            EntityDamageEvent object
     */
    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
	// Check if the entity damaged was a player
	if (event.getEntity() instanceof Player) {
	    // If the entity is a player and the player is currently travelling
	    if (this.isTravelling(((Player) event.getEntity()))) {
		// If the player can't take damage during travelling
		if (!this.travellers.get(
			((Player) event.getEntity()).getUniqueId())
			.isDamageable()) {
		    // Then cancel the event
		    event.setCancelled(true);
		}
	    }
	}
    }

    /**
     * An event which prevents a player travelling to use commands
     * 
     * @param event
     *            PlayerCommandPreprocessEvent object
     */
    @EventHandler
    private void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
	// If the player is currently travelling
	if (this.isTravelling(event.getPlayer())) {
	    // If the player can't use commands during travelling
	    if (!this.travellers.get(event.getPlayer().getUniqueId())
		    .isCommandable()) {
		// Then cancel the event
		boolean cancel = false;
		if (!event.getMessage().equalsIgnoreCase("/camera interrupt")) {
		    cancel = true;
		} else {
		    if (this.travellers.get(event.getPlayer().getUniqueId())
			    .canInterrupt()) {
			cancel = true;
		    } else {
			cancel = false;
		    }
		}
		if (cancel) {
		    event.setCancelled(true);
		    event.getPlayer().sendMessage(
			    ChatColor.RED
				    + "You cannot use commands at this time.");
		}
	    }
	}
    }

    @EventHandler
    public void onEnd(TravelEndEvent event) {
	this.stopTravel(event.getPlayer());
    }
}
