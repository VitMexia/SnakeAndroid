package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Position;


//Abstract Class that implements the methods and abstract methods that need to be implemented
//and used by the Snakes and Mouses
public abstract class MovingCells extends Cell {

    private Dir direction;
    public boolean isDead;

    public MovingCells() {
        super();
    }

    public void setDirection(Dir direction) {
        this.direction = direction;
    }

    public Dir getDirection() {
        return direction;
    }

    //tells the listener subscribers (Level) that a cell has moved
    protected void moveTo(Position oldPos, Position newPos) {
        for (StateChangeListener listener : listeners)
            listener.positionChanged(this, oldPos, newPos);
    }

    //tells the listener subscribers (Level) that a cell has been removed
    protected void removeCell(Position pos) {

        for (StateChangeListener listener : listeners) {
            listener.cellRemoved(pos);
        }
    }

    //tells the listener subscribers that a cell has been created
    protected void createCell(Cell cell) {
        for (StateChangeListener listener : listeners) {
            listener.cellCreated(cell);
        }
    }

    //abstract methods and interfaces
    public abstract void move(int stepCount, MapHolder mapHolder);


}
