package g12.li21n.poo.isel.pt.snakeandroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HighscoresActivity extends AppCompatActivity {
    private ListView scoreList;
    private String[] scores = {"1. ABAS 100", "2. DEF 95", "3. GDSC 89", "4. FAK 89", "5. DIS 85", "6. SHT 81", "7. Nub 50", "8. AIR 32", "9. MBA 30", "10. EXT 29"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        scoreList = findViewById(R.id.scorelistID);

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
