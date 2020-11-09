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

   /** Whose turn it is. */
   private int current;

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
      this.current = 0;
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
    * Executes a command according to what the command is.
    *
    * @param command The command entered by a user.
    * @return The response indicating the result of the execution.
    */
   public String execute(String command) {
      String[] commands = command.split(" ");
      String response = "";
      if (this.validateCommand(commands)) {
         switch (commands[0]) {
            case "/join":
               response = this.join(commands[1]);
               break;
            case "/play":
               response = this.play();
               break;
            case "/attack":
               int x = Integer.parseInt(commands[2]);
               int y = Integer.parseInt(commands[3]);
               response = this.attack(commands[1], x, y);
               break;
            case "/quit":
               response = this.quit();
               break;
            case "/show":
               response = this.show(commands[1]);
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
    * Adds a new player to this game and creates a new grid for that player.
    *
    * @param player The new player's name.
    * @return A string indicating what happened as a result of the command.
    */
   public String join(String player) {
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
    * Initiates a game of Battleship.
    *
    * @return A string indicating what happened as a result of the command.
    */
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

   /**
    * Launch an attack against a location in another player's grid.
    *
    * @param victim The player against whom the attack is directed.
    * @param x The x-coordinate of the location to be attacked.
    * @param y The y-coordinate of the location to be attacked.
    * @return A string indicating what happened as a result of the attack.
    */
   private String attack(String victim, int x, int y) {
      String response;
      if (this.inPlay) {
         String attacker = this.players.get(this.current);
         if (attacker.equals(victim)) {
            response = "Move Failed, player turn: " + attacker;
         } else {
            Grid victimsGrid = this.getGridByPlayerName(victim);
            victimsGrid.shotsFired(x, y);
            response = "Shots Fired at " + victim + " by " + attacker;
            if (victimsGrid.getTotalSquares() == 0) {
               this.players.remove(victim);
               this.grids.remove(victimsGrid);
               response += "\n" + victim + " has been eliminated!";
               if (this.players.size() == 1) {
                  response += "\nGAME OVER: " + attacker + " wins!";
                  this.inPlay = false;
               }
            }
            this.current = (this.current + 1) % this.players.size();
            if (this.inPlay) {
               String newPlayer = this.players.get(this.current);
               response += "\n" + newPlayer + " it is your turn";
            }
         }
      } else {
         response = "Play not in progress";
      }
      return response;
   }

   /**
    * Forfeit the game.
    *
    * @return An indication that the player surrendered.
    */
   private String quit() {
      String response;
      String currentPlayer = this.players.get(this.current);
      Grid currentGrid = this.grids.get(this.current);
      this.players.remove(currentPlayer);
      this.grids.remove(currentGrid);
      response = "!!! " + currentPlayer + " surrendered";
      if (this.players.size() == 1) {
         response += "\nGAME OVER: " + this.players.get(0) + " wins!";
         this.inPlay = false;
      }
      this.current = (this.current + 1) % this.players.size();
      if (this.inPlay) {
         String newPlayer = this.players.get(this.current);
         response += "\n" + newPlayer + " it is your turn";
      }
      return response;
   }

   /**
    * Display a player's current grid.
    *
    * @param player The player whose grid we want to show.
    * @return A string representation of a player's grid.
    */
   private String show(String player) {
      String response;
      if (this.inPlay) {
         String currentPlayer = this.players.get(this.current);
         if (currentPlayer.equals(player)) {
            response = this.getGridByPlayerName(player).drawSelf();
         } else {
            response = this.getGridByPlayerName(player).drawOpponent();
         }
      } else {
         response = "Play not in progress";
      }
      return response;
   }

   /**
    * Finds the grid belonging to the specified player.
    *
    * @param player The name of the player whose grid we want.
    * @return The grid belonging to that player.
    */
   private Grid getGridByPlayerName(String player) {
      return this.grids.get(this.players.indexOf(player));
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
