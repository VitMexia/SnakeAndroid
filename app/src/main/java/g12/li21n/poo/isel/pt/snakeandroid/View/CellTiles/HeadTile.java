package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;

public class HeadTile extends HeadsTiles {

    private final Paint brush;
    private final Paint brushIn;
    private final Paint brusheye;

    public HeadTile(){


        this.brush = new Paint();
        brush.setColor(Color.rgb(230, 230, 230));

        this.brushIn = new Paint();
        brushIn.setColor(Color.YELLOW);

        this.brusheye = new Paint();
        brusheye.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawPaint(brush);

        setEyesPositions(side);

        canvas.drawCircle(side/2, side/2, side/2, brushIn );
        canvas.drawCircle(eye1x, eye1y, side/9, brusheye);
        canvas.drawCircle(eye2x, eye2y, side/9, brusheye);

    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }

}
