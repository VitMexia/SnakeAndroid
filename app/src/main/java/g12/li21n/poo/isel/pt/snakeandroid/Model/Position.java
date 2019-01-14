package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.io.Serializable;

public class Position implements Serializable {

    private int col;
    private int line;

    public Position(int line, int col) {
        this.col = col;
        this.line = line;
    }

    public Position(){
    }

    public int getCol() {
        return col;
    }

    public int getLine() {
        return line;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;

        Position pos = (Position) obj;

        if (this.line == pos.line && this.col == pos.col)
            return true;
        return false;

    }

}
