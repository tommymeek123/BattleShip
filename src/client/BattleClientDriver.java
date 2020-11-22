package client;

import java.io.IOException;
import java.util.Scanner;

/**
 * A driver to start a Battleship client
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version December, 2020
 */
public class BattleClientDriver {

   /**
    * Main method that creates a BattleClient and handles main startup process.
    * @param args the args required to being a connection.
    */
   public static void main(String[] args){
      if(args.length == 3) {
         String hostname = args[0];
         int port = 0;
         try {
            port = Integer.parseInt(args[1]);
         }
         catch (NumberFormatException nfe) {
            System.out.println("Port must be a number");
            System.exit(1);
         }
         String username = args[2];

         var outStream = new PrintStreamMessageListener(System.out);
         try {
            boolean keepPlaying = true;
            BattleClient bc = new BattleClient(hostname, port, username);
            bc.connect();
            bc.addMessageListener(outStream);
            bc.send("/join " + username);
            while (keepPlaying) { //TODO: fix
               String command = getInput();
               //System.out.println(command);
               bc.send(command);
               if(command.contains("/quit"))
                  keepPlaying = false;
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      else {
         System.out.println("You must supply a hostname, port number and username.");
      }
   }

   public static String getInput() {
      Scanner in = new Scanner(System.in);
      String command = null;
      command = in.nextLine();
      return command;
   }

}
