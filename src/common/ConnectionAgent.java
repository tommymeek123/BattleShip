package common;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionAgent extends MessageSource implements Runnable {

   /** TODO: comment */
   private Socket socket;

   /** TODO: comment */
   private Scanner in;

   /** TODO: comment */
   private PrintStream out;

   /** TODO: comment */
   private Thread thread;

   public ConnectionAgent(Socket socket) {
      this.socket = socket;
   }

   public void sendMessage(String message) {
      throw new UnsupportedOperationException("not there yet");
   }

   public boolean isConnected() {
      throw new UnsupportedOperationException("not there yet");
   }

   public void close() {
      throw new UnsupportedOperationException("not there yet");
   }

   @Override
   public void run() {
      throw new UnsupportedOperationException("not there yet");
   }
}
