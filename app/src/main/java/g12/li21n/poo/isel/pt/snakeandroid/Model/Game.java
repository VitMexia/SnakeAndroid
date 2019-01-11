package g12.li21n.poo.isel.pt.snakeandroid.Model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Game {
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

    public Level loadNextLevel() throws Loader.LevelFormatException {
        try {
            //input.reset();
            Scanner in = new Scanner(input);
            curLevel = new Loader(in).load(++levelNumber);
            if (curLevel != null) {
                curLevel.init(this);
            }
            return curLevel;
        } catch (Exception e) {
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
