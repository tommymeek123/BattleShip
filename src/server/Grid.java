package server;

public class Grid {

    int size;
    Square[][] board;

    public Grid(int size) {
        this.size = size;
        this.board = new Square[size + 1][size+ 1];
    }

    public void draw() {
        System.out.print("      ");
        for(int i = 0; i < this.size; i++) {
            if (i != this.size - 1)
                System.out.print(i + "   ");
            else
                System.out.print(i);
        }
        int count = 0;
        for(int i = 0; i < size * 2; i++) {
            for(int j = 0; j < size; j++) {
                if(i == 0) {
                    System.out.print("   ");
                }
                else if( i % 2 == 0) {
                    System.out.print("+---");
                }
                else {
                    if(j == 0) {
                        System.out.print(i - count + " |");
                    }
                    else {
                        System.out.print(board[i - count][j].getShipSym() + "|");
                    }
                }
            }
            count++;
        }
    }
}
