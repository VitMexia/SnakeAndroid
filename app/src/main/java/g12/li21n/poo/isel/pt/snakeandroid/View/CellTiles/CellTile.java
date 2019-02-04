package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;


import android.content.Context;
import android.graphics.Canvas;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.AppleCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.BodyCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.DeadCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.EnemySnakeCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.MouseCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.PlayerCell;
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

        if(cell instanceof PlayerCell){
            return new HeadTile();
        }

        if(cell instanceof DeadCell && !((DeadCell)cell).isBad){
            DeadTile deadTile = new DeadTile();
            deadTile.direction = ((DeadCell) cell).getDirection();
            return deadTile;
        }

        if(cell instanceof BodyCell){
            return new BodyTile();
        }

        if(cell instanceof AppleCell){
            return new AppleTile(context);
        }

        if(cell instanceof WallCell){
            return new WallTile(context);
        }

        if(cell instanceof MouseCell){
            return new MouseTile(context);
        }

        if(cell instanceof EnemySnakeCell){
            BadSnakeTile badSnakeTile = new BadSnakeTile();
            badSnakeTile.direction = ((EnemySnakeCell) cell).getDirection();
            return new BadSnakeTile();
        }

        if(cell instanceof DeadCell && ((DeadCell)cell).isBad){
            BadDeadTile badDeadTile = new BadDeadTile();
            badDeadTile.direction = ((DeadCell) cell).getDirection();
            return badDeadTile;
        }
        return null;
    }

}
