package server;

public class BattleShipDriver {

   /**
    * Entry point into the program.
    *
    * @param args Command line arguments to the program. There must be exactly
    *             1 or 2 arguments. The first parameter specifies the port
    *             number. The second parameter, if present, is the size of the
    *             board.
    */
   public static void main(String[] args) {
//      int num = 0;
//      try {
//         //num = Integer.parseInt(args[0]);
//         Game game = new Game(4);
//
//      }
//      catch (NumberFormatException nfe) {
//         System.out.println("Error");
//      }
      go(args);
   }

   /**
    * Controller that drives the server.
    *
    * @param args Command line arguments from main.
    */
   private static void go(String[] args) {
      // validateArgs(args);
      int boardSize = Integer.parseInt(args[0]);
      Game game = new Game(boardSize);
   }

   /**
    * Ensures that the command line arguments are valid.
    *
    * @param args Command line arguments to the program.
    */
   private static void validateArgs(String[] args) {
      if (args.length == 0 || args.length > 2) {
         usage();
      }
      if (!validatePort(args[0])) {
         System.err.println("Invalid port number.");
         System.exit(1);
      }
      if (args.length == 2) {
         if (!validateSize(args[1])) {
            System.err.println("Invalid grid size.");
            System.exit(1);
         }
      }
   }

   /**
    * Prints a message indicating how the program should be used.
    */
   private static void usage() {
      System.err.println("java server.BattleShipDriver <port> [grid size]");
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
      int portNum;
      try {
         portNum = Integer.parseInt(portStr);
      } catch (NumberFormatException nfe) {
         return false;
      }
      return portNum >= 0 && portNum <= MAX_PORT;
   }

   /**
    * Validates that a string passed in is an integer between 5 and 10.
    *
    * @param sizeStr A string that should be a valid grid size.
    * @return True if the string passed is a valid grid size. False otherwise.
    */
   private static boolean validateSize(String sizeStr) {
      int sizeNum;
      try {
         sizeNum = Integer.parseInt(sizeStr);
      } catch (NumberFormatException nfe) {
         return false;
      }
      return sizeNum >= 5 && sizeNum <= 10;
   }

}
