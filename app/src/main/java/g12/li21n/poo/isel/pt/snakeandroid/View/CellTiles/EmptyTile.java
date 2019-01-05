package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.TileView;

public class EmptyTile extends CellTile{
    private final Paint brush;

    public EmptyTile() {
        super();
        this.brush = new Paint();
        brush.setColor(Color.rgb(230, 230, 230));
    }

    @Override
    public void draw(Canvas canvas, int side) {
//        canvas.drawLine(0,0,side,side,brush);
//        canvas.drawLine(0,side,side,0,brush);


        canvas.drawPaint(brush);

    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }


}
