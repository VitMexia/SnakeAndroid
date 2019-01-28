package g12.li21n.poo.isel.pt.snakeandroid;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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

/**
 * Code for game control
 */
public class MainActivity extends AppCompatActivity {
    private static int LEVELS_FILE = R.raw.levels;          // Name of levels file
    private TextView levelNumberView;                       // Level board
    private TextView applesNumberView;                      // Remaining apples board
    private TextView scoreNumberView;                       // Score board
    private Game model;                                     // Model of game
    private Level level;                                    // Model of current level
    private TilePanel view;                                 // Game panel
    private Context context;
    private Button okButton;                                // Button for level transitions
    private TextView userInfo;                              // Text box for user information (level transitions)

    private static final int STEP_TIME = 500;               // Milliseconds by step
    private boolean paused = false;
    private boolean dragDone = true;                        // Auxiliary var for swipe motions
    private int xStart, yStart;                             // Coordinates for swipe motions
    private boolean wonLevelGame;
    private Updater updater;
    private int levelsWon;                                 // Counter for total levels beat by player

    /**
     * Method called when creating the activity. Sets up the game and all it's required elements.
     *
     * @param savedInstanceState Instance state bundle when necessary (i.e. screen flips)
     */
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

        updater = new Updater();

        view.setListener(new OnTileTouchListener() {
            @Override
            public boolean onClick(int xTile, int yTile) {
                return false;
            }

            /**
             * Called when a new swipe movement starts.
             * @param xFrom x coordinate of the tile that was trying to drag
             * @param yFrom y coordinate of the tile that was trying to drag
             * @param xTo x coordinate to drag to
             * @param yTo y coordinate to drag to
             * @return
             */
            @Override
            public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
                if (dragDone) {
                    dragDone = false;
                    xStart = xFrom;
                    yStart = yFrom;
                }

                Log.v("Snake", "onDrag from (" + xFrom + "," + yFrom + ") to (" + xTo + "," + yTo + ")");
                return true;
            }

            /**
             * Listener for swipe end events. If this is called it means the swipe was completed successfully.
             * Based on the direction of the swipe will send a new direction to the game.
             * Since most swipes aren't straight vertical or horizontal the code will detect which axis has the greater delta and calculate direction based on that.
             * @param x x coordinate of last destination of drag
             * @param y y coordinate of last destination of drag
             */
            @Override
            public void onDragEnd(int x, int y) {
                dragDone = true;
                int xDiff, yDiff;
                Dir dir;

                // Compute difference in X and Y axes to determine which has the greater change.
                xDiff = Math.abs(x - xStart);
                yDiff = Math.abs(y - yStart);

                if (xDiff > yDiff) {
                    dir = x > xStart ? Dir.RIGHT : Dir.LEFT;
                } else {
                    dir = y > yStart ? Dir.DOWN : Dir.UP;
                }

                level.setSnakeDirection(dir);
                Log.v("Snake", "onDragEnd: (" + x + "," + y + ") -> Direction: " + dir);
            }

            /**
             * This listener is called when the user swipes to an illegal position (outside the play area) and cancels the motion.
             */
            @Override
            public void onDragCancel() {
                dragDone = true;
                Log.v("Snake", "onDragCancel");
            }
        });

        // Block for restoring the current instance state, if any
        if (savedInstanceState != null) {
            // TODO: colocar estas strings chave todas em resource?
            model = (Game) savedInstanceState.getSerializable("game");
            level = (Level) savedInstanceState.getSerializable("level");

            model.setListener(updater);
            level.setObserver(updater);

            dragDone = savedInstanceState.getBoolean("dragDone");
            paused = savedInstanceState.getBoolean("paused");
            wonLevelGame = savedInstanceState.getBoolean("wonLevelGame");
            levelsWon = savedInstanceState.getInt("levelsWon");

            setupGameView(level.getHeight(), level.getWidth());
        } else {// If no instances then load the level
            Bundle extra = getIntent().getExtras();
            levelsWon = extra != null ? extra.getInt("level") : 0;

            this.loadNextLevel();

        }
    }

    /**
     * Updates the game boards with current level and game state information.
     */
    private void updateBoards() {
        levelNumberView.setText(Integer.toString(level.getNumber()));
        applesNumberView.setText(Integer.toString(level.getRemainingApples()));
        scoreNumberView.setText(Integer.toString(model.getScore()));
    }

    /**
     * Initial setup for the game view painting all of the tiles in the current level.
     *
     * @param height
     * @param width
     */
    private void setupGameView(int height, int width) {
        view.setSize(width, height);            // Set view dimensions

        for (int l = 0; l < height; ++l) {                               // Create each tile for each cell
            for (int c = 0; c < width; ++c) {
                view.setTile(c, l, CellTile.tileOf(this.context, level.getCell(l, c)));
            }
        }

        // Create listener for game beats/turns
        view.setHeartbeatListener(STEP_TIME, new OnBeatListener() {
            @Override
            public void onBeat(long beat, long time) {
                if (!paused) { // Don't run if paused
                    if (level.isFinished()) { // If game is over
                        if (!level.snakeIsDead()) { // Player beat the level
                            Toast.makeText(getApplicationContext(), getString(R.string.beat_level)
                                    + " " + level.getNumber() + "!", Toast.LENGTH_LONG).show();
                            wonLevelGame = true;
                            updatedLevelsWonFile(); // Update save file
                            displayNextLevelButton(); // Show button to start next level
                        } else // Player lost
                            finishGame();
                    } else // Keep playing
                        level.step();
                }
            }
        });
    }

    /**
     * Loads the next level from file and sets up the game view panel.
     */
    private void loadNextLevel() {
        try (InputStream file = getResources().openRawResource(R.raw.levels)) { // Open description file
            if (model == null) {

                Bundle extra = getIntent().getExtras();
                // Create game model
                if (levelsWon > 0)  // If player has already beat some levels
                    model = new Game(file, levelsWon);
                else
                    model = new Game(file);
                model.setListener(updater);                             // Set listener of game
            }
            level = model.loadNextLevel(file);

            if (level == null) { // No more levels on file
                finishGame();
                return;
            }
            wonLevelGame = false;
        } catch (IOException | Loader.LevelFormatException e) {
            Log.e("Snake", "Error loading level.", e);
        }

        int height = level.getHeight(), width = level.getWidth();
        level.setObserver(updater);                                     // Set listener of level
        setupGameView(height, width);                                   // Set up the game view panel
        updateBoards();                                                 // Update score\level\apple boards
    }

    /**
     * Displays and configures the button to switch between levels.
     */
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

    /**
     * Loads the victory or defeat activity based on game state and closes the app.
     */
    private void finishGame() {
        view.removeHeartbeatListener();
        Intent intent;

        if (wonLevelGame) {
            intent = new Intent(MainActivity.this, VictoryActivity.class);
        } else {
            intent = new Intent(MainActivity.this, DefeatActivity.class);
        }

        startActivity(intent);
        finish();

    }

    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer {
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
            view.setTile(toC, toL, tile);
            view.invalidate(toC, toL);
            cellRemoved(fromL, fromC);
        }

        @Override
        public void applesUpdated(int apples) {
            applesNumberView.setText(Integer.toString(apples));
        }
    }

    /**
     * Pause the game when the activity stops
     */
    @Override
    protected void onStop() {
        super.onStop();
        paused = true;
    }

    /**
     * Unpause the game when activity resumes and updated boards.
     */
    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
        updateBoards();
    }

    /**
     * Save the current game state.
     *
     * @param outState Bundle containing relevant information for the current game state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", model);
        outState.putSerializable("level", level);

        outState.putBoolean("dragDone", dragDone);
        outState.putBoolean("paused", paused);
        outState.putBoolean("wonLevelGame", wonLevelGame);
        outState.putInt("levelsWon", levelsWon);

        view.removeHeartbeatListener();
    }

    /**
     * Listener for back button presses. Confirm if the user wants to exit the game.
     */
    @Override
    public void onBackPressed() {
        paused = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.back_button_text_title);
        builder.setMessage(R.string.back_button_text_message);
        builder.setPositiveButton(R.string.back_button_text_confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                });
        builder.setNegativeButton(R.string.back_button_text_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                paused = false;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void updatedLevelsWonFile(){
        try (OutputStream outputStream = openFileOutput("savefile.txt", MODE_PRIVATE);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream))
        ) {
            out.print(levelsWon);
        } catch (IOException e) {
            Log.e("Snake", "Error saving level information to savefile", e);
        }
    }
}