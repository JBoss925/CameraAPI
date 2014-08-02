package ca.mera;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CameraController extends BukkitRunnable {

    // Global variables
    private Player player;
    private final Location[] points;

    // Time properties
    private final long duration;
    private long time;

    // Path properties
    private boolean smooth;

    /**
     * Create an instance of the CameraController
     * 
     * @param points
     *            The locations to cycle through
     */
    protected CameraController(Player player, long duration, Location[] points) {
	this.player = player;
	this.duration = duration;
	this.points = points;

	// Path properties
	this.smooth = true;

	if (points.length < 2) {
	    throw new RuntimeException(
		    "A travel requires at least two locations (a start and an end point!)");
	}
    }

    @Override
    public void run() {
	// teleport to each point
    }

    /**
     * Starts the navigation of the player
     */
    public void start(boolean smooth) {
	// Set the time
	this.time = this.duration;

	// Set the path properties
	this.smooth = smooth;

	// Enable the players flight properties
	this.player.setAllowFlight(true);
	this.player.setFlying(true);

	// Run the task timer every tick
	this.runTaskTimer(CameraAPI.getInstance(), 0L, 1L);

	// Set the player as traveling
	CameraAPI.getInstance().setTravelling(this.player, true);
    }

    /**
     * Stops the navigation of the player
     */
    public void stop() {
	this.cancel();
    }

}
