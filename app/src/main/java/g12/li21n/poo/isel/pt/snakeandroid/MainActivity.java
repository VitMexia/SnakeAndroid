package g12.li21n.poo.isel.pt.snakeandroid;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Game;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Level;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Loader;
import g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles.CellTile;
import g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles.EmptyTile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Tile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.TilePanel;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.TileView;

public class MainActivity extends AppCompatActivity {

    //private static final String LEVELS_FILE = "levels.txt";
    private static int LEVELS_FILE = R.raw.levels;          // Name of levels file
    private  TextView  levelNumberView;
    private  TextView applesNumberView;
    private  TextView  scoreNumberView;
    private Game model;                                     // Model of game
    private Level level;                                    // Model of current level
    private TilePanel view;                                 // View of cells
    //private TileView tView;
    private static final int STEP_TIME = 300;               // Milliseconds by step
    private long time;                                      // Current time for next step
    private boolean escaped = false;
    private boolean paused = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        levelNumberView = findViewById(R.id.levelText);
        applesNumberView = findViewById(R.id.appleText);
        scoreNumberView = findViewById(R.id.scoreText);


        this.run(this);


//
//        InputStream is = getResources().openRawResource(LEVELS_FILE);
//
//        final TilePanel panel = findViewById(R.id.panel);
//        for(int hIdx = 0; hIdx < panel.getHeightInTiles(); ++hIdx)
//            for(int wIdx = 0; wIdx < panel.getWidthInTiles(); ++wIdx)
//
        //panel.setTile(wIdx, hIdx, new MyTile((hIdx + wIdx) % 2 == 0));
    }


    private void run(MainActivity mainActivity) {

        try (InputStream file = getResources().openRawResource(LEVELS_FILE)) { // Open description file
            model = new Game(file);                                 // Create game model
            model.setListener(updater);                             // Set listener of game
            //tView = new TileView(mainActivity);

//            level = model.loadNextLevel();
//            playLevel(mainActivity);

            while ((level = model.loadNextLevel()) != null)       // Load level model
                if (!playLevel(mainActivity) )//|| !win.question("Next level"))
                {  // Play level
                   // win.message("Bye.");
                    return;
                }
           // win.message("No more Levels");
        }
        catch (Loader.LevelFormatException e){
            Log.v("test", "LevelFormatException");
            e.printStackTrace();
        }

        catch (IOException e) {
            Log.v("test", "IOException");
            e.printStackTrace();

        } finally {
            Log.v("test", "Finally");
        }                                    // Close console window
    }

    /**
     * Main loop of each level.
     * @return true - the level has been completed. false - the player has given up.
     * @param mainActivity
     */
    private boolean playLevel(MainActivity mainActivity) {
        // Opens panel of tiles with dimensions appropriate to the current level.
        // Starts the viewer for each model cell.
        // Shows the initial state of all cells in the model.
        int height = level.getHeight(), width = level.getWidth();


        view = findViewById(R.id.panel);// Create view for cells
        view.setSize(width, height);
        view.getAnimator();
//        win.clear();                                                    // Clear area of previous level
//        view.center(WIN_HEIGHT,WIN_WIDTH);
// Center view in area

//        levelNumberView.setText(level.getNumber());
//        applesNumberView.setText(level.getRemainingApples());
//        scoreNumberView.setText(model.getScore());



//        for (int l = 0; l < height; ++l) {                               // Create each tile for each cell
//            for (int c = 0; c < width; ++c) {
//
//                TileView tView = new TileView(mainActivity);
//
//                view.setTile(l, c, CellTile.tileOf(mainActivity, level.getCell(c, l)));
//                tView.setTile(CellTile.tileOf(mainActivity, level.getCell(c, l)));
//
//            }
//        }
        level.setObserver(updater);                                     // Set listener of level
        time = System.currentTimeMillis();                              // Set step time
        do
            play();

        while ( !escaped && !level.isFinished() );
//        if (escaped || level.snakeIsDead()) return false;
//        win.message("You win");
        return true;                   // Verify win conditions; false: finished without complete
    }

    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer {
        // Game.Listener
        @Override
        public void scoreUpdated(int score) { //status.setScore( score );
        }
        // Level.Listener
        @Override
        public void cellUpdated(int l, int c, Cell cell) { view.getTile(l,c).setSelect(true); }
        @Override
        public void cellCreated(int l, int c, Cell cell) {
            //view.setTile(l,c,CellTile.tileOf( cell));
        }
        @Override
        public void cellRemoved(int l, int c) { view.setTile(l,c,new EmptyTile()); }
        @Override
        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell) {
            Tile tile = view.getTile(fromL,fromC);
            assert !(tile instanceof EmptyTile);
            view.setTile(toL,toC,tile);
            cellRemoved(fromL,fromC);
        }
        @Override
        public void applesUpdated(int apples) { //status.setApples(apples);
        }
    }
    private Updater updater = new Updater();

    private void play() {
        time += STEP_TIME;                  // Adjust step time


        if (true) {
            Dir dir = null;


            if (dir!=null) level.setSnakeDirection(dir);
        }
        if (!paused) level.step();
    }
}