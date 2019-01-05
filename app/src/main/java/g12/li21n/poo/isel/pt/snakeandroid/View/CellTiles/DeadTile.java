package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.graphics.Canvas;

public class DeadTile extends CellTile {

    public DeadTile(){

    }

    @Override
    public void draw(Canvas canvas, int side) {

    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
