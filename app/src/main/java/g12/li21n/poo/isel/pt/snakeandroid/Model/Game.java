package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;

public class Game implements Serializable {
    private final InputStream input;
    private int score = 0;
    private int levelNumber = 0;
    private Level curLevel = null;
    private Listener listener = null;


    void addScore(int points) {
        score += points;
        if (listener != null) listener.scoreUpdated(score);
    }


    public Game(InputStream levelsFile) {
        input = levelsFile.markSupported() ? levelsFile : new BufferedInputStream(levelsFile);
        input.mark(40 * 1024);
    }

    public Level loadNextLevel(InputStream levelsFile) throws Loader.LevelFormatException {
        try (InputStream input = new BufferedInputStream(levelsFile);
        Scanner in = new Scanner(input)){
            input.mark(40 * 1024);
            input.reset();
            curLevel = new Loader(in).load(++levelNumber);

            if (curLevel != null) {
                curLevel.init(this);
            }

            input.close();
            return curLevel;
        } catch(Exception e){
            throw new RuntimeException("IOException", e);
        }
    }

    public int getScore() {
        return score;
    }


    public interface Listener {
        void scoreUpdated(int score);
    }

    public void setListener(Listener l) {
        listener = l;
    }
}
