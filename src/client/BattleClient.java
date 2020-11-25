package client;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A client to play a game of Battleship.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version December, 2020
 */
public class BattleClient extends MessageSource implements MessageListener {

   /** The server that the client is trying to connect to */
   private InetAddress host;

   /** The port the client is using */
   private int port;

   /** The username of the client */
   private String username;

   /** Tells is the client is ready to connect to the server */
   private boolean ready;

   /** Connection agent used to communicate with the sever. */
   private ConnectionAgent agent;

   /**
    * Constructor for a BattleClient
    *
    * @param hostname the server the client is trying to connect to
    * @param port the port used to try to connect to the server
    * @param username the username of the client player
    */
   public BattleClient(InetAddress hostname, int port, String username) {
      this.host = hostname;
      this.port = port;
      this.username = username;
      this.ready = true;
   }

   /**
    * Connects this client to a server by creating a connection agent.
    *
    * @throws IOException if something goes wrong in the connection process.
    */
   public void connect() throws IOException {
      Socket socket = new Socket(this.host, this.port);
      this.agent = new ConnectionAgent(socket);
      Thread thread = new Thread(this.agent);
      this.agent.addMessageListener(this);
      thread.start();
      this.send("/join " + this.username);
   }

   /**
    * Used to notify observers that the subject has received a message.
    * @param message The message received by the subject
    * @param source  The source from which this message originated (if needed).
    */
   public void messageReceived(String message, MessageSource source) {
      if (message.contains("already in the game")
              || message.contains("Game already in progress")) {
         this.ready = false;
      }
      this.notifyReceipt(message);
   }

   /**
    * Used to notify observers that the subject will not receive new messages;
    * observers can deregister themselves.
    * @param source the MessageSource object that does not expect more messages.
    */
   public void sourceClosed(MessageSource source) {
      source.removeMessageListener(this);
   }

   /**
    * Used to send messages through the ConnectionAgent
    * @param message the message being sent
    */
   public void send(String message) {
      this.agent.sendMessage(message);
   }

   /**
    * Gets the isReady field
    * @return true if isReady, false otherwise
    */
   public boolean getReady() {
      return this.ready;
   }

}
