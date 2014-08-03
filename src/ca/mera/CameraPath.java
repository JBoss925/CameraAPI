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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

public class CameraPath {

    // Camera path
    private final List<Location> path = new ArrayList<Location>();

    /**
     * Create a CameraPath instance
     * 
     * @param time
     *            Time is used to determine how far apart each point should be
     * @param world
     *            The world of the path
     * @param points
     *            The points you wish to add to the path
     */
    protected CameraPath(long time, Location... points) {
	/**
	 * Original code was created by the ( crushedpixel.eu ) team
	 * 
	 * @author CrushedPixel
	 * @author Rafessor
	 */

	/**
	 * Revamped by Kieron Wiltshire and CurseLuke
	 * 
	 * @author KieronWiltshire
	 * @author CurseLuke
	 * 
	 */
	{
	    // Split the locations up into a navigable path
	    List<Double> distances = new ArrayList<Double>();
	    List<Integer> times = new ArrayList<Integer>();

	    double totalDistance = 0.0D;
	    for (int i = 0; i < (points.length - 1); i++) {
		// First we get the locations
		Location a = points[i];
		Location b = points[i + 1];

		// Then we get the distance
		double distance = a.distance(b);

		// We add the distance onto the total distance
		totalDistance += distance;

		// Add the value to the list
		distances.add(Double.valueOf(distance));
	    }
	    // For each distance value
	    for (Double d : distances) {
		// We add the time it takes to get there
		times.add(Integer.valueOf((int) ((d / totalDistance) * time)));
	    }

	    // Store the world of the player
	    World world = points[0].getWorld();

	    for (int i = 0; i < (points.length - 1); i++) {

		// Get the locations
		Location a = points[i];
		Location b = points[i + 1];

		// Get the travel time of that location
		int travelTime = times.get(i).intValue();

		// Get the move amount
		double moveX = b.getX() - a.getX();
		double moveY = b.getY() - a.getY();
		double moveZ = b.getZ() - a.getZ();
		double movePitch = b.getPitch() - a.getPitch();
		double yawDistance = Math.abs(b.getYaw() - a.getYaw());

		// Create the difference of the locations both yaw values
		double cam = 0.0D;
		if (yawDistance <= 180.0D) {
		    if (a.getYaw() < b.getYaw()) {
			cam = yawDistance;
		    } else {
			cam = -yawDistance;
		    }
		} else if (a.getYaw() < b.getYaw()) {
		    cam = -(360.0D - yawDistance);
		} else {
		    cam = 360.0D - yawDistance;
		}
		double dist = cam / travelTime;

		for (int x = 0; x < travelTime; x++) {

		    // First we create the coords for each location
		    double pathX = a.getX() + ((moveX / travelTime) * x);
		    double pathY = a.getY() + ((moveY / travelTime) * x);
		    double pathZ = a.getZ() + ((moveZ / travelTime) * x);
		    float pathYaw = (float) (a.getYaw() + (dist * x));
		    float pathPitch = (float) (a.getPitch() + ((movePitch / travelTime) * x));

		    // Then we create the location
		    Location pathLocation = new Location(world, pathX, pathY,
			    pathZ, pathYaw, pathPitch);

		    // Then we add the location to the path route
		    this.path.add(pathLocation);
		}
	    }
	}
    }

    /**
     * Get all of the points on the path
     * 
     * @return List<Location> object
     */
    protected List<Location> getPoints() {
	return this.path;
    }

    /**
     * Get a specific point on the path
     * 
     * @param point
     *            The point id you wish to retrieve
     * @return Location object
     */
    protected Location getPoint(int point) {
	return this.path.get(point);
    }

    /**
     * Get the amount of points on the path
     * 
     * @return Integer value
     */
    protected int getSize() {
	return this.path.size();
    }

}
