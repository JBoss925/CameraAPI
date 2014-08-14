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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ca.mera.events.TravelEndEvent;
import ca.mera.events.TravelPointHitEvent;

public class CameraController extends BukkitRunnable {

    // Global variables
    private Player player;
    private final Location[] points;

    // Path properties
    private final CameraPath path;

    // Aesthetic properties
    private boolean damageable;
    private boolean canCommand;
    private boolean canInterrupt;

    /**
     * Create an instance of the CameraController
     * 
     * @param points
     *            The locations to cycle through
     */
    protected CameraController(Player player, long duration, Location... points) {
	this.player = player;
	this.points = points;
	this.path = new CameraPath(duration, points);

	if (points.length < 2) {
	    throw new RuntimeException(
		    "The controller requires at least two locations (a start and an end point!)");
	}

	// Aesthetic properties
	this.damageable = false;
	this.canCommand = false;
    }

    /**
     * Set whether or not a player can take damage during travelling
     * 
     * @param damageable
     *            Boolean value
     */
    public void setDamageable(boolean damageable) {
	this.damageable = damageable;
    }

    /**
     * Set whether or not a player can use commands during travelling
     * 
     * @param canCommand
     *            Boolean value
     */
    public void setCommandable(boolean canCommand) {
	this.canCommand = canCommand;
    }

    /**
     * Set whether or not a player can use commands during travelling
     * 
     * @param canCommand
     *            Boolean value
     * @param canInterrupt
     *            Boolean value
     */
    public void setCommandable(boolean canCommand, boolean canInterrupt) {
	this.canCommand = canCommand;
	this.canInterrupt = canInterrupt;
    }

    /**
     * Check whether or not a player can take damage during travelling
     * 
     * @return Boolean value
     */
    public boolean isDamageable() {
	return this.damageable;
    }

    /**
     * Check whether or not a player can use commands during travelling
     * 
     * @return Boolean value
     */
    public boolean isCommandable() {
	return this.canCommand;
    }

    /**
     * Check whether or not a player can use the /camera interrupt command
     * during travel
     * 
     * @return Boolean value
     */
    public boolean canInterrupt() {
	return this.canInterrupt;
    }

    // Travel Points iterating value
    private int travelPoint = 0;

    @Override
    public void run() {
	// Check if the player travelling has been overridden
	if (!CameraAPI.getInstance().isTravelling(this.player)) {
	    // If so then stop the runnable
	    this.stop();
	}

	// Teleport to each location between the points
	if (this.travelPoint < this.path.getSize()) {
	    Location loc = this.path.getPoint(this.travelPoint);
	    this.player.teleport(loc);

	    // Loop through each default point stored in the array
	    for (Location point : this.points) {
		// Check if the current location is equal to any default point
		if ((loc.getX() == point.getX())
			&& (loc.getY() == point.getY())
			&& (loc.getZ() == point.getZ())) {

		    // If it is then trigger TravelPointHitEvent
		    Bukkit.getPluginManager().callEvent(
			    new TravelPointHitEvent(this.player, loc));
		}
	    }
	} else {
	    // Trigger TravelEndEvent
	    Bukkit.getPluginManager()
		    .callEvent(new TravelEndEvent(this.player));
	    // Stop the running task
	    this.stop();
	}
	this.travelPoint += 1;
    }

    /**
     * Get the player of the controller
     * 
     * @return Player object
     */
    protected Player getPlayer() {
	return this.player;
    }

    /**
     * Starts the navigation of the player
     */
    protected void start() {
	// Enable the players flight properties
	this.player.setAllowFlight(true);
	this.player.setFlying(true);

	// Run the task timer every tick
	this.runTaskTimer(CameraAPI.getInstance(), 0L, 1L);
    }

    /**
     * Stops the navigation of the player
     */
    protected void stop() {
	this.cancel();
    }

}
