package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class models a single player's grid in a game of Battleship. It keeps
 * track of the ships that are on the grid as well as their placement and where
 * the hits and misses have occurred.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version November, 2020
 */
public class Grid {

   /** The size of this grid. */
   private int size;

   /** An array containing all the Ships of this grid. */
   private Ship[][] grid;
   /** An array containing the Ships in the view for the opponent */
   private Ship[][] opponentView;
   /** A list of all the ships on the grid. */
   private List<Ship> ships;

   /**
    * Constructor.
    *
    * @param size The size of the square grid.
    */
   public Grid(int size, int numShips) {
      this.size = size;
      ships = new ArrayList<>();
      this.grid = new Ship[size][size];
      this.opponentView = new Ship[size][size];
      this.setUp(numShips);

      //Setting up the opponent view grid
      for(int i = 0; i < this.size; i++) {
          for(int j = 0; j < this.size; j++) {
              this.opponentView[i][j] = Ship.EMPTY;
          }
      }
   }

   /**
    * Populates the grid with all the ships.
    *
    * @param numShips The number of ships on this grid.
    */
   private void setUp(int numShips) {
      for (int i = 0; i < numShips; i++) {
         this.placeShip();
      }
      for (int i = 0; i < this.size; i++) {
         for (int j = 0; j < this.size; j++) {
            if (this.grid[i][j] == null) {
               this.grid[i][j] = Ship.EMPTY;
            }
         }
      }
   }

   /**
    * Places a single ship on the board in a valid position.
    */
   private void placeShip() {
      int SHIP_TYPES = 5;
      Random rand = new Random();
      boolean placed = false;
      while (!placed) {
         int i = rand.nextInt(this.size);
         int j = rand.nextInt(this.size);
         int shipId = rand.nextInt(SHIP_TYPES) + 1; // between 1 and SHIP_TYPES
         boolean direction = rand.nextBoolean(); // true: vertical. false: horiz
         Ship ship = Ship.getById(shipId);
         if (direction && this.size - i >= ship.getLength()) {
            int validSpaces = 0;
            for (int k = 0; k < ship.getLength(); k++) {
               if (this.grid[i+k][j] == null) {
                  validSpaces++;
               }
            }
            if (validSpaces == ship.getLength()) {
               for (int k = 0; k < ship.getLength(); k++) {
                  this.grid[i+k][j] = ship;
               }
               placed = true;
               this.ships.add(ship);
            }
         } else if (!direction && this.size - j >= ship.getLength()) {
            int validSpaces = 0;
            for (int k = 0; k < ship.getLength(); k++) {
               if (this.grid[i][j+k] == null) {
                  validSpaces++;
               }
            }
            if (validSpaces == ship.getLength()) {
               for (int k = 0; k < ship.getLength(); k++) {
                  this.grid[i][j+k] = ship;
               }
               placed = true;
               this.ships.add(ship);
            }
         }
      }
   }

   /**
    * Displays the grid to the console.
    */
   public void draw() {
      System.out.println(this.toString());
   }

    /**
     * Displays the opponent view of the grid to the console
     */
   public void drawOpponent() {
       System.out.println(this.printOpponentView());
   }

    /**
     * Called when an opponent fires. Determines if it's a hit or miss and updates grids
     * @param i The i index of the grid
     * @param j The j index of the grid
     */
   public void shotsFired(int i, int j) {
       if(this.grid[i][j] != Ship.EMPTY && this.grid[i][j] != Ship.HIT && this.grid[i][j] != Ship.MISS) {
           grid[i][j] = Ship.HIT;
           opponentView[i][j] = Ship.HIT;
       }
       else if(this.grid[i][j] == Ship.EMPTY) {
           grid[i][j] = Ship.MISS;
           opponentView[i][j] = Ship.MISS;
       }
   }


   /**
    * Returns a string representation of this grid.
    *
    * @return A string representation of this grid.
    */
   @Override
   public String toString() {
      StringBuilder str = new StringBuilder(" ");
      // Top row numbering all the columns of the grid.
      for (int i = 0; i < this.size; i++) {
         str.append("   ").append(i);
      }
      String divider = "\n  +" + "---+".repeat(this.size) + "\n";
      str.append(divider);
      for (int i = 0; i < this.size; i++) {
         str.append(i).append(" |");
         for (int j = 0; j < this.size; j++) {
            str.append(" ").append(this.grid[i][j].toString()).append(" |");
         }
         str.append(divider);
      }
      return str.toString();
   }

    /**
     * Returns a string representation of the opponent view grid
     * @return A string representation of the opponent view grid
     */
   public String printOpponentView() {
       StringBuilder str = new StringBuilder(" ");
       // Top row numbering all the columns of the grid.
       for (int i = 0; i < this.size; i++) {
           str.append("   ").append(i);
       }
       String divider = "\n  +" + "---+".repeat(this.size) + "\n";
       str.append(divider);
       for (int i = 0; i < this.size; i++) {
           str.append(i).append(" |");
           for (int j = 0; j < this.size; j++) {
               str.append(" ").append(this.opponentView[i][j].toString()).append(" |");
           }
           str.append(divider);
       }
       return str.toString();
   }

}
