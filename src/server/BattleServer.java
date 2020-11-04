package server;

import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.ServerSocket;

public class BattleServer implements MessageListener {

   /** The ServerSocket used to communicate with a client. */
   private ServerSocket serverSocket;

   /**  */
   private int current;

   /**  */
   private Game game;

   /**
    * Constructor for a BattleServer
    *
    * @param port The server's port number.
    * @throws IOException if something goes wrong creating the ServerSocket.
    */
   public BattleServer(int port) throws IOException {
      this.serverSocket = new ServerSocket(port);
   }

   public void listen() {

   }

   public void broadcast(String message) {

   }

   /**
    * Used to notify observers that the subject has received a message.
    *
    * @param message The message received by the subject
    * @param source  The source from which this message originated (if needed).
    */
   public void messageReceived(String message, MessageSource source) {

   }

   /**
    * Used to notify observers that the subject will not receive new messages.
    * observers can deregister themselves.
    *
    * @param source The MessageSource that does not expect more messages.
    */
   public void sourceClosed(MessageSource source) {

   }
}
