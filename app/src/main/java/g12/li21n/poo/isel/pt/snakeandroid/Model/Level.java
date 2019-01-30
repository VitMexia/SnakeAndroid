package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.io.Serializable;
import java.util.LinkedList;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.AppleCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.DeadCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.EnemySnakeCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.MouseCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.MovingCells;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.PlayerCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.SnakeCells;

public class Level implements Serializable {

    private static final int APPLESTOWIN = 3;
    private int levelNumber, appleCount, startApples;

    public MapHolder mapHolder;

    private LinkedList<MovingCells> otherPlayers;
    private LinkedList<MovingCells> deadSnakes;
    private PlayerCell playerHead;
    private int playerSize;
    private Game game;
    private boolean finish;
    private Observer updater;
    private int stepCount;

    public Level(int levelNumber, int height, int width) {

        this.levelNumber = levelNumber;
        this.mapHolder = new MapHolder(new Cell[height][width]);
        this.appleCount = APPLESTOWIN;
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

    /**
     * Method to process one game turn/step/beat
     */
    public void step() {

        //goes through the list of mouses and Bad Snakes and each one does its thing
        for (MovingCells others : otherPlayers) {
            if (others.isDead && others instanceof MouseCell) {
                //otherPlayers.remove(others);
                // TODO: isto é suposto estar comentado ?
            } else
                others.move(stepCount, mapHolder);

            if (others instanceof SnakeCells) {
                if (((SnakeCells) others).getMeal() instanceof AppleCell) {
                    checkMeal(others, mapHolder); //if snake ate apple, a new apple will be generated
                }
            }
        }

        for (MovingCells deadSnake : deadSnakes) {
            deadSnake.move(stepCount, mapHolder);
        }

        playerSize = playerHead.getSnakeSize();
        playerHead.move(stepCount++, mapHolder);
        checkMeal(playerHead, mapHolder); // checks what the player ate and acts accordingly

        if (appleCount == 0 || playerHead.isDead) {
            finish = true;
            return;
        }

        if (stepCount % 10 == 0 && stepCount > 0) {
            game.addScore(-1);
        }

    }

    /**
     * Checks the last object eaten by each snake and updates score or apples as necessary
     */
    private void checkMeal(MovingCells cell, MapHolder mapHolder) {

        this.mapHolder = mapHolder;

        if (((SnakeCells) cell).getMeal() instanceof AppleCell) {
            if (cell == playerHead) {
                game.addScore(4);
                updater.applesUpdated(--appleCount);
            }
            AddApples(cell);
        }

        if (((SnakeCells) cell).getMeal() instanceof MouseCell) {

            if (cell == playerHead) {
                game.addScore(10);
            }
        }

        if (((SnakeCells) cell).getMeal() instanceof DeadCell) {
            if (cell == playerHead) {
                game.addScore(10 + 2 * ((DeadCell) ((SnakeCells) cell).getMeal()).getSize());
            }
        }
    }

    //Requests Cell Class to generate an apple on a free position
    private void AddApples(MovingCells cell) {

        if (appleCount >= startApples || cell instanceof EnemySnakeCell) {
            Cell apple = Cell.getApple(mapHolder);
            updater.cellCreated(apple.getPosition().getLine(), apple.getPosition().getCol(), apple);
            mapHolder.setCellAt(apple, apple.getPosition());
        }
    }

    //sets the snake Direction based on user input
    public void setSnakeDirection(Dir dir) {
//        if (!playerHead.getDirection().isOppositeOrSame(dir))
        playerHead.setDirection(dir);

    }

    //based on Cell type property defines the player and other players (Bad Snakes and Mouses);
    //it also sets the start apples quantity
    public void putCell(int l, int c, Cell cell) {

        cell.setPosition(l, c);
        mapHolder.setCellAt(cell, cell.getPosition());

        if (cell instanceof PlayerCell) {
            playerHead = (PlayerCell) cell;
            return;
        }

        if (cell instanceof AppleCell)
            startApples++;

        if (cell instanceof MovingCells)
            otherPlayers.add((MovingCells) cell);
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

    /**
     * initializes the listener list and implements the Cell StateChangeListener interface
     */
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
