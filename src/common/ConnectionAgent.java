package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * A ConnectionAgent facilitates communication between a client and a server
 * using the observer pattern. The ConnectionAgent acts as 'subject' while
 * the client and server each act as 'observers'.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version December, 2020
 */
public class ConnectionAgent extends MessageSource implements Runnable {

   /** The socket used for communication with a remote host. */
   private Socket socket;

   /** Input stream to the agent. */
   private Scanner in;

   /** Output stream from the agent. */
   private PrintStream out;

   /** The thread in which this agent will run. */
   private Thread thread;

//   /** The username that this connection agent is associated with */
//   private String username;
//
//   /** True of this agent has joined a live game. False otherwise. */
//   private boolean joined;

   /**
    * Constructor.
    *
    * @param socket The socket through which to communicate with a remote host.
    * @throws IOException if something goes wrong creating the socket.
    */
   public ConnectionAgent(Socket socket) throws IOException {
      this.socket = socket;
      this.out = new PrintStream(this.socket.getOutputStream());
      this.in = new Scanner(this.socket.getInputStream());
      //this.joined = false;
   }

   /**
    * Sends message across the network.
    *
    * @param message The message to be sent across the network.
    */
   public void sendMessage(String message) {
      this.out.println(message);
//      if (message.contains("/join") && !joined) {
//         this.out.println(message);
//         this.username = message.substring(6);
//         joined = true;
//      } else if (message.contains("/join")) {
//         this.out.println("Already in game");
//      } else if (message.contains("/quit")) {
//         this.out.println(message);
//         try {
//            this.close();
//            //thread.interrupt();
//         } catch (IOException ieo) {
//            System.err.println(ieo.getMessage());
//         }
//      } else {
//         this.out.println(message);
//      }
   }

   /**
    * Indicates whether this agent is connected to a remote host.
    *
    * @return True if we are connected. False otherwise.
    */
   public boolean isConnected() {
      return this.socket.isConnected();
   }

   /**
    * Closes this connection.
    *
    * @throws IOException if an error occurs while closing the socket.
    */
   public void close() throws IOException {
      this.socket.close();
   }

   /**
    * This agent will listen for messages from it's counterpart on a remote
    * host. When another agent whose socket is connected to this one's calls
    * its sendMessage method, that message will be received here. Once the
    * message is received, all observers will be notified.
    */
   @Override
   public void run() {
      this.thread = Thread.currentThread();
      while (!this.thread.isInterrupted() && in.hasNextLine()) {
         String command = in.nextLine();
         this.notifyReceipt(command);
      }
   }

}
