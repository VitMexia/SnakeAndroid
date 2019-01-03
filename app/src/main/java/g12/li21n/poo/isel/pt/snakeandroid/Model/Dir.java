package g12.li21n.poo.isel.pt.snakeandroid.Model;

public enum Dir {
    UP(-1,0), DOWN(1,0), RIGHT(0,1), LEFT(0,-1), NONE(0,0);

    public final int line, column;

    Dir(int line, int column){
        this.line = line;
        this.column = column;
    }
}
