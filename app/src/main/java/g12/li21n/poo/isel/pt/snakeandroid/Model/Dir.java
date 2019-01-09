package g12.li21n.poo.isel.pt.snakeandroid.Model;

public enum Dir {
    UP(-1,0), DOWN(1,0), RIGHT(0,1), LEFT(0,-1), NONE(0,0);

    public final int line, column;

    Dir(int line, int column){
        this.line = line;
        this.column = column;
    }

    /**
     * Returns true if facing the same direction or the direct opposite
     * @param dir The direction to compare against
     * @return If opposite direction
     */
    boolean isOppositeOrSame(Dir dir){
        return Math.abs(dir.column) == Math.abs(this.column) && Math.abs(dir.line) == Math.abs(this.line);
    }
}
