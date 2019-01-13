package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;


import java.util.ArrayList;
import java.util.LinkedList;

import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Position;

public class AppleCell extends Cell {

    public AppleCell() {
        super();
    }

    public AppleCell(MapHolder mapHolder) {
        super();
        this.mapHolder = mapHolder;
        setPosition(getNewPosition());
    }

    private Position getNewPosition() {
        ArrayList<Position> freePosList = mapHolder.getEmptyPositions();
        return mapHolder.getRandomAvailablePosition(freePosList);
    }


}
