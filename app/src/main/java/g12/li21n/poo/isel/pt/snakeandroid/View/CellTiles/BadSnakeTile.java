package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BadSnakeTile extends HeadsTiles {


    private final Paint brushIn;


    public BadSnakeTile(){

        this.brushIn = new Paint();
        brushIn.setColor(Color.rgb(102, 255, 255));

    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawPaint(brush);

        setEyesPositions(side);

        canvas.drawCircle(side/2, side/2, side/2, brushIn );
        canvas.drawCircle(eye1x, eye1y, side/9, brushEye);
        canvas.drawCircle(eye2x, eye2y, side/9, brushEye);

    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
