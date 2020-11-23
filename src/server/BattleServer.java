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
    * @param source  The connection agent through which the command was received.
    */
   public void messageReceived(String message, MessageSource source) {
      String sender = "";
      if (!message.contains("/join")) {
         for (int i = 0; i < this.game.getNumPlayers(); i++) {
            if (this.agents.get(i) == source) {
               sender = this.game.getPlayerAt(i);
            }
         }
      }

      String result = this.game.execute(message, sender);
      if (this.isPrivate(result, message)) {
         for (ConnectionAgent agent : this.agents) {
            if (agent == source) {
               agent.sendMessage(result);
            }
         }
      } else {
         this.broadcast(result);
      }
   }

   /**
    * Determines if the response from a command execution is public or private.
    *
    * @param result The response from a command execution.
    * @param message The command supplied by the client.
    * @return True if the response is private. False if it is public.
    */
   private boolean isPrivate(String result, String message) {
      return result.contains("Invalid command") || message.contains("/show")
              || result.contains("Move Failed")
              || result.contains("Play not in progress")
              || result.contains("Game already in progress");
   }

   /**
    * Close the connection to a client.
    *
    * @param source The connection agent to close.
    */
   public void sourceClosed(MessageSource source) {
      source.removeMessageListener(this);
      this.agents.remove(source);
   }

}
