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
   CARRIER('C', ' ', 5, 1),

   /** A Battleship. The second largest ship. */
   BATTLESHIP('B', ' ', 4, 2),

   /** A Cruiser. A smaller version of a Battleship. */
   CRUISER('R', ' ', 3, 3),

   /** A Submarine. Can go completely underwater. */
   SUBMARINE('S', ' ', 3, 4),

   /** A Destroyer. The smallest ship in the armada. */
   DESTROYER('D', ' ', 2, 5),

   /** The burning wreckage of a ship that has been hit. */
   HIT('@', '@', 1, 0),

   /** Heavy waves from a missile that landed safely in the sea. */
   MISS('X', 'X', 1, 0),

   /** Calm waters. No ships. Just some fish. */
   EMPTY(' ', ' ', 1, 0);

   /** A symbol to represent the ship on a BattleShip grid. */
   char symbol;

   /** What an enemy sees when they observe this ship on a grid. */
   char enemyView;

   /** How many spaces this ship takes up on the grid. */
   int length;

   /** A unique integer to identify this type of ship. */
   int id;

   /**
    * Constructor for the Ship enum.
    *
    * @param symbol A symbol to represent the ship on a BattleShip grid.
    * @param enemyView What an enemy sees when they observe this ship on a grid.
    * @param length How many spaces this ship takes up on the grid.
    * @param id A unique integer to identify this type of ship.
    */
   Ship(char symbol, char enemyView, int length, int id) {
      this.symbol = symbol;
      this.enemyView = enemyView;
      this.length = length;
      this.id = id;
   }

   /**
    * Retrieves the ship with the specified id.
    *
    * @param id The id of the desired ship.
    * @return The ship with the specified id.
    */
   public static Ship getById(int id) {
      Ship ship = null;
      for (Ship s : values()) {
         if (s.id == id) {
            ship = s;
         }
      }
      return ship;
   }

   /**
    * Accessor for this Ship's length.
    *
    * @return This Ship's length.
    */
   public int getLength() {
      return this.length;
   }

   /**
    * Returns what the ship's owner sees when they observe this ship on a grid.
    *
    * @param player Either "Friendly" or "Enemy" depending on the desired view.
    * @return A string representation of this ship.
    */
   public String show(String player) {
      String view;
      switch (player.toLowerCase()) {
         case "friendly":
            view = String.valueOf(this.symbol);
            break;
         case "enemy":
            view =String.valueOf(this.enemyView);
            break;
         default:
            throw new IllegalStateException("Unexpected value: " + player);
      }
      return view;
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
