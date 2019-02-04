package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.TileView;

public class EmptyTile extends CellTile{



    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawPaint(brush);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }


}
