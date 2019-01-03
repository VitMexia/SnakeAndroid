package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;


import java.util.LinkedList;

import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;

public class DeadCell extends MovingCells {

    public boolean isBad;
    protected LinkedList<BodyCell> bodyList;

    public DeadCell() {
        super();

    }

    @Override
    public void doYourThing(int stepCount, MapHolder mapHolder) {

        if (bodyList.size() > 0)
            removeCell(bodyList.removeLast().getPosition());

    }

    public int getSize() {
        return bodyList.size();
    }


}
