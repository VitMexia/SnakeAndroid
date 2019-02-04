package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import g12.li21n.poo.isel.pt.snakeandroid.R;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Img;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Tile;

public class MouseTile extends CellTile implements Tile {

    private final Img image;

    public MouseTile(Context context){
        int imgID = R.drawable.mouse;

        this.image = new Img(context, imgID);

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
