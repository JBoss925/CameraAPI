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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationFile {

    // Configuration related variables
    private FileConfiguration config;
    private File configFile;

    // File related variables
    private JavaPlugin plugin;
    private File path;
    private String name;

    /**
     * Create a new configuration file instance
     * 
     * @param plugin
     *            JavaPlugin object
     * @param path
     *            the path to the file
     * @param file
     *            the name of the configuration file
     */
    public ConfigurationFile(JavaPlugin plugin, File path, String name) {
	// Initialise the global variables
	this.plugin = plugin;
	this.path = path;
	// Add a file format on the end of the file name
	this.name = name + ".yml";
    }

    /**
     * Get the name of the file
     * 
     * @return the name of the file
     */
    public String getName() {
	// Get the name of the file
	return this.name.replace(".yml", "");
    }

    /**
     * Check if the configuration file exists
     * 
     * @return true if the configuration file exists
     */
    public boolean doesFileExist() {
	// Check if the file exists
	if (new File(this.path, this.name).exists()) {
	    // If it does then return true
	    return true;
	}
	return false;
    }

    /**
     * Get the configuration file
     * 
     * @return FileConfiguration object
     */
    public FileConfiguration getConfigFile() {
	// Check if the config object is null
	if (this.config == null) {
	    // If it's null then reload the config object
	    this.reload();
	}
	// Return the config object
	return this.config;
    }

    /**
     * Reload the configuration file
     */
    public void reload() {
	// Check if the configFile object is null
	if (this.configFile == null) {
	    // If it's null then initialise the variable
	    this.configFile = new File(this.plugin.getDataFolder(), this.name);
	}
	// Initialise the config object loading the YamlConfiguration from the
	// configFile object
	this.config = YamlConfiguration.loadConfiguration(this.configFile);

	// The configuration file itself
	File defConfigFile = new File(this.path, this.name);
	// Check if it's null
	if (defConfigFile != null) {
	    // If it isn't null then get it's YamlConfiguration and set it's
	    // defaults
	    YamlConfiguration defConfig = YamlConfiguration
		    .loadConfiguration(defConfigFile);
	    this.config.setDefaults(defConfig);
	}
    }

    /**
     * Save the configuration file
     */
    public void save() {
	// Check if either the config or configFile objects are null
	if ((this.config == null) || (this.configFile == null)) {
	    // Then return and don't save anything
	    return;
	}
	try {
	    // Try and save the file
	    this.getConfigFile().save(this.configFile);
	} catch (IOException ex) {
	    // If it fails then log the error into the Bukkit logger
	    this.plugin.getLogger().log(Level.SEVERE,
		    "Error whilst saving " + this.configFile, ex);
	}
    }

    /**
     * Save a copy of the default configuration file
     */
    public void copy() {
	// Check if the configFile object is null
	if (this.configFile == null) {
	    // If it's null then initialise the variable
	    this.configFile = new File(this.path, this.name);
	}
	// Check if the file doesn't exists
	if (!this.configFile.exists()) {
	    // If it doesn't then get the resource from the jar
	    InputStream is = this.plugin.getResource(this.name);
	    // If the resource exists within the jar
	    if (is != null) {
		// Create an OutputStream in order to export the file from the
		// jar
		OutputStream resStreamOut = null;
		int readBytes;
		byte[] buffer = new byte[4096];
		try {
		    // Create a FileOutputStream in order to copy the file from
		    // the jar
		    resStreamOut = new FileOutputStream(new File(this.path
			    + File.separator + this.name));
		    // While the bytes are more than 0
		    while ((readBytes = is.read(buffer)) > 0) {
			// Output the bytes to the newly created file
			resStreamOut.write(buffer, 0, readBytes);
		    }
		} catch (IOException e) {
		    // If an error occurs print a stacktrace
		    e.printStackTrace();
		} finally {
		    try {
			// Close both streams
			is.close();
			resStreamOut.close();
		    } catch (IOException e) {
			// If an error occurs print a stacktrace
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    /**
     * Create a blank configuration file
     */
    public void createFile() {
	// Check if the file doesn't exist
	if (!new File(this.path, this.name).exists()) {
	    try {
		// If it doesn't then try and create a new blank file
		new File(this.path, this.name).createNewFile();
	    } catch (IOException e) {
		// If an error occurs print a stacktrace
		e.printStackTrace();
	    }
	}
    }

    /**
     * Delete the configuration file
     */
    public void deleteFile() {
	// Check if the file exists
	if (new File(this.path, this.name).exists()) {
	    // If it does then delete it
	    new File(this.path, this.name).delete();
	}
    }

}
