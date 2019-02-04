package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BodyTile extends CellTile {

    private final Paint brushOut;
    private final Paint brushIn;

    public BodyTile(){

        this.brushOut = new Paint();
        brushOut.setColor(Color.RED);


        this.brushIn = new Paint();
        brushIn.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawPaint(brush);
        canvas.drawCircle(side / 2, side / 2, side / 1.7F, brushOut);
        canvas.drawCircle(side / 2, side / 2, side / 3, brushIn);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
