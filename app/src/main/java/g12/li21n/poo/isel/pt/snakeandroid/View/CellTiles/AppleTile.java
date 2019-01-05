package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import g12.li21n.poo.isel.pt.snakeandroid.R;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Img;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Tile;

public class AppleTile extends CellTile implements Tile {

    private final Img image;
    private final Paint brush;

    public AppleTile(Context context){
        //super(context);
        int imgID = R.drawable.apple;

        this.image = new Img(context, imgID);
        this.brush = new Paint();

        brush.setColor(Color.rgb(230, 230, 230));
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawPaint(brush);
        image.draw(canvas, side, side, brush);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
