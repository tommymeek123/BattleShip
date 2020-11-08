package server;

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

   /** A list of grids. One for each player. */
   private List<Grid> grids;

   /**
    * Constructor for the Game class
    *
    * @param size The size of the grids.
    */
   public Game(int size) {
      int numShips = getNumShips(size);
      Grid grid = new Grid(size, numShips);
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
}
