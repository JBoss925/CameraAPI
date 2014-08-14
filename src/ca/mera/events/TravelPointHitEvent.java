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
package ca.mera.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TravelPointHitEvent extends Event {

    // Handler list
    private static final HandlerList handlers = new HandlerList();

    // Global variables
    private Player player;
    private Location location;

    /**
     * Create an instance of the TravelPointHitEvent
     * 
     * @param player
     *            Player who triggered the event
     * @param location
     *            The player's location when he triggered the event
     */
    public TravelPointHitEvent(Player player, Location location) {
	this.player = player;
	this.location = location;
    }

    /**
     * Get the player who triggered the event
     * 
     * @return Player object
     */
    public Player getPlayer() {
	return this.player;
    }

    /**
     * Get the location when the player triggered the event
     * 
     * @return Location object
     */
    public Location getLocation() {
	return this.location;
    }

    @Override
    public HandlerList getHandlers() {
	return TravelPointHitEvent.handlers;
    }

    /**
     * Get the HandlersList
     * 
     * @return HanderList object
     */
    public static HandlerList getHandlerList() {
	return TravelPointHitEvent.handlers;
    }

}
