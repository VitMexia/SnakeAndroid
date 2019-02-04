package g12.li21n.poo.isel.pt.snakeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import g12.li21n.poo.isel.pt.snakeandroid.Model.HighScoreHandler;
import g12.li21n.poo.isel.pt.snakeandroid.Model.HighScoreItem;

public class InsertNewScoreActivity extends AppCompatActivity {
    private Button confirmButton;
    private EditText editNameTextbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_score);

        final HighScoreHandler highScoreHandler = new HighScoreHandler(this);

        final int newScore = (int)getIntent().getSerializableExtra("highScore");

        editNameTextbox = findViewById(R.id.nameTextboxId);
        confirmButton = findViewById(R.id.confirmButtonId);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNameTextbox.getText().length() < 3)
                    Toast.makeText(getApplicationContext(), getString(R.string.name_error_text), Toast.LENGTH_LONG).show();

                else{
                    highScoreHandler.updateTop10(editNameTextbox.getText().toString(), newScore);
                    Intent intent = new Intent(InsertNewScoreActivity.this, HighscoresActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
