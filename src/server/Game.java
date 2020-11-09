package server;

import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class implements the logic for a game of Battleship. Multiple players
 * may join and each will have their own grid.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version November, 2020
 */
public class Game {

   /** The size of the grids in this game. */
   private int size;

   /** The number of ships on each grid in this game. */
   private int numShips;

   /** True if the game is in progress. False otherwise. */
   private boolean inPlay;

   /** A list of grids. One for each player. */
   private List<Grid> grids;

   /** A list of active players in the game. */
   private List<String> players;

   /**
    * Constructor for the Game class
    *
    * @param size The size of the grids.
    */
   public Game(int size) {
      this.numShips = getNumShips(size);
      this.size = size;
      this.inPlay = false;
      this.grids = new ArrayList<>();
      this.players = new ArrayList<>();
   }

   /**
    * Determines the number of ships to place on each grid.
    *
    * @param size The size of the grids.
    * @return the number of ships on each grid.
    */
   private int getNumShips(int size) {
      Random rand = new Random();
      int numShips = 0;
      switch (size) {
         case 5:
            numShips = rand.nextInt(2) + 1; // 1-2
            break;
         case 6:
         case 7:
            numShips = rand.nextInt(2) + 2; // 2-3
            break;
         case 8:
         case 9:
            numShips = rand.nextInt(3) + 3; // 3-5
            break;
         case 10:
            numShips = rand.nextInt(3) + 4; // 4-6
            break;
      }
      return numShips;
   }

   /**
    * Adds a new player to this game and creates a new grid for that player.
    *
    * @param player The new player's name.
    */
   public String addPlayer(String player) {
      String response;
      if (this.inPlay) {
         response = "Game already in progress";
      } else {
         this.grids.add(new Grid(this.size, this.numShips));
         this.players.add(player);
         response = "!!! " + player + " has joined";
      }
      return response;
   }

   /**
    * Gets the player's name at the specified position.
    *
    * @param playerNum Which player to return.
    * @return The player at the specified position.
    */
   public String getPlayerAt(int playerNum) {
      return this.players.get(playerNum);
   }

   private String play() {
      String response;
      if (this.inPlay) {
         response = "Game already in progress";
      } else if (this.players.size() < 2) {
         response = "Not enough players to play the game";
      } else {
         this.inPlay = true;
         response = "The game begins";
      }
      return response;
   }

   public String execute(String command) {
      String[] commands = command.split(" ");
      String response = "";
      if (this.validateCommand(commands)) {
         switch (commands[0]) {
            case "/join":
               response = this.addPlayer(commands[1]);
               break;
            case "/play":
               response = this.play();
               break;
            case "/attack":
               int current = 0;
               current = (current + 1) % this.players.size();
               break;
            case "/quit":
               break;
            case "/show":
               break;
         }
      } else {
         response = "Invalid command: " + command;
      }
      return response;
   }

   private boolean validateCommand(String[] commands) {
      boolean isValid = false;
      if(commands[0].equals("/join") && commands.length == 2) {
         isValid = true;
      }
      else if(commands[0].equals("/play") && commands.length == 1) {
         isValid = true;
      }
      else if(commands[0].equals("/attack") && commands.length == 4) {
         isValid = this.validateAttack(commands);
      }
      else if(commands[0].equals("/quit") && commands.length == 1) {
         isValid = true;
      }
      else if(commands[0].equals("/show") && commands.length == 2) {
         isValid = this.validateShow(commands);
      }
      //throw new UnsupportedOperationException("not working yet");
      return isValid;
   }

   private boolean validateAttack(String[] commands) {
      boolean isValid = false;
      try {
         int i = Integer.parseInt(commands[2]);
         int j = Integer.parseInt(commands[3]);
         if(i >= 0 && i < this.size && j >= 0 && j < this.size && this.players.contains(commands[1]))
            isValid = true;
      }
      catch (NumberFormatException nfe) {
         //Do something?
      }
      return isValid;
   }

   private boolean validateShow(String[] commands) {
      boolean isValid = false;
      if(this.players.contains(commands[1]))
         isValid = true;
      return isValid;
   }









   /**
    * Determines if the supplied String is the name of a current player.
    *
    * @param player The player name we are checking.
    * @return True if the supplied name is a current player. False otherwise.
    */
   public boolean isPlayer(String player) {
      return this.players.contains(player);
   }

   /**
    * Accessor for the number of players currently in the game.
    *
    * @return The number of players currently in the game.
    */
   public int numPlayers() {
      return this.players.size();
   }

}
