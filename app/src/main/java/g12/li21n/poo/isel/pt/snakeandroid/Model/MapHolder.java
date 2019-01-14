package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.AppleCell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.MouseCell;


public class MapHolder {

    private final Cell[][] cellMap;
    private final int height;
    private final int width;
    private final Random random = new Random();
    private ArrayList<Position> freeCellsPositions;
    private final List<Dir> directions;

    public MapHolder(Cell cell[][]) {

        this.cellMap = cell;
        this.height = cell.length;
        this.width = cell[0].length;
        freeCellsPositions = new ArrayList<>();
        directions = new ArrayList<>(Arrays.asList(Dir.values()));
        initEmptyPositions();
    }

    private void initEmptyPositions(){

        for (int col = 0; col < width; col++) {
            for (int line = 0; line < height; line++) {
                if (cellMap[line][col] == null) {
                    freeCellsPositions.add(new Position(line, col));
                }
            }
        }

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    public Cell getCellAt(int line, int col) {
        return cellMap[line][col];
    }

    public void setCellAt(Cell cell, Position pos) {
        cellMap[pos.getLine()][pos.getCol()] = cell;
        freeCellsPositions.remove(pos);
    }

    public void clearCellAt(Position pos) {
        cellMap[pos.getLine()][pos.getCol()] = null;
        freeCellsPositions.add(pos);
    }

    public Cell getCellAt(Position position) {

        Position correctPos = position;
        //Correct Lines
        if (position.getLine() == -1) {
            correctPos.setLine(getHeight() - 1);
        } else if (position.getLine() == getHeight()) {
            correctPos.setLine(0);
        }
        //Correct Columns
        if (position.getCol() == -1) {
            correctPos.setCol(getWidth() - 1);
        } else if (position.getCol() == getWidth()) {
            correctPos.setCol(0);
        }

        return cellMap[position.getLine()][position.getCol()];
    }

    public ArrayList<Position> getEmptyPositions() {
        return freeCellsPositions;
    }

    public ArrayList<Position> getNewAdjacentAvailablePosition(Position position, boolean eatables) {

        ArrayList<Position> adjacentAvailCell = new ArrayList<>();

        for (Dir d : Dir.values()) {

            Position pos = new Position(position.getLine() + d.line, position.getCol() + d.column);

            if (isPositionFree(pos, eatables)) {
                adjacentAvailCell.add(pos);
            }
        }

        return adjacentAvailCell;
    }


    /**
     * Checks if the given position is free
     * @param pos Position to check
     * @param edible True if the position can count as free if it's edible
     * @return True if it's free
     */
    public boolean isPositionFree(Position pos, boolean edible){
        return getCellAt(pos) == null || (edible && (getCellAt(pos) instanceof AppleCell
                || getCellAt(pos) instanceof MouseCell));
    }

    //Produce a random position of a provided list of positions
    public Position getRandomAvailablePosition(ArrayList<Position> list) {
        if (list.size() > 0) {
            return list.remove(random.nextInt(list.size()));
        }
        return null;
    }

    /**
     * Returns all empty directions from the given cell
     * @param current Cell to get directions for
     * @return List of possible directions
     */
    public List<Dir> getAllEmptyDirections(Cell current){
        List<Dir> viableDirs = new ArrayList<>();
        for (Dir direction : directions) {
            if (isPositionFree(getNewPosition(current.getPosition(), direction),false))
                viableDirs.add(direction);
        }
        if (viableDirs.size() > 1) {
            viableDirs.remove(Dir.NONE);
        }

        if (viableDirs.size() == 0)
            viableDirs.add(Dir.NONE);

        return viableDirs;
    }

    /**
     * Returns all empty or food directions from the given cell
     * @param current Cell to get directions for
     * @return List of possible directions
     */
    public List<Dir> getAllEmptyOrFoodDirections(Cell current){
        List<Dir> viableDirs = new ArrayList<>();
        for (Dir direction :directions) {
            if (isPositionFree(getNewPosition(current.getPosition(), direction), true))
                viableDirs.add(direction);
        }
        if (viableDirs.size() > 1) {
            viableDirs.remove(Dir.NONE);
        }
        if (viableDirs.size() == 0)
            viableDirs.add(Dir.NONE);

        return viableDirs;
    }


    /**
     * Returns the new position when going in the provided direction.
     * @param currentLine Current Line value
     * @param currentColumn Current Column value
     * @param direction Direction to go to
     * @return The new position
     */
    public Position getNewPosition(int currentLine, int currentColumn, Dir direction){
        Position newPos = new Position();
        int newLine = currentLine + direction.line;
        int newCol = currentColumn + direction.column;

        if (newLine > getHeight()-1)
            newLine = 0;
        else if (newLine < 0)
            newLine = getHeight()-1;

        if (newCol > getWidth()-1)
            newCol = 0;
        else if (newCol < 0)
            newCol = getWidth()-1;

        newPos.setCol(newCol);
        newPos.setLine(newLine);

        return newPos;
    }


    /**
     * Returns the new position when going in the provided direction.
     * @param currentPosition Current position
     * @param direction Direction to go to
     * @return The new position
     */
    public Position getNewPosition(Position currentPosition, Dir direction){
        return this.getNewPosition(currentPosition.getLine(), currentPosition.getCol(),direction);
    }


    /**
     * Get a random direction that is either empty or occupied by a food cell
     * @param current Current cell
     * @return Viable direction
     */
    public Dir getRandomEmptyOrFoodDirection(Cell current){
        List<Dir> viableDirs = getAllEmptyOrFoodDirections(current);
        return viableDirs.get(random.nextInt(viableDirs.size()));
    }


    public Dir getDirection(Position pos, Position currentPos){

        if(pos==null) return null;

        int l = pos.getLine() - currentPos.getLine();
        int c = pos.getCol() - currentPos.getCol();



        if (l == 0) {
            if (c == 1) {
                return Dir.RIGHT;
            } else {
                return Dir.LEFT;
            }
        } else {
            if (l == 1) {
                return Dir.DOWN;
            } else {
                return Dir.UP;
            }
        }

    }

}
