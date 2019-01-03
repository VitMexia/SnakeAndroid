package g12.li21n.poo.isel.pt.snakeandroid.Model;

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

    public MapHolder(Cell cell[][]) {

        this.cellMap = cell;
        this.height = cell.length;
        this.width = cell[0].length;
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
    }

    public void clearCellAt(Position pos) {
        cellMap[pos.getLine()][pos.getCol()] = null;
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
            correctPos.setLine(getWidth() - 1);
        } else if (position.getCol() == getWidth()) {
            correctPos.setCol(0);
        }

        return cellMap[position.getLine()][position.getCol()];
    }

    public LinkedList<Position> getEmptyPositions() {

        LinkedList<Position> positions = new LinkedList<>();

        for (int col = 0; col < width; col++) {
            for (int line = 0; line < height; line++) {
                if (cellMap[line][col] == null) {
                    positions.add(new Position(line, col));
                }
            }
        }
        return positions;
    }

    public List<Position> getNewAdjacentAvailablePosition(Position position, boolean eatables) {

        List<Position> adjacentAvailCell = new LinkedList<>();

        for (Dir d : Dir.values()) {

            Position pos = new Position(position.getLine() + d.line, position.getCol() + d.column);

            if (getCellAt(pos) == null || eatables && (getCellAt(pos) instanceof AppleCell
                    || getCellAt(pos) instanceof MouseCell)) {
                adjacentAvailCell.add(pos);
            }
        }

        return adjacentAvailCell;
    }

    //Produce a random position of a provided list of positions
    public Position getRandomAvailablePosition(List<Position> list) {

        if (list.size() > 0) {
            return list.remove(random.nextInt(list.size()));
        }
        return null;
    }

}
