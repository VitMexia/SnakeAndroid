package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.util.LinkedList;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.AppleCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.DeadCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.MouseCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.MovingCells;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.SnakeCells;

public class Level {

    private int levelNumber, appleCount, startApples;

    public MapHolder mapHolder;

    private LinkedList<MovingCells> otherPlayers;
    private LinkedList<MovingCells> deadSnakes;
    private SnakeCells playerHead;
    private int playerSize;
    private Game game;
    private boolean finish;
    private Observer updater;
    private int stepCount;

    public Level(int levelNumber, int height, int width) {

        this.levelNumber = levelNumber;
        this.mapHolder = new MapHolder(new Cell[height][width]);
        this.appleCount = 10;//TODO: Correct to 10
        this.otherPlayers = new LinkedList<>();
        this.deadSnakes = new LinkedList<>();
    }

    public int getHeight() {
        return mapHolder.getHeight();
    }

    public int getWidth() {
        return mapHolder.getWidth();
    }

    public int getNumber() {
        return levelNumber;
    }

    public int getRemainingApples() {
        return appleCount;
    }

    public Cell getCell(int l, int c) {
        return mapHolder.getCellAt(l, c);
    }

    public void setObserver(Observer updater) {
        this.updater = updater;
    }

    public boolean isFinished() {
        return finish;
    }

    public boolean snakeIsDead() {
        return playerHead.isDead;
    }

    public void step() {

        //goes through the list of mouses and Bad Snakes and each one does its thing
        for (MovingCells others : otherPlayers) {
            if (others.isDead && others instanceof MouseCell)
                otherPlayers.remove(others);
            else
                others.doYourThing(stepCount, mapHolder);

            if (others instanceof SnakeCells) {
                if (((SnakeCells) others).meal instanceof AppleCell) {
                    checkMeal(others, mapHolder); //if snake ate apple, a new apple will be generated
                }
            }
        }

        for (MovingCells deadSnake : deadSnakes) {
            deadSnake.doYourThing(stepCount, mapHolder);
        }

        playerSize = playerHead.snakeSize;
        playerHead.doYourThing(stepCount, mapHolder);
        checkMeal(playerHead, mapHolder); // checks what the player ate and acts accordingly

        if (appleCount == 0) {
            finish = true;
            return;
        }

        if (playerHead.isDead) {
            finish = true;
            return;
        }
        stepCount += 1;

        if (stepCount % 10 == 0 && stepCount > 0) {
            game.addScore(-1);
        }

    }

    //checks was was eaten by each snake (player or BadASnake) and adds score and requests a new apple when required
    private void checkMeal(MovingCells cell, MapHolder mapHolder) {

        this.mapHolder = mapHolder;

        if (((SnakeCells) cell).meal instanceof AppleCell) {
            if (!((SnakeCells) cell).isBad) {
                appleCount -= 1;
                game.addScore(4);
                updater.applesUpdated(appleCount);
            }
            AddApples(cell);
        } else if (((SnakeCells) cell).meal instanceof MouseCell) {

            if (!((SnakeCells) cell).isBad) {
                game.addScore(10);
            }
        } else if (((SnakeCells) cell).meal instanceof DeadCell) {

            if (!((SnakeCells) cell).isBad) {
                game.addScore(10 + 2 * ((DeadCell) ((SnakeCells) cell).meal).getSize());
            }
        }
    }

    //Requests Cell Class to generate an apple on a free position
    private void AddApples(MovingCells cell) {

        if (appleCount >= startApples || ((SnakeCells) cell).isBad) {
            Cell apple = Cell.getApple(mapHolder);
            updater.cellCreated(apple.getPosition().getLine(), apple.getPosition().getCol(), apple);
            mapHolder.setCellAt(apple, apple.getPosition());
        }
    }

    //sets the snake Direction based on user input
    public void setSnakeDirection(Dir dir) {

        switch (dir) {
            case UP:
                if (playerHead.getDirection() != Dir.DOWN) playerHead.setDirection(dir);
                break;
            case DOWN:
                if (playerHead.getDirection() != Dir.UP) playerHead.setDirection(dir);
                break;
            case RIGHT:
                if (playerHead.getDirection() != Dir.LEFT) playerHead.setDirection(dir);
                break;
            case LEFT:
                if (playerHead.getDirection() != Dir.RIGHT) playerHead.setDirection(dir);
                break;
        }
    }

    //based on Cell type property defines the player and other players (Bad Snakes and Mouses);
    //it also sets the start apples quantity
    public void putCell(int l, int c, Cell cell) {


        if (cell instanceof SnakeCells && !((SnakeCells) cell).isBad) {
            playerHead = (SnakeCells) cell;
        } else if (cell instanceof SnakeCells && ((SnakeCells) cell).isBad) {

            otherPlayers.add((MovingCells) cell);
        } else if (cell instanceof AppleCell) {
            startApples += 1;
        } else if (cell instanceof MouseCell) {
            otherPlayers.add(0, (MovingCells) cell);
        }

        cell.setPosition(l, c);
        mapHolder.setCellAt(cell, cell.getPosition());

    }


    public interface Observer {
        public void cellUpdated(int l, int c, Cell cell); //its never used in this implementation

        public void cellCreated(int l, int c, Cell cell);

        public void cellRemoved(int l, int c);

        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell);

        public void applesUpdated(int apples);
    }

    public void init(Game game) {

        this.game = game;
        initBehaviour();
    }

    //initializes the listener list and implements the Cell StateChangeListener interface
    private void initBehaviour() {

        Cell.StateChangeListener listener = new MovingCells.StateChangeListener() {

            @Override
            public void positionChanged(Cell source, Position oldPos, Position newPos) {
                updater.cellMoved(oldPos.getLine(), oldPos.getCol(), newPos.getLine(), newPos.getCol(), source);
                mapHolder.clearCellAt(oldPos);
                mapHolder.setCellAt(source, newPos);
            }

            @Override
            public void cellRemoved(Position posToRemove) {
                updater.cellRemoved(posToRemove.getLine(), posToRemove.getCol());
                mapHolder.clearCellAt(posToRemove);
            }

            @Override
            public void cellCreated(Cell cell) {
                updater.cellCreated(cell.getPosition().getLine(), cell.getPosition().getCol(), cell); //add where head wa
                mapHolder.setCellAt(cell, cell.getPosition());
            }


        };

        playerHead.addListener(listener);
        for (Cell cell : otherPlayers) {
            cell.addListener(listener);
        }


    }


}
