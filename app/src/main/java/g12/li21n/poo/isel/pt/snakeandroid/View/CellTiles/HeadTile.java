package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class HeadTile extends CellTile {

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
//        canvas.drawCircle(side/2, side/2, side/1.7F, brushOut );
        canvas.drawCircle(side/2, side/2, side/2, brushIn );
        canvas.drawCircle(side/4, side/4, side/9, brusheye);
        canvas.drawCircle(side-side/4, side/4, side/9, brusheye);

    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }

}
