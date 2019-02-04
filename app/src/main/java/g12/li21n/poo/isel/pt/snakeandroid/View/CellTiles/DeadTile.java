package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DeadTile extends HeadsTiles {


    private final Paint brushIn;
    private final Paint xEye;

    public DeadTile(){


        this.brushIn = new Paint();
        brushIn.setColor(Color.YELLOW);

        this.xEye = new Paint();
        xEye.setColor(Color.RED);
        xEye.setStrokeWidth(3);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawPaint(brush);

        setEyesPositions(side);
        setXPositions(side);


        canvas.drawCircle(side/2, side/2, side/2, brushIn );

        canvas.drawCircle(eye1x, eye1y, side/9, brushEye);
        canvas.drawCircle(eye2x, eye2y, side/9, brushEye);

        canvas.drawLine(eye1xX1Start1,eye1xY1Start1,eye1xX1End1,eye1xY1End1,xEye);
        canvas.drawLine(eye1xX1Start2,eye1xY1Start2,eye1xX1End2,eye1xY1End2,xEye);

        canvas.drawLine(eye2xX1Start1,eye2xY1Start1,eye2xX1End1,eye2xY1End1,xEye);
        canvas.drawLine(eye2xX1Start2,eye2xY1Start2,eye2xX1End2,eye2xY1End2,xEye);



    }


    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
