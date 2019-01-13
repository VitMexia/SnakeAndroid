package g12.li21n.poo.isel.pt.snakeandroid.Model.Cells;

import java.util.LinkedList;
import java.util.List;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.MapHolder;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Position;


public class SnakeCells extends MovingCells {

    protected int bodyToAdd;
    private boolean justAte;
    public boolean isBad;
    public Cell meal;
    public int snakeSize;

    protected LinkedList<BodyCell> bodyList;
    protected static LinkedList<SnakeCells> snakes = new LinkedList();     //Static method to save each snake that can be accessed


    public SnakeCells() {
        super();
        bodyToAdd = 4;
        snakeSize = 1;
        bodyList = new LinkedList<>();
        setDirection(Dir.UP);
        snakes.add(this);
    }

    public SnakeCells(boolean bad) {
        this();
        isBad = bad;
        setDirection(null);
    }

    //sets the new positions for the player and other snakes
    @Override
    public void move(int stepCount, MapHolder mapHolder) {
        this.mapHolder = mapHolder;

        Position oldPos = getPosition();
        Position newPos = getNewPos();

        // TODO: isto tem que levar rework, o prof vai-se mandar aos arames com tanto if (e isto é complicado de ler para caraças)

        if (!isBad) {
            if (stepCount % 10 == 0 && stepCount != 0 && bodyList.size() > 0) {
                removeCell(bodyList.removeLast().getPosition());
                snakeSize--;
            } else if (stepCount % 10 == 0 && stepCount != 0 && bodyList.size() == 0) {
                killSnake();
                return;
            }
        } else if (isDead) {

            if (bodyList.size() > 0) removeCell(bodyList.removeLast().getPosition());
            return;
        } else {
            List<Position> availablePos = mapHolder.getNewAdjacentAvailablePosition(getPosition(), true);

            boolean isFreePos = false;

            for (Position pos : availablePos) {
                if (newPos.equals(pos)) {
                    isFreePos = true;
                    break;
                }
            }
            if (!isFreePos) {
                newPos = mapHolder.getRandomAvailablePosition(availablePos);

                if (newPos != null) {
                    setDirection(getDirection(newPos));
                } else {
                    killSnake();
                    return;
                }
            }
        }

        validateNewPos(newPos);

        if (isDead) {
            return;
        }
        setPosition(newPos);
        moveTo(oldPos, newPos);
        addMoveBody(oldPos);

    }


    //depending on what type of cell is on the new position addbody, kill snake
    private void validateNewPos(Position pos) {
//TODO: precisa rework, no mínimo tirar os instanceof/ifs, idealmente ter isto tudo em despacho dinâmico
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
            bodyToAdd -= 1;
            snakeSize += 1;
        }

        justAte = false;
    }

    //get new position generates the next position based on the current direction or newly generated direction if non available
    private Position getNewPos() {

        if (isBad && getPosition() != null && getDirection() == null)
            setDirection(provideInitialDirection());

        return new Position(getPosition().getLine() + getDirection().line, getPosition().getCol() + getDirection().column);
    }

    //Provides a random  generated direction when the game starts to each one of the bad snakes
    private Dir provideInitialDirection() {
        Position pos = mapHolder.getRandomAvailablePosition(mapHolder.getNewAdjacentAvailablePosition(getPosition(), true));

        if (getDirection() == null && pos != null) {
            return getDirection(pos);
        }
        return null;
    }

    private Dir getDirection(Position pos) {
// TODO: meter o getRandom.. a devolver logo a direção em vez de estar a dar esta volta toda?
        int l = pos.getLine() - getPosition().getLine();
        int c = pos.getCol() - getPosition().getCol();

        if (l == 0) {
            if (c == 1) {
                return Dir.RIGHT;
            } else {
                {
                    return Dir.LEFT;
                }
            }
        } else {
            if (l == 1) {
                return Dir.DOWN;
            } else {
                return Dir.UP;
            }
        }
    }

    //set the properties of a deadsnake and creates an instance of a DeadCel. Also removes all the body cells of the snake when bad
    public void killSnake() {
        isDead = true;
        DeadCell dc = new DeadCell();
        dc.isBad = this.isBad;
        dc.bodyList = this.bodyList;
        dc.setPosition(this.getPosition());

        removeCell(this.getPosition());
        createCell(dc);
    }


}
