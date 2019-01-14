package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;

public class PlayerCell extends SnakeCells {
    public PlayerCell() {
        super();
        setDirection(Dir.UP);
    }

    @Override
    public void move(int stepCount, MapHolder mapHolder) {
        super.move(stepCount, mapHolder);

        if (stepCount % 10 == 0 && stepCount != 0) { // Penalty turn
            if (bodyList.size() == 0) { // Kill snake if all body parts are gone
                killSnake();
                return;
            }
            removeCell(bodyList.removeLast().getPosition());
            snakeSize--;
        }
    }

}
