package server;

public class Grid {

    /** The size of this grid. */
    private int size;

    /** An array containing all the squares of this grid. */
    private Square[][] grid;

    /**
     * Constructor.
     *
     * @param size The size of the square grid.
     */
    public Grid(int size) {
        this.size = size;
        this.grid = new Square[size + 1][size+ 1];
        this.makeBoard();
    }

    /**
     * Populates the grid with all the ships.
     */
    public void makeBoard() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.grid[i][j] = new Square();
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
        String divider = "\n  +" +"---+".repeat(this.size) + "\n";
        str.append(divider);
        for (int i = 0; i < this.size; i++) {
            str.append(i).append(" |");
            for (int j = 0; j < this.size; j++) {
                str.append(" ").append(this.grid[j][i].toString()).append(" |");
            }
            str.append(divider);
        }
        return str.toString();
    }

}
