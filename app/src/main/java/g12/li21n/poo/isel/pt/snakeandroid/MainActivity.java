package g12.li21n.poo.isel.pt.snakeandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import g12.li21n.poo.isel.pt.snakeandroid.Model.Cells.Cell;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Dir;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Game;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Level;
import g12.li21n.poo.isel.pt.snakeandroid.Model.Loader;
import g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles.CellTile;
import g12.li21n.poo.isel.pt.snakeandroid.View.CellTiles.EmptyTile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.AnimTile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Animator;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.OnBeatListener;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.OnTileTouchListener;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.Tile;
import g12.li21n.poo.isel.pt.snakeandroid.View.Tile.TilePanel;

public class MainActivity extends AppCompatActivity {

    //private static final String LEVELS_FILE = "levels.txt";
    private static int LEVELS_FILE = R.raw.levels;          // Name of levels file
    private  TextView  levelNumberView;
    private  TextView applesNumberView;
    private  TextView  scoreNumberView;
    private Game model;                                     // Model of game
    private Level level;                                    // Model of current level
    private TilePanel view;
    private Context context;

    //private TileView tView;
    private static final int STEP_TIME = 500;               // Milliseconds by step
    private boolean escaped = false;
    private boolean paused = false;
    private boolean dragDone = true;
    private int xStart, yStart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        levelNumberView = findViewById(R.id.levelText);
        applesNumberView = findViewById(R.id.appleText);
        scoreNumberView = findViewById(R.id.scoreText);

        this.run(this);
    }


    private void run(MainActivity mainActivity) {

        try (InputStream file = getResources().openRawResource(LEVELS_FILE)) { // Open description file
            model = new Game(file);                                 // Create game model
           // model.setListener(updater);                             // Set listener of game

            view = findViewById(R.id.panel);// Create view for cells
            level = model.loadNextLevel();
            playLevel(mainActivity);
//            while ((level = model.loadNextLevel() ) != null )
//                if (!playLevel(mainActivity) )//|| !win.question("Next level"))
//                {  // Play level
//                    //win.message("Bye.");
//                    return;
//                }

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

        levelNumberView.setText(Integer.toString(level.getNumber()));
        applesNumberView.setText(Integer.toString(level.getRemainingApples()));
        scoreNumberView.setText(Integer.toString(model.getScore()));

        for (int l = 0; l < height; ++l) {                               // Create each tile for each cell
            for (int c = 0; c < width; ++c) {
                view.setTile(c,l, CellTile.tileOf(mainActivity, level.getCell(l, c)));
            }
        }

        level.setObserver(updater);                                     // Set listener of level

        view.setListener(new OnTileTouchListener() {
            @Override
            public boolean onClick(int xTile, int yTile) {
                Log.v("Snake", "onClick");
                return false;
            }

            @Override
            public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
                if (dragDone){ // Início de um novo movimento de swipe
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

                if (xDiff > yDiff){
                    dir = x > xStart ? Dir.RIGHT : Dir.LEFT;
                }
                else {
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

        view.setHeartbeatListener(STEP_TIME, new OnBeatListener() {
            @Override
            public void onBeat(long beat, long time) {
                if (!paused) level.step();
            }
        });


//        while ( !escaped && !level.isFinished() );
        if (escaped || level.snakeIsDead()) return false;
////        win.message("You win");
        return true;                   // Verify win conditions; false: finished without complete
    }

    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer {
        @Override
        public void scoreUpdated(int score) {
            scoreNumberView.setText(model.getScore());
        }
        @Override
        public void cellUpdated(int l, int c, Cell cell) {
            view.invalidate(view.getTile(c,l));
            view.getTile(c,l).setSelect(true);
        }
        @Override
        public void cellCreated(int l, int c, Cell cell) {
            view.setTile(c,l,CellTile.tileOf( context,cell));
            view.invalidate(c,l);
        }
        @Override
        public void cellRemoved(int l, int c) { view.setTile(c,l,new EmptyTile()); }
        @Override
        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell) {
//            animator.floatTile(fromL, fromC,toL, toC ,100);
            Tile tile = view.getTile(fromL,fromC);
            assert !(tile instanceof EmptyTile);
            view.setTile(toC,toL,tile); // TODO: atenção que estas funções do prof esperam receber X, Y e se dermos L,C fica ao contrário!!!!
            view.invalidate(toC, toL);
            cellRemoved(fromL,fromC);
        }
        @Override
        public void applesUpdated(int apples) {
            applesNumberView.setText(Integer.toString(apples));
        }
    }
    private Updater updater = new Updater();

}