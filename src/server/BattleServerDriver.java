package server;

import java.io.IOException;

/**
 * A driver to start the Battleship server.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version December, 2020
 */
public class BattleServerDriver {

   /** The index of the command line argument specifying the port number. */
   private final static int PORT_ARG = 0;

   /** The index of the command line argument specifying the grid size. */
   private final static int SIZE_ARG = 1;

   /** The minimum number of command line arguments. */
   private final static int MIN_ARGS = 1;

   /** The maximum number of command line arguments. */
   private final static int MAX_ARGS = 2;

   /**
    * Entry point into the program.
    *
    * @param args Command line arguments to the program. There must be exactly
    *             1 or 2 arguments. The first parameter specifies the port
    *             number. The second parameter, if present, is the size of the
    *             board.
    */
   public static void main(String[] args) {
      BattleServerDriver driver = new BattleServerDriver();
      driver.go(args);
   }

   /**
    * Controller that drives the server.
    *
    * @param args Command line arguments from main.
    */
   private void go(String[] args) {
      // Check command line arguments.
      this.validateArgs(args);
      int port = Integer.parseInt(args[PORT_ARG]);
      int gridSize = this.getGridSize(args);

      // Start server.
      this.makeServer(port, gridSize);
   }

   /**
    * Creates a BattleServer and listens for incoming connections.
    *
    * @param port     The server's port number.
    * @param gridSize The size of the grids.
    */
   private void makeServer(int port, int gridSize) {
      try {
         BattleServer server = new BattleServer(port, gridSize);
         server.listen();
      } catch (IOException ioe) {
         ioe.printStackTrace();
      }
   }

   /**
    * Ensures that the command line arguments are valid.
    *
    * @param args Command line arguments to the program.
    */
   private void validateArgs(String[] args) {
      final String PORT_ERR_MSG = "Invalid port number. Should be [1024-65535]";
      final String SIZE_ERR_MSG = "Invalid grid size. Should be [5-10]";
      if (args.length < MIN_ARGS || args.length > MAX_ARGS) {
         this.usage();
      }
      if (!this.validatePort(args[PORT_ARG])) {
         System.err.println(PORT_ERR_MSG);
         System.exit(1);
      }
      if (args.length == 2) {
         if (!this.validateSize(args[SIZE_ARG])) {
            System.err.println(SIZE_ERR_MSG);
            System.exit(1);
         }
      }
   }

   /**
    * Finds the appropriate grid size.
    *
    * @param args Command line arguments to the program.
    * @return The grid size.
    */
   private int getGridSize(String[] args) {
      final int DEFAULT_SIZE = 10;
      int gridSize;
      if (args.length == MAX_ARGS) {
         gridSize = Integer.parseInt(args[SIZE_ARG]);
      } else {
         gridSize = DEFAULT_SIZE;
      }
      return gridSize;
   }

   /**
    * Validates that a string passed in is an integer between 0 and 65535.
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
    * Validates that a string passed in is an integer between 5 and 10.
    *
    * @param sizeStr A string that should be a valid grid size.
    * @return True if the string passed is a valid grid size. False otherwise.
    */
   private boolean validateSize(String sizeStr) {
      final int MIN_SIZE = 5;
      final int MAX_SIZE = 10;
      int sizeNum;
      try {
         sizeNum = Integer.parseInt(sizeStr);
      } catch (NumberFormatException nfe) {
         return false;
      }
      return sizeNum >= MIN_SIZE && sizeNum <= MAX_SIZE;
   }

   /**
    * Prints a message indicating how the program should be used.
    */
   private void usage() {
      final String USG_MSG = "java server.BattleServerDriver <port> [grid size]";
      System.err.println(USG_MSG);
      System.exit(1);
   }

}
