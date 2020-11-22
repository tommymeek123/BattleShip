package client;

import common.MessageListener;
import common.MessageSource;

import java.io.PrintStream;

/**
 * A view class to direct output from a client to an output stream.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version December, 2020
 */
public class PrintStreamMessageListener implements MessageListener {

   /** The output stream to which to print the messages. */
   private PrintStream out;

   /**
    * Constructor for a PrintStreamMessageListener.
    *
    * @param out The output stream to use.
    */
   public PrintStreamMessageListener(PrintStream out) {
      this.out = out;
   }

   /**
    * Prints the received message to the appropriate output stream.
    *
    * @param message The message received from the client.
    * @param source  The client from which this message originated (if needed).
    */
   public void messageReceived(String message, MessageSource source) {
      this.out.println(message);
   }

   /**
    * Terminates the connection between this and the client it is observing.
    *
    * @param source The client that does not expect more messages.
    */
   public void sourceClosed(MessageSource source) {
      source.removeMessageListener(this);
   }
}
