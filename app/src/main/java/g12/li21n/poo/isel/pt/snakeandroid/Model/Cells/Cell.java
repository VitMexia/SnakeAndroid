package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Position;


public abstract class Cell implements Serializable {


    private Position position;
    protected List<StateChangeListener> listeners;
    protected MapHolder mapHolder;

    public Cell() {
        listeners = new ArrayList<>();
    }


    //Sets the Cell position by receiving a position
    public void setPosition(Position position) {
        this.position = position;
    }

    //Sets the Cell position by receiving a line and a column
    public void setPosition(int l, int c) {
        this.position = new Position(l, c);
    }

    //return Cell current Position
    public Position getPosition() {
        return position;
    }

    //creates a new cell depending on the char receive
    public static Cell newInstance(char type) {

        switch (type) {
            case '@':
                return new SnakeCells();
            case 'A':
                return new AppleCell();
            case 'X':
                return new WallCell();
            case '#':
                return new BodyCell();
            case 'M':
                return new MouseCell();
            case '*':
                return new SnakeCells(true);
            default:
                return null;
        }
    }

    //get an apple
    public static Cell getApple(MapHolder mapHolder) {
        return new AppleCell(mapHolder);
    }

    //Adds a listener to the array
    public void addListener(StateChangeListener stateChangeListener) {
        listeners.add(stateChangeListener);
    }

    public void removeListener(StateChangeListener stateChangeListener) {
        listeners.remove(stateChangeListener);
    }

    //interface that sets the contract to be implemented on who needs to know about Cell changes (Level)
    public interface StateChangeListener {
        void positionChanged(Cell source, Position oldPos, Position newPos);

        void cellRemoved(Position posToRemove);

        void cellCreated(Cell cell);

    }


}
