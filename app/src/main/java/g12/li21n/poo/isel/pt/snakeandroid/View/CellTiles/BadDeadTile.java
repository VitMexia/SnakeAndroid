package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BadDeadTile extends HeadsTiles{

    private final Paint brush;
    private final Paint brushIn;
    private final Paint brusheye;
    private final Paint xEye;

    public BadDeadTile(){

        this.brush = new Paint();
        brush.setColor(Color.rgb(230, 230, 230));

        this.brushIn = new Paint();
        brushIn.setColor(Color.rgb(102, 255, 255));

        this.brusheye = new Paint();
        brusheye.setColor(Color.BLACK);

        this.xEye = new Paint();
        xEye.setColor(Color.RED);
        xEye.setStrokeWidth(3);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawPaint(brush);

        canvas.drawCircle(side/2, side/2, side/2, brushIn );
        canvas.drawCircle(side/4, side/4, side/9, brusheye);
        canvas.drawCircle(side-side/4, side/4, side/9, brusheye);

        canvas.drawLine(side/4-side/8,side/4-side/8,side/4+side/8,side/4+side/8,xEye);
        canvas.drawLine(side/4+side/8,side/4-side/8,side/4-side/8,side/4+side/8,xEye);

        canvas.drawLine(side -side/4-side/8,side/4-side/8,side-side/4+side/8,side/4+side/8,xEye);
        canvas.drawLine(side -side/4+side/8,side/4-side/8,side -side/4-side/8,side/4+side/8,xEye);


    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
