package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.content.Context;
import android.graphics.Canvas;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.AppleCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.BodyCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.DeadCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.MouseCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.SnakeCells;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.WallCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Tile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.TileView;

public abstract class CellTile implements Tile  {

    public final static int SIDE = 1;
    private int color;
    private int foregroundColor;
    protected Dir direction;


    public void setColor(int color) {
        this.color = color;
    }

    public void setForegroundColor(int color){this.foregroundColor = color;}

    public void setDirection(Dir direction){
        this.direction = direction;
    }

    public static CellTile tileOf(Context context, Cell cell) {

        if(cell == null){
            return new EmptyTile();
        }
        else if(cell instanceof SnakeCells && !((SnakeCells)cell).isBad()){
            return new HeadTile();
        }
        else if(cell instanceof DeadCell && !((DeadCell)cell).isBad){
            return new DeadTile();
        }
        else if(cell instanceof BodyCell){
            return new BodyTile();
        }
        else if(cell instanceof AppleCell){
            return new AppleTile(context);
        }
        else if(cell instanceof WallCell){
            return new WallTile(context);
        }
        else if(cell instanceof MouseCell){
            return new MouseTile(context);
        }
        else if(cell instanceof SnakeCells && ((SnakeCells)cell).isBad()){
            return new BadSnakeTile();
        }
        else if(cell instanceof DeadCell && ((DeadCell)cell).isBad){
            return new BadDeadTile();
        }
        return null;
    }

}
