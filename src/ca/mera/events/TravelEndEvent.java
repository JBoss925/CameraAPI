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

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TravelEndEvent extends Event {

    // Handler list
    private static final HandlerList handlers = new HandlerList();

    // Global variables
    private Player player;

    /**
     * Create an instance of the TravelPointHitEvent
     * 
     * @param player
     *            Player who triggered the event
     */
    public TravelEndEvent(Player player) {
	this.player = player;
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
     * Get the Handlers
     */
    @Override
    public HandlerList getHandlers() {
	return TravelEndEvent.handlers;
    }

    /**
     * Get the HandlersList
     * 
     * @return HanderList object
     */
    public static HandlerList getHandlerList() {
	return TravelEndEvent.handlers;
    }

}
