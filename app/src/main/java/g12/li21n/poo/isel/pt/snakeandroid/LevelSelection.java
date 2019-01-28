package g12.li21n.poo.isel.pt.snakeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Activity to allow the user to select which level he intends to play.
 * Generates buttons for each level dynamically as to support level growth without the need to alter this code.
 * User can only play a given level if he has already beat all previous levels.
 */
public class LevelSelection extends AppCompatActivity {
    private int levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);
        LinearLayout linearLayout = findViewById(R.id.scroll_layout_id);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        levels = loadLevelsFromFile();

        if (levels == 0){
            startActivity(new Intent(LevelSelection.this, MainActivity.class));
            finish();
        }

        int[] colorArray = getResources().getIntArray(R.array.androidcolors); // Color array as defined in colors.xml
        ArrayList<Integer> androidColors = new ArrayList<>();

        // Generate color list, retrieved from the color array, so as to not have repeated colors next to each other
        for(int i = 0, j = 0; i <= levels; i++, j++){
            if (j == colorArray.length) // Loop back to the start of the color array if all colors have been used
                j = 0;
            androidColors.add(colorArray[j]);
        }

        // Generate a button for each playable level
        for (int i = 1; i <= levels + 1; i++) {
            Button btn = new Button(this);
            btn.setId(i);
            final int id_ = btn.getId();

            btn.setText(String.format(getResources().getString(R.string.level_text), id_)); // Retrieve formatted button text from resources and set as button text
            btn.setTextColor(getResources().getColor(R.color.buttonTextcolor, getTheme()));

            btn.setBackgroundColor(androidColors.remove(0)); // Retrieve the next color from the color array

            linearLayout.addView(btn, params);
            btn = findViewById(id_);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(LevelSelection.this, MainActivity.class);
                    intent.putExtra("level",id_);
                    startActivity(intent);
                }
            });
        }
    }

    private int loadLevelsFromFile(){
        int levelsVal = 0;
        try (InputStream file = openFileInput("savefile.txt");
             Scanner input = new Scanner(file)) {
            levelsVal = input.nextInt();
        }catch (FileNotFoundException e) {  // If no file exists the user hasn't yet beat any levels
            levelsVal = 0;
        } catch (IOException e) {
            Log.e("Snake", "Level Selection: Error reading save file.", e);
        }
        return levelsVal;
    }
}
