package g12.li21n.poo.isel.pt.snakeandroid.Model;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

/**
 * Container class for score board items
 */
public class HighScoreHandler implements Serializable {

    private final String highScoreFileName = "scores.txt";
    public  final List<HighScoreItem> highScoresList;
    private final Context context;
    private int maxScore;
    private int minScore;

    public HighScoreHandler(Context context){
        this.context = context;
        this.highScoresList = getScores();
        maxScore = Integer.MIN_VALUE;
        minScore = Integer.MAX_VALUE;
    }

    /**
     * Load scores from disk file
     * @return The list of score items
     */
    private List<HighScoreItem> getScores() {

        List<HighScoreItem> list = new LinkedList<>();

        try (FileInputStream fileInputStream = context.openFileInput(highScoreFileName);
             Scanner input = new Scanner(fileInputStream))
        {
            int i = 1;

            while(input.hasNextLine()){
                String[] line = input.nextLine().split(",");

                if(line.length%2 != 0) {
                    break;
                }

                HighScoreItem hsItem = new HighScoreItem();
                hsItem.setName(line[0]);
                hsItem.setScore(Integer.parseInt(line[1]));
                hsItem.setPosition(i++);
                list.add(hsItem);

                if (hsItem.getScore() > maxScore)
                    maxScore = hsItem.getScore();
                else if(hsItem.getScore() < minScore)
                    minScore = hsItem.getScore();
            }

        } catch (IOException e) {
            Log.e("IoError", "ProblemSaving", e);
        }

        return list;
    }

    /**
     * Saves current scores to file
     */
    private void saveScores() {
        try (FileOutputStream outputStream = context.openFileOutput(highScoreFileName, MODE_PRIVATE);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream))
        ) {

            for (HighScoreItem hsi : highScoresList) {
                out.println(hsi.toStringToFile());
            }

        } catch (IOException e) {
            Log.e("IoError", "ProblemSaving", e);
        }
    }

    /**
     * Check if a given score makes it into the top 10
     * @param score The score to evaluate
     * @return True if score belongs in top 10
     */
    public boolean isTop10(int score){
        if(score> minScore || highScoresList.size()<10) return true;
        return false;
    }

    /**
     * Updates the List of HighScores
     * @param name Player name associated with the score
     * @param score The amount of points in the score
     */
    public void updateTop10(String name, int score){
        HighScoreItem newHSI = new HighScoreItem(name, score);

        int index = 0;
        boolean added = false;
        for (HighScoreItem hsi : highScoresList) {
            if(hsi.getScore() < newHSI.getScore()) {
                highScoresList.add(index, newHSI);
                added = true;
                break;
            }
            else if(hsi.getScore()==newHSI.getScore()){
                highScoresList.add(index, newHSI);
                added = true;
                break;
            }
            index++;
        }
        if(!added)
            highScoresList.add(newHSI);

        if(highScoresList.size() > 10){
            highScoresList.remove(10);
        }
        saveScores();
    }
}
