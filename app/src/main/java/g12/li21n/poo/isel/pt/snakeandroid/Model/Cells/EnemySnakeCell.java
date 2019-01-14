package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;

import java.util.ArrayList;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Position;

public class EnemySnakeCell extends SnakeCells {
    public EnemySnakeCell() {
        setDirection(null);
    }

    @Override
    public void move(int stepCount, MapHolder mapHolder) {
        super.move(stepCount, mapHolder);

        if (isDead) {
            if (bodyList.size() > 0) removeCell(bodyList.removeLast().getPosition());
            return;
        }
    }

    @Override
    protected Position getNewPos() {
        if(getDirection() == null || !mapHolder.isPositionFree(mapHolder.getNewPosition(getPosition(), getDirection()),true)) // Check if current direction is free
            setDirection(mapHolder.getRandomEmptyOrFoodDirection(this)); // Otherwise try to find a new direction

        if (getDirection() == Dir.NONE) {
            killSnake();
            return null;
        }

        return super.getNewPos();
    }
}
