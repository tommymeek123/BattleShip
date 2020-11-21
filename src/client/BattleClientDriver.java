package client;

import java.io.IOException;

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
            BattleClient bc = new BattleClient(hostname, port, username);
            bc.addMessageListener(outStream);
            bc.connect();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      else {
         System.out.println("You must supply a hostname, port number and username.");
      }
   }
}
