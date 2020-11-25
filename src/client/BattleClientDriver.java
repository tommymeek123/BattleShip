package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * A driver to start a Battleship client
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version December, 2020
 */
public class BattleClientDriver {

   /** The index of the command line argument specifying the server hostname. */
   private final static int HOST_ARG = 0;

   /** The index of the command line argument specifying the port number. */
   private final static int PORT_ARG = 1;

   /** The index of the command line argument specifying the username. */
   private final static int NAME_ARG = 2;

   /**
    * Main method that functions as an entry point to the program.
    *
    * @param args Command line arguments to the program. There must be exactly
    *             3 arguments. The first argument specifies the hostname or IP
    *             address of a remote host running a BattleServer. The second
    *             argument is the port number of that server. The third
    *             argument is the username a client wishes to use.
    */
   public static void main(String[] args) {
      BattleClientDriver driver = new BattleClientDriver();
      driver.go(args);
   }

   /**
    * Creates a BattleClient and handles main startup process.
    *
    * @param args The command line arguments passed from main.
    */
   public void go(String[] args) {
      // Check command line arguments.
      this.validateArgs(args);
      InetAddress hostname = this.validateHost(args[HOST_ARG]);
      int port = Integer.parseInt(args[PORT_ARG]);
      String username = args[NAME_ARG];

      // Start client and loop for input.
      BattleClient bc = makeClient(hostname, port, username);
      this.getInput(bc);
   }

   /**
    * Loops to collect user input to pass to the client.
    *
    * @param bc The client we are collecting input for.
    */
   private void getInput(BattleClient bc) {
      //List<String> forbiddenCommands = List.of("/join");
      boolean keepPlaying = true;
      Scanner input = new Scanner(System.in);
      while (keepPlaying) {
         String command = input.nextLine();
         if (command.contains("/join")) {
            System.out.println("Invalid command: " + command);
         } else {
            bc.send(command);
            if (command.contains("/quit") || !bc.getReady()) {
               keepPlaying = false;
            }
         }
      }
   }

   /**
    * Creates a BattleClient and connects it to a BattleServer.
    *
    * @param hostname the server the client is trying to connect to
    * @param port the port used to try to connect to the server
    * @param username the username of the client player
    * @return A client.
    */
   private BattleClient makeClient(InetAddress hostname, int port,
                                   String username) {
      var outStream = new PrintStreamMessageListener(System.out);
      BattleClient bc = new BattleClient(hostname, port, username);
      bc.addMessageListener(outStream);
      try {
         bc.connect();
      } catch (IOException ioe) {
         System.err.println("Error connecting.\n");
         ioe.printStackTrace();
         System.exit(1);
      }
      return bc;
   }

   /**
    * Ensures that the command line arguments are valid.
    *
    * @param args Command line arguments to the program.
    */
   private void validateArgs(String[] args) {
      final String PORT_ERR_MSG = "Invalid port number. Should be [1024-65535]";
      final String NAME_ERR_MSG = "Enter a username smaller than 20 characters";
      final int NUM_ARGS = 3;
      if (args.length != NUM_ARGS) {
         this.usage();
      }
      if (!this.validatePort(args[PORT_ARG])) {
         System.err.println(PORT_ERR_MSG);
         System.exit(1);
      }
      if (!this.validateName(args[NAME_ARG])) {
         System.err.println(NAME_ERR_MSG);
         System.exit(1);
      }
   }

   /**
    * Validates that a string passed in is an integer between 1024 and 65535.
    *
    * @param portStr A string that should be a valid port number.
    * @return True if the string passed is a valid port number. False otherwise.
    */
   private boolean validatePort(String portStr) {
      final int MAX_PORT  = 65535;
      final int MIN_PORT  = 1024;
      int portNum;
      try {
         portNum = Integer.parseInt(portStr);
      } catch (NumberFormatException nfe) {
         return false;
      }
      return portNum >= MIN_PORT && portNum <= MAX_PORT;
   }

   /**
    * Validates that a string passed in is a valid username.
    *
    * @param name A string that should be a valid username.
    * @return True if the string passed is a valid username. False otherwise.
    */
   private boolean validateName(String name) {
      final int MAX_NAME_LENGTH = 20;
      return name.length() <= MAX_NAME_LENGTH;
   }

   /**
    * Converts a string entered by the user to an InetAddress.
    *
    * @param host String representing either the host name or the IP address.
    * @return The relevant InetAddress
    */
   private InetAddress validateHost(String host) {
      final String HOST_ERR_MSG = "Invalid host name.";
      InetAddress address = null;
      try {
         address = InetAddress.getByName(host);
      } catch (UnknownHostException uhe) {
         System.err.println(HOST_ERR_MSG);
         System.exit(1);
      }
      return address;
   }

   /**
    * Prints a message indicating how the program should be used.
    */
   private void usage() {
      final String USG_MSG = "java client.BattleClientDriver " +
                             "<host> <port> <username>";
      System.err.println(USG_MSG);
      System.exit(1);
   }

}
