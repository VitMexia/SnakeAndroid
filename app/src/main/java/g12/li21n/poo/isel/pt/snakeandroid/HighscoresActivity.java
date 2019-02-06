package g12.li21n.poo.isel.pt.snakeandroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import g12.li21n.poo.isel.pt.snakeandroid.Model.HighScoreHandler;
import g12.li21n.poo.isel.pt.snakeandroid.Model.HighScoreItem;

public class HighscoresActivity extends AppCompatActivity {
    private ListView scoreList;
    private String[] scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        scoreList = findViewById(R.id.scorelistID);

        HighScoreHandler highScoreHandler = new HighScoreHandler(this);
        scores = new String[highScoreHandler.highScoresList.size()];
        int i= 0;

        for (HighScoreItem hsi: highScoreHandler.highScoresList) {
            scores[i++] = hsi.toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, scores) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(getResources().getDimension(R.dimen.score_text_size));

                return view;
            }
        };

        scoreList.setAdapter(adapter);
    }
}
