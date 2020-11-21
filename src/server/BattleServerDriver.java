package server;

import java.io.IOException;

public class BattleServerDriver {

   /** The index of the command line argument specifying the port number. */
   final static int PORT_ARG = 0;

   /** The index of the command line argument specifying the grid size. */
   final static int SIZE_ARG = 1;

   /** The minimum number of command line arguments. */
   final static int MIN_ARGS = 1;

   /** The maximum number of command line arguments. */
   final static int MAX_ARGS = 2;

   /**
    * Entry point into the program.
    *
    * @param args Command line arguments to the program. There must be exactly
    *             1 or 2 arguments. The first parameter specifies the port
    *             number. The second parameter, if present, is the size of the
    *             board.
    */
   public static void main(String[] args) {
      go(args);
   }

   /**
    * Controller that drives the server.
    *
    * @param args Command line arguments from main.
    */
   private static void go(String[] args) {
      final int DEFAULT_SIZE = 10;
      validateArgs(args);
      int gridSize = DEFAULT_SIZE;
      int port = Integer.parseInt(args[PORT_ARG]);
      if (args.length == MAX_ARGS) {
         gridSize = Integer.parseInt(args[SIZE_ARG]);
      }
      try {
         BattleServer server = new BattleServer(port, gridSize);
         server.listen();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * Ensures that the command line arguments are valid.
    *
    * @param args Command line arguments to the program.
    */
   private static void validateArgs(String[] args) {
      final int PORT_ARG = 0;
      final int SIZE_ARG = 1;
      final String PORT_ERR_MSG = "Invalid port number. Should be [1024-65535]";
      final String SIZE_ERR_MSG = "Invalid grid size. Should be [5-10]";
      if (args.length < MIN_ARGS || args.length > MAX_ARGS) {
         usage();
      }
      if (!validatePort(args[PORT_ARG])) {
         System.err.println(PORT_ERR_MSG);
         System.exit(1);
      }
      if (args.length == 2) {
         if (!validateSize(args[SIZE_ARG])) {
            System.err.println(SIZE_ERR_MSG);
            System.exit(1);
         }
      }
   }

   /**
    * Prints a message indicating how the program should be used.
    */
   private static void usage() {
      final String USG_MSG = "java server.BattleServerDriver <port> [grid size]";
      System.err.println(USG_MSG);
      System.exit(1);
   }

   /**
    * Validates that a string passed in is an integer between 0 and 65535.
    *
    * @param portStr A string that should be a valid port number.
    * @return True if the string passed is a valid port number. False otherwise.
    */
   private static boolean validatePort(String portStr) {
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
   private static boolean validateSize(String sizeStr) {
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

}
