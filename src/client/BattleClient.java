package client;

import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class BattleClient extends MessageSource implements MessageListener {

   /* The server that the client is trying to connect to */
   private InetAddress host;
   /* The port the client is using */
   private int port;
   /* The username of the client */
   private String username;
   //ConnectionAgent ca;

   /**
    * Constructor for a BattleClient
    * @param hostname the server the client is trying to connect to
    * @param port the port used to try to connect to the server
    * @param username the username of the client player
    */
   public BattleClient(String hostname, int port, String username) {
      try {
         this.host = InetAddress.getByName(hostname);
      }
      catch(UnknownHostException uhe) {
         System.err.println(uhe.getMessage());
      }
      this.port = port;
      this.username = username;


   }

   /**
    *
    */
   public void connect() {
      try {
         Socket socket = new Socket(host, port);
         ConnectionAgent ca = new ConnectionAgent(socket);

         while(ca.isConnected()) {

         }

      }
      catch (IOException ieo) {
         System.out.println("Error");
      }

   }

   /**
    * Used to notify observers that the subject has received a message.
    * @param message The message received by the subject
    * @param source  The source from which this message originated (if needed).
    */
   public void messageReceived(String message, MessageSource source) {
      System.out.println(message);
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
      ca.sendMessage(message);
   }

   /**
    * Used to get the username
    * @return the username
    */
   public String getUsername() {
      return this.username;
   }
}
