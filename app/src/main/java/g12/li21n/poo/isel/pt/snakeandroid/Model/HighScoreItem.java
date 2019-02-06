package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.io.Serializable;

/**
 * Represents an entry for the score board
 */
public class HighScoreItem implements Serializable {

    private String name;
    private int score;
    private int position;

    public HighScoreItem(){}

    public HighScoreItem(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String toStringToFile(){
        return name + "," + score;
    }

    @Override
    public String toString() {
        String format = "%-6s%-15s%2s";;
        return String.format(format, position,name, score);
    }
}
