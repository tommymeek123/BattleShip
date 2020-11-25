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

   /** The number of responses returned by the execute method. */
   final int NUM_RESPONSES = 4;

   /** The index that indicates if the sender should be removed. */
   final int QUIT_INDEX = 0;

   /** The index that indicates if the response should be global or private. */
   final int PRIVATE_INDEX = 1;

   /** The index that contains the contents of the response message. */
   final int MSG_INDEX = 2;

   /** The index that indicates if a player was eliminated. */
   final int ELIMINATE_INDEX = 3;

   /** Flag indicating the response message is private. */
   final String PRIVATE = "p";

   /** Flag indicating the response message is global. */
   final String GLOBAL = "g";

   /** Flag indicating the sender should be removed from the server. */
   final String REMOVE = "r";

   /** Flag indicating the sender should not be removed from the server. */
   final String STAY = "";

   /** Flag indicating no player was eliminated. */
   final String NOBODY = "";

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
    * @param sender The player who sent the command.
    * @return An array of Strings. Index 0 is a flag indicating if the user
    *         that sent the message should be removed. "r" means remove them.
    *         Empty string means do not. Index 1 is a flag indicating if the
    *         response is global or private. "g" means global. "p" means
    *         private. Index 2 is the response indicating the result of the
    *         execution. Index 3 names a player who may have been eliminated.
    */
   public String[] execute(String command, String sender) {
      String[] commands = command.split(" ");
      String[] response = new String[NUM_RESPONSES];
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
               response = this.attack(commands[1], sender, x, y);
               break;
            case "/quit":
               response = this.quit(sender);
               break;
            case "/show":
               response = this.show(commands[1], sender);
               break;
         }
      } else {
         response[QUIT_INDEX] = STAY;
         response[PRIVATE_INDEX] = PRIVATE;
         response[MSG_INDEX] = "Invalid command: " + command;
         response[ELIMINATE_INDEX] = NOBODY;
      }
      return response;
   }

   /**
    * Adds a new player to this game and creates a new grid for that player.
    *
    * @param player The new player's name.
    * @return A string indicating what happened as a result of the command.
    */
   public String[] join(String player) {
      String[] response = new String[NUM_RESPONSES];
      response[ELIMINATE_INDEX] = NOBODY;
      if (this.players.contains(player)) {
         response[QUIT_INDEX] = REMOVE;
         response[PRIVATE_INDEX] = PRIVATE;
         response[MSG_INDEX] = player + " is already in the game.";
      } else if (this.inPlay) {
         response[QUIT_INDEX] = REMOVE;
         response[PRIVATE_INDEX] = PRIVATE;
         response[MSG_INDEX] = "Game already in progress";
      } else {
         this.grids.add(new Grid(this.size, this.numShips));
         this.players.add(player);
         response[QUIT_INDEX] = STAY;
         response[PRIVATE_INDEX] = GLOBAL;
         response[MSG_INDEX] = "!!! " + player + " has joined";
      }
      return response;
   }

   /**
    * Initiates a game of Battleship.
    *
    * @return A string indicating what happened as a result of the command.
    */
   private String[] play() {
      String[] response = new String[NUM_RESPONSES];
      response[QUIT_INDEX] = STAY;
      response[ELIMINATE_INDEX] = NOBODY;
      if (this.inPlay) {
         response[PRIVATE_INDEX] = PRIVATE;
         response[MSG_INDEX] = "Game already in progress";
      } else if (this.players.size() < 2) {
         response[PRIVATE_INDEX] = PRIVATE;
         response[MSG_INDEX] = "Not enough players to play the game";
      } else {
         this.inPlay = true;
         response[PRIVATE_INDEX] = GLOBAL;
         response[MSG_INDEX] = "The game begins\n" + this.players.get(0) +
                               " it is your turn";
      }
      return response;
   }

   /**
    * Launch an attack against a location in another player's grid.
    *
    * @param victim The player against whom the attack is directed.
    * @param sender The player who sent the command.
    * @param x The x-coordinate of the location to be attacked.
    * @param y The y-coordinate of the location to be attacked.
    * @return A string indicating what happened as a result of the attack.
    */
   private String[] attack(String victim, String sender, int x, int y) {
      String[] response = new String[NUM_RESPONSES];
      response[QUIT_INDEX] = STAY;
      String attacker = this.players.get(this.current);
      response[ELIMINATE_INDEX] = NOBODY;
      if (!this.inPlay) {
         response[PRIVATE_INDEX] = PRIVATE;
         response[MSG_INDEX] = "Play not in progress";
         response[ELIMINATE_INDEX] = NOBODY;
      } else if (!attacker.equals(sender) || sender.equals(victim)) {
         response[PRIVATE_INDEX] = PRIVATE;
         response[MSG_INDEX] = "Move Failed, player turn: " + attacker;
         response[ELIMINATE_INDEX] = NOBODY;
      } else {
         Grid victimsGrid = this.getGridByPlayerName(victim);
         victimsGrid.shotsFired(x, y);
         response[PRIVATE_INDEX] = GLOBAL;
         response[MSG_INDEX] = "Shots Fired at " + victim + " by " + attacker;
         if (victimsGrid.getTotalSquares() == 0) {
            int eliminatedIndex = this.getPlayerIndexByName(victim);
            response[ELIMINATE_INDEX] = Integer.toString(eliminatedIndex);
            response[MSG_INDEX] += "\n" + victim + " has been eliminated!";
            response[MSG_INDEX] += this.checkForEndGame(victim);
         }
         this.current = (this.current + 1) % this.players.size();
         if (this.inPlay) {
            String newPlayer = this.players.get(this.current);
            response[MSG_INDEX] += "\n" + newPlayer + " it is your turn";
         }
      }
      return response;
   }

   /**
    * Forfeit the game.
    *
    * @param quitter The player who sent the command.
    * @return An indication that the player surrendered.
    */
   private String[] quit(String quitter) {
      String[] response = new String[NUM_RESPONSES];
      response[QUIT_INDEX] = REMOVE;
      response[PRIVATE_INDEX] = PRIVATE;
      response[ELIMINATE_INDEX] = NOBODY;
      response[MSG_INDEX] = NOBODY;
      if (this.players.contains(quitter)) {
         response[MSG_INDEX] += "!!! " + quitter + " surrendered";
         String currentPlayer = this.players.get(this.current);
         response[PRIVATE_INDEX] = GLOBAL;
         if (this.inPlay) {
            response[MSG_INDEX] += this.checkForEndGame(quitter);
            if (currentPlayer.equals(quitter)) {
               this.current++;
               this.current = Math.floorMod(this.current, this.players.size());
            }
            if (inPlay) {
               String newPlayer = this.players.get(this.current);
               response[MSG_INDEX] += "\n" + newPlayer + " it is your turn";
            }
         }
      }
      return response;
   }

   /**
    * Display a player's current grid.
    *
    * @param player The player whose grid we want to show.
    * @param sender The player who sent the command.
    * @return A string representation of a player's grid.
    */
   private String[] show(String player, String sender) {
      String[] response = new String[NUM_RESPONSES];
      response[QUIT_INDEX] = STAY;
      response[PRIVATE_INDEX] = PRIVATE;
      response[ELIMINATE_INDEX] = NOBODY;
      if (this.inPlay) {
         if (sender.equals(player)) {
            response[MSG_INDEX] = this.getGridByPlayerName(player).drawSelf();
         } else {
            response[MSG_INDEX]=this.getGridByPlayerName(player).drawOpponent();
         }
      } else {
         response[MSG_INDEX] = "Play not in progress";
      }
      return response;
   }

   /**
    * Determines if the game is at an end. If so, it returns an appropriate
    * message indicating the winner.
    *
    * @param loser A player who recently left the game.
    * @return A string that may indicate a winner. If not, an empty string.
    */
   private String checkForEndGame(String loser) {
      String result = "";
      int loserIndex = this.getPlayerIndexByName(loser);
      this.grids.remove(this.getGridByPlayerName(loser));
      this.players.remove(loser);
      if (this.players.size() == 1) {
         result = "\nGAME OVER: " + this.players.get(0) + " wins!";
         this.inPlay = false;
      } else {
         if (loserIndex <= this.current) {
            this.current--; // prevent bug where next player is skipped
            this.current = Math.floorMod(this.current, this.players.size());
         }
      }
      return result;
   }

   /**
    * Ensures that a given command is valid.
    *
    * @param commands A command as an array of Strings.
    * @return True if the command is valid. False otherwise.
    */
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

   /**
    * Ensures that an /attack command is in a valid format.
    *
    * @param commands An /attack command.
    * @return True if the /attack command is valid. False otherwise.
    */
   private boolean validateAttack(String[] commands) {
      boolean isValid = false;
      try {
         int i = Integer.parseInt(commands[2]);
         int j = Integer.parseInt(commands[3]);
         if (i >= 0 && i < this.size && j >= 0 && j < this.size &&
                 this.players.contains(commands[1]))
            isValid = true;
      }
      catch (NumberFormatException nfe) {
         return false;
      }
      return isValid;
   }

   /**
    * Ensures that a /show command is in a valid format.
    *
    * @param commands A /show command.
    * @return True if the /show command is valid. False otherwise.
    */
   private boolean validateShow(String[] commands) {
      boolean isValid = false;
      if(this.players.contains(commands[1]))
         isValid = true;
      return isValid;
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
    * Finds the turn order of the player whose name is supplied.
    *
    * @param player The name of the player whose index we want.
    * @return The turn order of that player.
    */
   public int getPlayerIndexByName(String player) {
      return this.players.indexOf(player);
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
    * Gets the name of the current player.
    *
    * @return The player at the specified position.
    */
   public String getCurrentPlayer() {
      return this.players.get(this.current);
   }

   /**
    * Returns the number of active player.
    *
    * @return the number of active players
    */
   public int getNumPlayers() {
      return this.players.size();
   }

   /**
    * Gets the turn order of the current player.
    *
    * @return The current turn order.
    */
   public int getCurrent() {
      return this.current;
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
