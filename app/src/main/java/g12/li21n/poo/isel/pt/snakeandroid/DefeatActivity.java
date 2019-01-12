package g12.li21n.poo.isel.pt.snakeandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DefeatActivity extends AppCompatActivity {
    private Button closebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defeat);

        closebutton = findViewById(R.id.closebuttonId);

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Goodbye!",Toast.LENGTH_SHORT).show();
                finish();
                //System.exit(0);

            }
        });
    }
}
