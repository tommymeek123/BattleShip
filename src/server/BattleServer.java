package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server to host a game of Battleship.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version December, 2020
 */
public class BattleServer implements MessageListener {

   /** The ServerSocket used to communicate with a client. */
   private ServerSocket serverSocket;

   /** Whose turn it is. */
   private int current;

   /** The game of Battleship */
   private Game game;

   /** The list of active connection agents. */
   private ArrayList<ConnectionAgent> agents;

   /**
    * Constructor for a BattleServer
    *
    * @param port     The server's port number.
    * @param gridSize The size of the grids.
    * @throws IOException if something goes wrong creating the ServerSocket.
    */
   public BattleServer(int port, int gridSize) throws IOException {
      this.serverSocket = new ServerSocket(port);
      this.current = 0;
      this.game = new Game(gridSize);
      this.agents = new ArrayList<>();
   }

   /**
    * Causes the BattleServer to listen for requests.
    *
    * @throws IOException if there is a problem with the socket.
    */
   public void listen() throws IOException {
      while (!this.serverSocket.isClosed()) {
         Socket socket = this.serverSocket.accept();
         ConnectionAgent agent = new ConnectionAgent(socket);
         Thread thread = new Thread(agent);
         agent.addMessageListener(this);
         this.agents.add(agent);
         thread.start();
      }
   }

   /**
    * Send a message to all the clients connected to this server.
    *
    * @param message The message to send out.
    */
   public void broadcast(String message) {
      for (ConnectionAgent agent : this.agents) {
         agent.sendMessage(message);
      }
   }

   /**
    * Attempts to execute the given command.
    *
    * @param message The command received from the client.
    * @param source The connection agent through which the command was received.
    */
   public void messageReceived(String message, MessageSource source) {
      String sender = "";
      if (!message.contains("/join")) { // Prevents index out of bounds error
         // Find the name of the player who sent the message.
         for (int i = 0; i < this.game.getNumPlayers(); i++) {
            if (this.agents.get(i) == source) {
               sender = this.game.getPlayerAt(i);
            }
         }
      }

      String[] result = this.game.execute(message, sender);

      // Remove client if appropriate.
      if (result[this.game.QUIT_INDEX].equals(this.game.REMOVE)) {
         this.sourceClosed(source);
      }

      if (result[this.game.PRIVATE_INDEX].equals(this.game.PRIVATE)) {
         // Send response only to the source of the command.
         for (ConnectionAgent agent : this.agents) {
            if (agent == source) {
               agent.sendMessage(result[this.game.MSG_INDEX]);
            }
         }
      } else {
         // Send response to all players.
         this.broadcast(result[this.game.MSG_INDEX]);
      }

      // Eliminate player if appropriate.
      if (!result[this.game.ELIMINATE_INDEX].equals(this.game.NOBODY)) {
         this.moveToEndOfList(result);
      }
   }

   /**
    * Move connection agent of the eliminated player to end of the list.
    *
    * @param result The result of the execute method.
    */
   private void moveToEndOfList(String[] result) {
      int loserIndex = Integer.parseInt(result[this.game.ELIMINATE_INDEX]);
      ConnectionAgent losersAgent = this.agents.get(loserIndex);
      this.agents.remove(losersAgent);
      this.agents.add(losersAgent);
   }

   /**
    * Close the connection to a client.
    *
    * @param source The connection agent to close.
    */
   public void sourceClosed(MessageSource source) {
      for (ConnectionAgent agent : this.agents) {
         if (agent == source) {
            try {
               agent.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      source.removeMessageListener(this);
      this.agents.remove(source);
   }

}
