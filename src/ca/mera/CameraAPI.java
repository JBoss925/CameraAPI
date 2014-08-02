/**
 * A bukkit plugin which allows the user to neatly navigate their player in a
 * camera like sense.
 *
 * @author Kieron Wiltshire
 * @author CurseLuke
 *
 */
package ca.mera;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CameraAPI extends JavaPlugin {

    /**
     * Get the cameraAPI instance
     * 
     * @return CameraAPI object
     */
    public static CameraAPI getInstance() {
	return CameraAPI.instance;
    }

    // CameraAPI plugin instance
    private static CameraAPI instance;

    // A List of all of the players that are travelling
    private List<UUID> travellers = new ArrayList<UUID>();

    /**
     * Get a list of
     * 
     * @return
     */
    public List<UUID> getTravellers() {
	return this.travellers;
    }

    /**
     * Check if the player is travelling
     * 
     * @param player
     *            Player you wish to check
     * @return boolean value based whether or not the player is travelling
     */
    public boolean isTravelling(Player player) {
	return this.travellers.contains(player.getUniqueId());
    }

    // bad name
    public CameraController createController(Player player, Location... points) {
	return new CameraController(player, points);
    }

    /**
     * When the plugin is enabled
     */
    @Override
    public void onEnable() {
	CameraAPI.instance = this;
    }

    /**
     * Set whether the player is travelling or not
     * 
     * @param player
     *            The player you wish to set the state of
     * @param isTravelling
     *            The state you wish to set
     */
    public void setTravelling(Player player, boolean isTravelling) {
	UUID uuid = player.getUniqueId();
	this.getTravellers().remove(uuid);
	if (isTravelling) {
	    this.getTravellers().add(uuid);
	}
    }

}
