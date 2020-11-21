package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionAgent extends MessageSource implements Runnable {

   /** The socket used for communication with a remote host. */
   private Socket socket;

   /** Input stream to the agent. */
   private Scanner in;

   /** Output stream from the agent. */
   private PrintStream out;

   /** The thread in which this agent will run. */
   private Thread thread;

   private boolean joined = false;

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
      //this.thread = new Thread(this);
   }

   /**
    * Sends message across the network.
    *
    * @param message The message to be sent across the network.
    */
   public void sendMessage(String message) {
      if(message.contains("/join") && !joined) {
         this.out.println(message);
         joined = true;
      }
      else if(message.contains("/join"))
         this.out.println("Already in game");
      else if(message.contains("/quit")) {
         this.out.println(message);
         try {
            this.close();
            thread.interrupt();
         }
         catch (IOException ieo) {
            System.err.println(ieo.getMessage());
         }
      }
      else
         this.out.println(message);
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

//   @Override
//   public void run() {
//      this.thread = Thread.currentThread();
//      while (this.isConnected()) {
//         String command = null;
//         while (!this.thread.isInterrupted()) {
//            System.out.println("Hey there!");
//            command = in.next();
//            System.out.println("Hi there!");
//         }
//         this.notifyReceipt(command);
//      }
//   }

   @Override
   public void run() {
      int i = 0;
      this.thread = Thread.currentThread();
      while (!this.thread.isInterrupted()) {
         //System.out.println("Hey there!");
         if (in.hasNext()) {
            String command = in.nextLine();
            this.notifyReceipt(command);
         }
      }
   }
}
