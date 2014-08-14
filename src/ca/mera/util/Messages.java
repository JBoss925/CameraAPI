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

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import org.bukkit.ChatColor;

public class Messages {

    public static ChatColor defaultColor = ChatColor.GOLD;

    // Plugin prefix
    public static String prefix = DARK_GRAY + "[" + Messages.defaultColor
	    + "CameraAPI" + DARK_GRAY + "] " + Messages.defaultColor;

    // Incorrect usage, use /camera help
    public static String incorrectUsage = RED + "\n"
	    + "[ERROR] Incorrect command usage. Please use [ /camera help ]."
	    + "\n";

    // You do not have permission
    public static String noPermission = RED + "\n"
	    + "You do not have permission to do that.";

    // Too many arguments
    public static String tooManyArgs = RED + "\n"
	    + "[ERROR] Too many arguments." + "\n";

    // The argument must be an integer
    public static String pageArgMustBeInteger = RED + "\n"
	    + "[ERROR] Page argument must be an integer." + "\n";

    // The player is now planning a path route
    public static String nowPlanningPath = "\n" + Messages.defaultColor
	    + "You're now planning a path route." + "\n"
	    + "Use [ /camera setpoint ] to add a point to the path." + "\n";

    // The player is already planning a path route
    public static String alreadyPlanningPath = "\n" + RED
	    + "[ERROR] You're already planning a path route." + "\n"
	    + Messages.defaultColor
	    + "Use [ /camera cancel ] to cancel your current route." + "\n";

    // A point was placed successfully on the path
    public static String pointPlaced = Messages.prefix + GREEN
	    + "A path point was successfully added to the route. {ID: " + AQUA
	    + "%ID%" + GREEN + "}";

    // The player is not currently planning a path route
    public static String notPlanningPath = "\n" + RED
	    + "[ERROR] You're not currently planning a route." + "\n"
	    + Messages.defaultColor
	    + "Use [ /camera plan ] to start creating a route." + "\n";

    // A path by that name already exists
    public static String pathAlreadyExists = RED + "\n"
	    + "A path by that name already exists." + "\n"
	    + Messages.defaultColor + "Please specify a different path name."
	    + "\n";

    public static String pathPlanningCancelled = Messages.prefix + GREEN
	    + "Path route planning cancelled.";

    // The path was successfully created
    public static String pathCreated = Messages.prefix + GREEN
	    + "Your path was successfully created.";

    // The path creation was unsuccessful
    public static String pathCreationFailed = RED
	    + "\n"
	    + "[ERROR] An unknown error occured whilst creating the path route."
	    + "\n";

    // You must specify a path name
    public static String specifyNameUsage = RED + "\n"
	    + "[ERROR] Not enough arguments. Please specify a name." + "\n";

    // You must have atleast 2 location points
    public static String notEnoughPoints = RED + "\n"
	    + "[ERROR] A path route must contain atleast 2 locations." + "\n";

    // The path specified was successfully deleted
    public static String pathDeleted = Messages.prefix + GREEN
	    + "The path specified was successfully deleted.";

    // The path specified failed to be deleted
    public static String pathDeletionFailed = RED + "\n"
	    + "[ERROR] An error occured whilst attempting to delete the" + "\n"
	    + "specified path. The path route was not deleted." + "\n";

    // The last point on the path was removed
    public static String pathPointRemoved = Messages.prefix + GREEN
	    + "The last point was successfully removed. {ID: " + AQUA + "%ID%"
	    + GREEN + "}";

    // There are no more points to remove
    public static String cannotUndo = RED + "\n"
	    + "[ERROR] There are no more points to undo." + "\n";

    // The argument must be an integer
    public static String playArgMustBeInteger = RED + "\n"
	    + "[ERROR] The last argument must be an integer." + "\n";

    // The path specified doesn't exist
    public static String pathDoesntExist = RED + "\n"
	    + "[ERROR] The path route specified doesn't exist." + "\n";

    // Stopping camera sequence
    public static String stoppingSequence = Messages.prefix + GREEN
	    + "Interrupting camera sequence.";

    // The command sender is not running a sequence
    public static String notRunningSequence = RED + "\n"
	    + "[ERROR] You're not currently in a running sequence.";

    // The help command usage
    public static String helpCommand = Messages.defaultColor + "/camera help"
	    + GRAY + " - Displays this menu.";

    // The list command usage
    public static String listCommand = Messages.defaultColor
	    + "/camera list <page>" + GRAY
	    + " - Display's all of the created routes.";

    // The plan command usage
    public static String planCommand = Messages.defaultColor + "/camera plan"
	    + GRAY + " - Enabled the planning of a route.";

    // The setpoint command usage
    public static String setPointCommand = Messages.defaultColor
	    + "/camera setpoint" + GRAY + " - Adds a point to the path route.";

    // The cancel command usage
    public static String cancelCommand = Messages.defaultColor
	    + "/camera cancel" + GRAY + " - Cancels your current route plan.";

    // The create command usuage
    public static String createCommand = Messages.defaultColor
	    + "/camera create <route>" + GRAY
	    + " - Create a route using your plan.";

    // The delete command usage
    public static String deleteCommand = Messages.defaultColor
	    + "/camera delete <route>" + GRAY + " - Delete a route.";

    // The undo command usage
    public static String undoCommand = Messages.defaultColor + "/camera undo"
	    + GRAY + " - Removes the last point added to the plan.";

    // The play command usage
    public static String playCommand = Messages.defaultColor
	    + "/camera play <route> <seconds>" + GRAY
	    + " - Play's a route in the given amount of seconds.";

    public static String interruptCommand = Messages.defaultColor
	    + "/camera interrupt" + GRAY + " - Interrupts a camera sequence.";

}
