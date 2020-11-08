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

   /** The game of Battleship */
   private Game game;

   /**
    * Constructor for a BattleServer
    *
    * @param port The server's port number.
    * @param gridSize The size of the grids.
    * @throws IOException if something goes wrong creating the ServerSocket.
    */
   public BattleServer(int port, int gridSize) throws IOException {
      //this.serverSocket = new ServerSocket(port);
      this.game = new Game(gridSize);
   }

   public void play() {
      this.game.go();
   }

   /**
    * Adds a new player to the Battleship Game.
    *
    * @param player The new player's name.
    */
   public void addPlayer(String player) {
      this.game.addPlayer(player);
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
