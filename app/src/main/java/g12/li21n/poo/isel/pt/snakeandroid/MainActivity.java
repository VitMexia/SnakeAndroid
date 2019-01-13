package g12.li21n.poo.isel.pt.snakeandroid;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Game;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Level;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Loader;
import g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles.CellTile;
import g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles.EmptyTile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.OnBeatListener;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.OnTileTouchListener;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Tile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.TilePanel;

public class MainActivity extends AppCompatActivity {

    //private static final String LEVELS_FILE = "levels.txt";
    private static int LEVELS_FILE = R.raw.levels;          // Name of levels file
    private TextView levelNumberView;
    private TextView applesNumberView;
    private TextView scoreNumberView;
    private Game model;                                     // Model of game
    private Level level;                                    // Model of current level
    private TilePanel view;
    private Context context;
    private Button okButton;
    private TextView userInfo;

    //private TileView tView;
    private static final int STEP_TIME = 500;               // Milliseconds by step
    private boolean paused = false;
    private boolean dragDone = true;
    private int xStart, yStart;
    private boolean wonLevelGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        levelNumberView = findViewById(R.id.levelText);
        applesNumberView = findViewById(R.id.appleText);
        scoreNumberView = findViewById(R.id.scoreText);
        okButton = findViewById(R.id.okButtonId);
        userInfo = findViewById(R.id.userInfoTextId);
        view = findViewById(R.id.panel);// Create view for cells

        okButton.setVisibility(View.GONE);
        userInfo.setVisibility(View.GONE);

        view.setListener(new OnTileTouchListener() {
            @Override
            public boolean onClick(int xTile, int yTile) {
                Log.v("Snake", "onClick");
                return false;
            }

            @Override
            public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
                if (dragDone) { // Início de um novo movimento de swipe
                    dragDone = false;
                    xStart = xFrom;
                    yStart = yFrom;
                }

                Log.v("Snake", "onDrag from (" + xFrom + "," + yFrom + ") to (" + xTo + "," + yTo + ")");
                return true;
            }

            @Override
            public void onDragEnd(int x, int y) { // Só é chamado quando onDrag retorna true
                dragDone = true;
                int xDiff, yDiff;
                Dir dir = null;

                xDiff = Math.abs(x - xStart);
                yDiff = Math.abs(y - yStart);

                if (xDiff > yDiff) {
                    dir = x > xStart ? Dir.RIGHT : Dir.LEFT;
                } else {
                    dir = y > yStart ? Dir.DOWN : Dir.UP;
                }

                level.setSnakeDirection(dir);

                Log.v("Snake", "onDragEnd: (" + x + "," + y + ") ");
            }

            @Override
            public void onDragCancel() { // Quando o user arrasta para fora da tela
                dragDone = true;
                Log.v("Snake", "onDragCancel");
            }
        });

        if (savedInstanceState != null){
            model = (Game) savedInstanceState.getSerializable("game");
            level = (Level) savedInstanceState.getSerializable("level");
            updater = new Updater();

            model.setListener(updater);
            level.setObserver(updater);

            dragDone = savedInstanceState.getBoolean("dragDone");
            paused = savedInstanceState.getBoolean("paused");
            wonLevelGame = savedInstanceState.getBoolean("wonLevelGame");

            createLevelGraphics(level.getHeight(), level.getWidth());
        }
        else
            this.loadNextLevel();
    }


    private void updateBoards(){
        levelNumberView.setText(Integer.toString(level.getNumber()));
        applesNumberView.setText(Integer.toString(level.getRemainingApples()));
        scoreNumberView.setText(Integer.toString(model.getScore()));
    }

    private void createLevelGraphics(int height, int width){
        level.setObserver(updater);                                     // Set listener of level
        view.setSize(width, height);

        for (int l = 0; l < height; ++l) {                               // Create each tile for each cell
            for (int c = 0; c < width; ++c) {
                view.setTile(c, l, CellTile.tileOf(this.context, level.getCell(l, c)));
            }
        }

        view.setHeartbeatListener(STEP_TIME, new OnBeatListener() {
            @Override
            public void onBeat(long beat, long time) {
                if (!paused) {

                    if (level.isFinished()) {
                        if (!level.snakeIsDead()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.beat_level)
                                    + " " + level.getNumber() + "!", Toast.LENGTH_LONG).show();
                            wonLevelGame = true;
                            displayNextLevelButton();
                        }
                        else
                            finishGame();
                    } else {
                        level.step();
                    }
                }
            }
        });
    }

    private void loadNextLevel() {
        try (InputStream file = getResources().openRawResource(LEVELS_FILE)) { // Open description file
            if (model == null) {
                model = new Game(file);                                 // Create game model
                model.setListener(updater);                             // Set listener of game
            }
            level = model.loadNextLevel(file);

            if (level == null) {
                finishGame();
                return;
            }
            wonLevelGame = false;
        } catch (IOException | Loader.LevelFormatException e) {
            Log.e("Snake", "Error loading level.", e);
        }

        int height = level.getHeight(), width = level.getWidth();

        createLevelGraphics(height, width);
        updateBoards();
    }

    private void displayNextLevelButton() {
        view.removeHeartbeatListener();
        okButton.setVisibility(View.VISIBLE);
        userInfo.setVisibility(View.VISIBLE);

        userInfo.setText(R.string.level_finished);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextLevel();
                okButton.setVisibility(View.GONE);
                userInfo.setVisibility(View.GONE);
            }
        });
    }


    private void finishGame() {
        view.removeHeartbeatListener();
        Intent intent;

        if (wonLevelGame) {
            intent = new Intent(MainActivity.this, VictoryActivity.class);
        } else {
            intent = new Intent(MainActivity.this, DefeatActivity.class);
        }

        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer, Serializable {
        @Override
        public void scoreUpdated(int score) {
            scoreNumberView.setText(Integer.toString(model.getScore()));
        }

        @Override
        public void cellUpdated(int l, int c, Cell cell) {
            view.invalidate(view.getTile(c, l));
            view.getTile(c, l).setSelect(true);
        }

        @Override
        public void cellCreated(int l, int c, Cell cell) {
            view.setTile(c, l, CellTile.tileOf(context, cell));
            view.invalidate(c, l);
        }

        @Override
        public void cellRemoved(int l, int c) {
            view.setTile(c, l, new EmptyTile());
        }

        @Override
        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell) {
            Tile tile = view.getTile(fromC, fromL);
            assert !(tile instanceof EmptyTile);
            view.setTile(toC, toL, tile); // TODO: atenção que estas funções do prof esperam receber X, Y e se dermos L,C fica ao contrário!!!!
            view.invalidate(toC, toL);
            cellRemoved(fromL, fromC);
        }

        @Override
        public void applesUpdated(int apples) {
            applesNumberView.setText(Integer.toString(apples));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
        updateBoards();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", model);
        outState.putSerializable("level", level);

        outState.putBoolean("dragDone", dragDone);
        outState.putBoolean("paused", paused);
        outState.putBoolean("wonLevelGame", wonLevelGame);

        view.removeHeartbeatListener();
    }

    private Updater updater = new Updater();

}