package server;

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
   public void addPlayer(String player) {
      if (this.inPlay) {
         System.out.println("Game already in progress");
      } else {
         this.grids.add(new Grid(this.size, this.numShips));
         this.players.add(player);
         System.out.println("!!! " + player + " has joined");
      }
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

   private void play() {
      if (this.inPlay) {
         System.out.println("Game already in progress");
      } else {
         this.inPlay = true;
         System.out.println("The game begins");
      }
   }

   public void execute(String[] commands) {
      switch (commands[0]) {
         case "/join":
            this.addPlayer(commands[1]);
            break;
         case "/play":
            this.play();
            break;
         case "/attack":
            break;
         case "/quit":
            break;
         case "/show":
            break;
      }
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

   /**
    * Starts the game.
    */
   public void go() {
      for (Grid g : this.grids) {
         g.drawSelf();
      }
   }

}
