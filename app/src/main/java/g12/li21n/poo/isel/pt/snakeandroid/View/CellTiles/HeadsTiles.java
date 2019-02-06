package g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;

/**
 * Saves and sets all variables that are responsible for the creation
 * of the heads of the snakes (HeadTile, BadSnakeTile, BadDeadTile, DeadTile)
 * with the eyes and dead eyes based on the direction
 */

public abstract class HeadsTiles extends CellTile {

    protected Dir direction = Dir.UP;
    protected float eye1x = 0;
    protected float eye1y = 0;
    protected float eye2x = 0;
    protected float eye2y = 0;

    protected float eye1xX1Start1 = 0;
    protected float eye1xX1Start2 = 0;
    protected float eye1xY1Start1 = 0;
    protected float eye1xY1Start2 = 0;

    protected float eye1xX1End1 = 0;
    protected float eye1xX1End2 = 0;
    protected float eye1xY1End1 = 0;
    protected float eye1xY1End2 = 0;

    protected float eye2xX1Start1 = 0;
    protected float eye2xX1Start2 = 0;
    protected float eye2xY1Start1 = 0;
    protected float eye2xY1Start2 = 0;

    protected float eye2xX1End1 = 0;
    protected float eye2xX1End2 = 0;
    protected float eye2xY1End1 = 0;
    protected float eye2xY1End2 = 0;


    /**
     * sets tile direction
     * @param direction
     */
    public void setDirection(Dir direction){
        this.direction = direction;
    }

    /**
     * sets eyes positions
     * @param side
     */
    protected void setEyesPositions(int side) {
        eye1x = side / 4;
        eye1y = side / 4;
        eye2x = side - side / 4;
        eye2y = side / 4;

        switch (direction) {

            case RIGHT:
                eye1x = side - side / 4;
                eye1y = side - side / 4;
                break;
            case DOWN:
                eye1x = side - side / 4;
                eye1y = side - side / 4;
                eye2x = side / 4;
                eye2y = side - side / 4;
                break;
            case LEFT:
                eye2x = side / 4;
                eye2y = side - side / 4;
                break;

        }
    }

    /**
     * sets dead eyes positions
     * two lines for each eye
     * @param side
     */
    protected void setXPositions(int side){


        eye1xX1Start1 = side/4-side/8;
        eye1xX1Start2 = side/4+side/8;
        eye1xY1Start1 = side/4-side/8;
        eye1xY1Start2 = side/4-side/8;

        eye1xX1End1 = side/4+side/8;
        eye1xX1End2 = side/4-side/8;
        eye1xY1End1 = side/4+side/8;
        eye1xY1End2 = side/4+side/8;

        eye2xX1Start1 = side - side/4-side/8;
        eye2xX1Start2 = side - side/4+side/8;
        eye2xY1Start1 = side/4 - side/8;
        eye2xY1Start2 = side/4 - side/8;

        eye2xX1End1 = side-side/4+side/8;
        eye2xX1End2 = side -side/4-side/8;
        eye2xY1End1 = side/4+side/8;
        eye2xY1End2 = side/4+side/8;

        switch (direction) {

            case RIGHT:
                eye1xX1Start1 = side -side/4-side/8;
                eye1xX1Start2 = side -side/4+side/8;

                eye1xY1Start1 = side -side/4-side/8;
                eye1xY1Start2 = side -side/4-side/8;

                eye1xX1End1 = side -side/4+side/8;
                eye1xX1End2 = side -side/4-side/8;

                eye1xY1End1 = side -side/4+side/8;
                eye1xY1End2 = side -side/4+side/8;
                break;
            case DOWN:

                eye1xX1Start1 = side -side/4-side/8;
                eye1xX1Start2 = side -side/4+side/8;

                eye1xY1Start1 = side -side/4-side/8;
                eye1xY1Start2 = side -side/4-side/8;

                eye1xX1End1 = side -side/4+side/8;
                eye1xX1End2 = side -side/4-side/8;

                eye1xY1End1 = side -side/4+side/8;
                eye1xY1End2 = side -side/4+side/8;

                eye2xX1Start1 = side/4-side/8;
                eye2xX1Start2 = side/4+side/8;

                eye2xY1Start1 = side -side/4-side/8;
                eye2xY1Start2 = side -side/4-side/8;

                eye2xX1End1 = side/4+side/8;
                eye2xX1End2 = side/4-side/8;

                eye2xY1End1 = side/4+side/8;
                eye2xY1End2 = side/4+side/8;

                eye2xX1End1 = side/4+side/8;
                eye2xX1End2 = side/4-side/8;

                eye2xY1End1 = side -side/4+side/8;
                eye2xY1End2 = side -side/4+side/8;

                break;
            case LEFT:
                eye2xX1Start1 = side/4-side/8;
                eye2xX1Start2 = side/4+side/8;

                eye2xY1Start1 = side -side/4-side/8;
                eye2xY1Start2 = side -side/4-side/8;

                eye2xX1End1 = side/4+side/8;
                eye2xX1End2 = side/4-side/8;

                eye2xY1End1 = side/4+side/8;
                eye2xY1End2 = side/4+side/8;

                eye2xX1End1 = side/4+side/8;
                eye2xX1End2 = side/4-side/8;

                eye2xY1End1 = side -side/4+side/8;
                eye2xY1End2 = side -side/4+side/8;
                break;

        }

    }
}
