package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
   List<ConnectionAgent> agents;

   /**
    * Constructor for a BattleServer
    *
    * @param port The server's port number.
    * @param gridSize The size of the grids.
    * @throws IOException if something goes wrong creating the ServerSocket.
    */
   public BattleServer(int port, int gridSize) throws IOException {
      this.serverSocket = new ServerSocket(port);
      this.current = 0;
      this.game = new Game(gridSize);
      this.agents = new ArrayList<>();
   }

   public void listen() throws IOException {
      while (!this.serverSocket.isClosed()) {
         //System.out.println("listening");
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

      String result = this.game.execute(message);
      if (result.contains("Invalid command")) {
         for (ConnectionAgent agent : this.agents) {
            if (agent == source) {
               agent.sendMessage(result);
            }
         }
      }
      else {
         this.broadcast(result);
      }
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
