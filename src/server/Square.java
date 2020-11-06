package server;

public class Square {

    private Ship ship;

    public Square() {
        this.ship = Ship.EMPTY;
    }

    public Ship getShip() {
        return this.ship;
    }

    public void setShip(char letter) {
        switch(letter) {
            case 'C':
                this.ship = Ship.CARRIER;
            case 'B':
                this.ship = Ship.BATTLESHIP;
            case 'R':
                this.ship = Ship.CRUISER;
            case 'S':
                this.ship = Ship.SUBMARINE;
            case 'D':
                this.ship = Ship.DESTROYER;
            case '@':
                this.ship = Ship.HIT;
            case 'X':
                this.ship = Ship.MISS;
            default:
                this.ship = Ship.EMPTY;
        }
    }

    public char getShipSym() {
        return ship.shipSym();
    }

    /**
     * Returns the symbol associated with the ship in this square.
     * If this square has no ship, a space is returned.
     *
     * @return a String representation of the ship on this square.
     */
    @Override
    public String toString() {
        return String.valueOf(this.ship.symbol);
    }

    private enum Ship {

        CARRIER('C', 4),
        BATTLESHIP('B', 4),
        CRUISER('R', 3),
        SUBMARINE('S', 3),
        DESTROYER('D', 2),
        HIT('@', 1),
        MISS('X', 1),
        EMPTY(' ', 1);

        char symbol;
        int size;

        private Ship(char symbol, int size) {
            this.symbol = symbol;
            this.size = size;
        }

        private char shipSym() {
            return this.symbol;
        }

    }
}
