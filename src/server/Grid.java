package server;

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

   /** An array containing all the squares of this grid. */
   private Ship[][] playerGrid;

   /** An array containing all the squares of this grid. */
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
      this.opponentView = new Ship[size][size];
      for (int i = 0; i < this.size; i++) {
         for (int j = 0; j < this.size; j++) {
            this.opponentView[i][j] = Ship.EMPTY;
         }
      }
      this.playerGrid = new Ship[size][size];
      this.setUp(numShips);
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
            if (this.playerGrid[i][j] == null) {
               this.playerGrid[i][j] = Ship.EMPTY;
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

         // Check left to right
         if (direction && this.size - i >= ship.getLength()) {
            int validSpaces = 0;
            for (int k = 0; k < ship.getLength(); k++) {
               if (this.playerGrid[i+k][j] == null) {
                  validSpaces++;
               }
            }
            if (validSpaces == ship.getLength()) {
               for (int k = 0; k < ship.getLength(); k++) {
                  this.playerGrid[i+k][j] = ship;
               }
               placed = true;
            }

            // Check up to down
         } else if (!direction && this.size - j >= ship.getLength()) {
            int validSpaces = 0;
            for (int k = 0; k < ship.getLength(); k++) {
               if (this.playerGrid[i][j+k] == null) {
                  validSpaces++;
               }
            }
            if (validSpaces == ship.getLength()) {
               for (int k = 0; k < ship.getLength(); k++) {
                  this.playerGrid[i][j+k] = ship;
               }
               placed = true;
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

//    public void draw() {
//        System.out.print("      ");
//        for(int i = 0; i < this.size; i++) {
//            if (i != this.size - 1)
//                System.out.print(i + "   ");
//            else
//                System.out.print(i);
//        }
//        int count = 0;
//        for(int i = 0; i < size * 2; i++) {
//            for(int j = 0; j < size; j++) {
//                if(i == 0) {
//                    System.out.print("   ");
//                }
//                else if( i % 2 == 0) {
//                    System.out.print("+---");
//                }
//                else {
//                    if(j == 0) {
//                        System.out.print(i - count + " |");
//                    }
//                    else {
//                        System.out.print(board[i - count][j].getShipSym() + "|");
//                    }
//                }
//            }
//            count++;
//        }
//    }

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
            str.append(" ").append(this.playerGrid[j][i].toString()).append(" |");
         }
         str.append(divider);
      }
      return str.toString();
   }

}
