package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.util.ArrayList;
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

    public MapHolder(Cell cell[][]) {

        this.cellMap = cell;
        this.height = cell.length;
        this.width = cell[0].length;
        freeCellsPositions = new ArrayList<>();
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


    //TODO: Vitor, please check now :) (precisa rework: não é eficiente, basta manter a lista de posições vazias e ir atualizando a cada adição\remoção (vê o freeCells no meu map manager))
    public ArrayList<Position> getEmptyPositions() {

        return freeCellsPositions;
    }



    public ArrayList<Position> getNewAdjacentAvailablePosition(Position position, boolean eatables) {

        ArrayList<Position> adjacentAvailCell = new ArrayList<>();

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
    public Position getRandomAvailablePosition(ArrayList<Position> list) {

        if (list.size() > 0) {
            return list.remove(random.nextInt(list.size()));
        }
        return null;
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
