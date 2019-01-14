package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Position;


public abstract class SnakeCells extends MovingCells {

    protected int bodyToAdd;
    private boolean justAte;
    private Cell meal;
    protected int snakeSize;

    protected LinkedList<BodyCell> bodyList;
    protected static LinkedList<SnakeCells> snakes = new LinkedList();     //Static method to save each snake that can be accessed


    public SnakeCells() {
        super();
        bodyToAdd = 4;
        snakeSize = 1;
        bodyList = new LinkedList<>();
        snakes.add(this);
    }

    //sets the new positions for the player and other snakes
    @Override
    public void move(int stepCount, MapHolder mapHolder) {
        this.mapHolder = mapHolder;

        Position oldPos = getPosition();
        Position newPos = getNewPos();

        if (newPos != null)
            validateNewPos(newPos);

        if (isDead)
            return;

        setPosition(newPos);
        moveTo(oldPos, newPos);
        addMoveBody(oldPos);

    }


    //depending on what type of cell is on the new position addbody, kill snake
    private void validateNewPos(Position pos) {
        Cell cell = mapHolder.getCellAt(pos);
        meal = null;
        if (cell == null) return;

        if (cell instanceof AppleCell) {
            removeCell(cell.getPosition());
            meal = cell;
            bodyToAdd += 4;
            justAte = true;
        } else if (cell instanceof MouseCell) {
            removeCell(cell.getPosition());
            meal = cell;
            ((MovingCells) cell).isDead = true;
            bodyToAdd += 10;
            justAte = true;
        } else if (cell instanceof DeadCell) {
            removeCell(cell.getPosition());
            bodyToAdd += 10 + 2 * ((DeadCell) cell).bodyList.size();
            meal = cell;
            justAte = true;
        } else if (cell instanceof WallCell) {
            killSnake();
            return;
        } else if (cell instanceof BodyCell) {
            boolean notMoved = false;
            for (SnakeCells snake : snakes) {
                if (snake.equals(this)) {
                    notMoved = true;
                }

                boolean lastBodyPos = snake.bodyList.getLast().getPosition().equals(cell.getPosition());

                //verifies if the the cell to move to is the last cell of the snake or other snakes and if that snake will move or not
                //and kill the snake if not the last cell or is the last cell and wont move (is adding bodies)
                if (notMoved && (!lastBodyPos || snake.bodyToAdd > 0 && lastBodyPos)) {
                    killSnake();
                    return;
                }
            }
        }

    }

    //this class defines how the body of the snake moves. a cell is created where the head was and if there are no bodies to add
    //the last body is removed
    private void addMoveBody(Position pos) {
        BodyCell bc = new BodyCell();
        bc.setPosition(pos);
        createCell(bc);
        bodyList.add(0, bc);

        if (bodyToAdd == 0) {
            if (!this.getPosition().equals(bodyList.getLast().getPosition())) {
                removeCell(bodyList.removeLast().getPosition());
            }
        } else if (!justAte) {
            bodyToAdd--;
            snakeSize++;
        }

        justAte = false;
    }

    //get new position generates the next position based on the current direction or newly generated direction if non available
    protected Position getNewPos() {
        return new Position(getPosition().getLine() + getDirection().line, getPosition().getCol() + getDirection().column);
    }

    //set the properties of a deadsnake and creates an instance of a DeadCel. Also removes all the body cells of the snake when bad
    public void killSnake() {
        isDead = true;
        DeadCell dc = new DeadCell();
        dc.bodyList = this.bodyList;
        dc.setPosition(this.getPosition());

        removeCell(this.getPosition());
        createCell(dc);
    }

    public Cell getMeal() {
        return meal;
    }

    public int getSnakeSize() {
        return snakeSize;
    }

}
