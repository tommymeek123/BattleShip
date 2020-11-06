package server;

/**
 * An enum for the ships and other symbols used in the Battleship grid.
 *
 * @author Gatlin Cruz
 * @author Tommy Meek
 * @version November, 2020
 */
public enum Ship {

   /** A Carrier. The largest ship in the armada. */
   CARRIER('C', 5, 1),

   /** A Battleship. The second largest ship. */
   BATTLESHIP('B', 4, 2),

   /** A Cruiser. A smaller version of a Battleship. */
   CRUISER('R', 3, 3),

   /** A Submarine. Can go completely underwater. */
   SUBMARINE('S', 3, 4),

   /** A Destroyer. The smallest ship in the armada. */
   DESTROYER('D', 2, 5),

   /** The burning wreckage of a ship that has been hit. */
   HIT('@', 1, 0),

   /** Heavy waves from a missile that landed safely in the sea. */
   MISS('X', 1, 0),

   /** Calm waters. No ships. Just some fish. */
   EMPTY(' ', 1, 0);

   /** A symbol to represent the ship on a BattleShip grid. */
   char symbol;

   /** How many spaces this ship takes up on the grid. */
   int length;

   /** A unique integer to identify this type of ship. */
   int id;

   /**
    * Constructor for the Ship enum.
    *
    * @param symbol A symbol to represent the ship on a BattleShip grid.
    * @param length How many spaces this ship takes up on the grid.
    * @param id A unique integer to identify this type of ship.
    */
   Ship(char symbol, int length, int id) {
      this.symbol = symbol;
      this.length = length;
      this.id = id;
   }

   /**
    * Accessor for this Ship's length.
    *
    * @return this Ship's length.
    */
   public int getLength() {
      return this.length;
   }

   /**
    * Returns the symbol associated with this ship.
    *
    * @return A string representation of this ship.
    */
   @Override
   public String toString() {
      return String.valueOf(this.symbol);
   }
}
