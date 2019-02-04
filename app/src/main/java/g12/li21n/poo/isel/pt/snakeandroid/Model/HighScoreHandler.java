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


public class HighScoreHandler implements Serializable {

    private final String highScoreFileName = "scores.txt";
    public  final List<HighScoreItem> hsList; //TODO: DOes it need to be public?
    private final Context context;
    private int maxScore;
    private int minScore;

    public HighScoreHandler(Context context){
        this.context = context;
        this.hsList = getScores();

    }

    private List<HighScoreItem> getScores() {

        List<HighScoreItem> list = new LinkedList<>();

        try (FileInputStream fileInputStream = context.openFileInput(highScoreFileName);
             Scanner input = new Scanner(fileInputStream))
        {
            int i = 1;

            while(input.hasNext()){
                String[] line = input.next().split(",");

                if(line == null || line.length%2 != 0) {
                    break;
                }

                HighScoreItem hsItem = new HighScoreItem();
                hsItem.setName(line[0]);
                hsItem.setScore(Integer.parseInt(line[1]));
                hsItem.setPosition(i);
                i+=1;
                list.add(hsItem);

                if (hsItem.getScore()>maxScore) maxScore = hsItem.getScore();
                else if(hsItem.getScore()<minScore)minScore = hsItem.getScore();
            }

        } catch (IOException e) {
            Log.e("IoError", "ProblemSaving", e);
        }

        return list;
    }

    private void saveScores() {
        try (FileOutputStream outputStream = context.openFileOutput(highScoreFileName, MODE_PRIVATE);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream))
        ) {

            //out.println("");
            for (HighScoreItem hsi : hsList) {
                out.println(hsi.toStringToFile());

            }

        } catch (IOException e) {
            Log.e("IoError", "ProblemSaving", e);
        }
    }

    public boolean isTop10(int score){
        if(score> minScore || hsList.size()<10) return true;
        return false;
    }

    public void updateTop10(String name, int score){

        HighScoreItem newHSI = new HighScoreItem(name, score);

        int index = 0;
        boolean added = false;
        for (HighScoreItem hsi : hsList) {

            if(hsi.getScore()<newHSI.getScore()) {
                hsList.add(index, newHSI);
                added = true;
                break;
            }
            else if(hsi.getScore()==newHSI.getScore()){
                hsList.add(index+1, newHSI);
                added = true;
                break;
            }

            index +=1;
        }
        if(!added)hsList.add(newHSI);

        if(hsList.size()>10){
            hsList.remove(10);
        }
        saveScores();
    }
}
