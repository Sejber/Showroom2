package robertboschgmbh.test;

/*********************************************************************/
/**  Dateiname: DetailViewActivity.java                             **/
/**                                                                 **/
/**  Beschreibung:  Zeigt den Inhalt eines Projektes  an            **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dataloading.AsyncImageLoader;
import models.*;

public class DetailViewActivity extends AppCompatActivity {

    //Indizes für ViewSets
    private static final int BLOCK_LAYOUT = 0;
    private static final int BLOCK_TITLE = 1;
    private static final int SB1_TEXT = 2;
    private static final int SB1_IMAGE_LAYOUT = 3;
    private static final int SB1_IMAGE = 4;
    private static final int SB1_SUBTITLE = 5;
    private static final int SB1_SCROLLVIEWER = 6;
    private static final int SB2_TEXT = 7;
    private static final int SB2_IMAGE_LAYOUT = 8;
    private static final int SB2_IMAGE = 9;
    private static final int SB2_SUBTITLE = 10;
    private static final int SB2_SCROLLVIEWER = 11;

    private TimerThread timerThread; //Steuert den Bildschirmschoner
    private static boolean foreground = true; //Variable zur Steuerung des Bildschirmschoners

    private ImageView buttonLeft, buttonRight; //Scrollbuttons

    private int leftBlockIndex = 0; //Index des linken Blocks
    private int blockCount = 0; //Anzahl der Blöcke im aktuellen Projekt

    private ProjectModel model; //Aktuelles Projekt

    //Speichert Views
    private SparseArray<View> block1ViewSet = new SparseArray<>();
    private SparseArray<View> block2ViewSet = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ContentView
        setContentView(R.layout.activity_detail_view);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //OnTouchListener zuweisen
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.activity_detail_view);
        rl.setOnTouchListener(touchListener);

        //Homebutton
        ImageButton imageButton1 = (ImageButton)findViewById(R.id.main_screen_top_toolbar_settings);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        //Get the corresponding model for this activity
        Bundle extras = getIntent().getExtras();
        model = (ProjectModel)extras.get("model");

        //Put the image views into variables so we save on findViewById-calls
        buttonLeft = (ImageView) findViewById(R.id.imageButtonLeft);
        buttonRight = (ImageView) findViewById(R.id.imageButtonRight);

        //Get total block counts
        blockCount = model.getBlocks().size();

        //Set the project title
        TextView projectTitleView = (TextView)findViewById(R.id.tvProjectTitle);
        projectTitleView.setText(model.getTitle());

        fillViewSets();

        updateBlocks();
        checkButtonVisibility();

    }

    //Füllt alle Views in die SparseArrays
    private void fillViewSets() {
        //Fill view set for block 1
        block1ViewSet.put(BLOCK_LAYOUT, findViewById(R.id.block1_layout));
        block1ViewSet.put(BLOCK_TITLE, findViewById(R.id.block1_title));
        block1ViewSet.put(SB1_TEXT, findViewById(R.id.block1_sb1_text));
        block1ViewSet.put(SB1_IMAGE_LAYOUT, findViewById(R.id.block1_sb1_imageLayout));
        block1ViewSet.put(SB1_IMAGE, findViewById(R.id.block1_sb1_image));
        block1ViewSet.put(SB1_SUBTITLE, findViewById(R.id.block1_sb1_subtitle));
        block1ViewSet.put(SB1_SCROLLVIEWER, findViewById(R.id.block1_sb1_scrollViewer));
        block1ViewSet.put(SB2_TEXT, findViewById(R.id.block1_sb2_text));
        block1ViewSet.put(SB2_IMAGE_LAYOUT, findViewById(R.id.block1_sb2_imageLayout));
        block1ViewSet.put(SB2_IMAGE, findViewById(R.id.block1_sb2_image));
        block1ViewSet.put(SB2_SUBTITLE, findViewById(R.id.block1_sb2_subtitle));
        block1ViewSet.put(SB2_SCROLLVIEWER, findViewById(R.id.block1_sb2_scrollViewer));

        //Fill view set for block 2
        block2ViewSet.put(BLOCK_LAYOUT, findViewById(R.id.block2_layout));
        block2ViewSet.put(BLOCK_TITLE, findViewById(R.id.block2_title));
        block2ViewSet.put(SB1_TEXT, findViewById(R.id.block2_sb1_text));
        block2ViewSet.put(SB1_IMAGE_LAYOUT, findViewById(R.id.block2_sb1_imageLayout));
        block2ViewSet.put(SB1_IMAGE, findViewById(R.id.block2_sb1_image));
        block2ViewSet.put(SB1_SUBTITLE, findViewById(R.id.block2_sb1_subtitle));
        block2ViewSet.put(SB1_SCROLLVIEWER, findViewById(R.id.block2_sb1_scrollViewer));
        block2ViewSet.put(SB2_TEXT, findViewById(R.id.block2_sb2_text));
        block2ViewSet.put(SB2_IMAGE_LAYOUT, findViewById(R.id.block2_sb2_imageLayout));
        block2ViewSet.put(SB2_IMAGE, findViewById(R.id.block2_sb2_image));
        block2ViewSet.put(SB2_SUBTITLE, findViewById(R.id.block2_sb2_subtitle));
        block2ViewSet.put(SB2_SCROLLVIEWER, findViewById(R.id.block2_sb2_scrollViewer));

    }

    //Füllt Blöcke mit Daten
    private void updateBlocks() {
        //load left block
        if (blockCount > 0) {
            block1ViewSet.get(BLOCK_LAYOUT).setVisibility(View.VISIBLE);
            loadDataFromModel(model.getBlocks().get(leftBlockIndex), block1ViewSet);
        } else {
            block1ViewSet.get(BLOCK_LAYOUT).setVisibility(View.GONE);
        }

        if (blockCount == 1) {
            //only display one block, stretched to the full display width,
            //so make the other block 'gone'
            block2ViewSet.get(BLOCK_LAYOUT).setVisibility(View.GONE);
        } else {
            block2ViewSet.get(BLOCK_LAYOUT).setVisibility(View.VISIBLE);
            loadDataFromModel(model.getBlocks().get(leftBlockIndex + 1), block2ViewSet);
        }

    }

    //Füllt Views von einem Block mit Daten
    private void loadDataFromModel(BlockModel bm, SparseArray<View> viewSet) {

        String blockTitle = bm.getTitle();
        SubBlockModel sb1 = bm.getSubBlock1();
        SubBlockModel sb2 = bm.getSubBlock2();

        TextView blockTitleView = (TextView)viewSet.get(BLOCK_TITLE);
        if (blockTitle != null && !blockTitle.equals("")) {
            blockTitleView.setVisibility(View.VISIBLE);
            blockTitleView.setText(blockTitle);
        } else {
            blockTitleView.setVisibility(View.GONE);
        }

        if (sb1 != null) {
            if (sb1.getType() == SubBlockType.TEXT) {

                viewSet.get(SB1_IMAGE_LAYOUT).setVisibility(View.GONE);
                viewSet.get(SB1_SCROLLVIEWER).setVisibility(View.VISIBLE);

                TextView textView = (TextView)viewSet.get(SB1_TEXT);
                textView.setText(sb1.getText());

                //even though the image view will be invisible for now, reset the image
                //so the GC can collect it
                ((ImageView)viewSet.get(SB1_IMAGE)).setImageBitmap(null);

            } else {

                viewSet.get(SB1_SCROLLVIEWER).setVisibility(View.GONE);
                viewSet.get(SB1_IMAGE_LAYOUT).setVisibility(View.VISIBLE);

                String subtitle = sb1.getSubtitle();
                TextView subtitleView = (TextView) viewSet.get(SB1_SUBTITLE);
                if (subtitle != null && !subtitle.equals("")) {
                    subtitleView.setVisibility(View.VISIBLE);
                    subtitleView.setText(subtitle);
                } else {
                    subtitleView.setVisibility(View.GONE);
                }

                ImageView imageView = (ImageView)viewSet.get(SB1_IMAGE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(null);
                AsyncImageLoader.setImageToImageView(sb1.getImage(), imageView,
                        imageView.getWidth(), imageView.getHeight());

            }
        } else {

            viewSet.get(SB1_SCROLLVIEWER).setVisibility(View.GONE);
            viewSet.get(SB1_IMAGE_LAYOUT).setVisibility(View.GONE);

            //even though the image view will be invisible for now, reset the image
            //so the GC can collect it
            ((ImageView)viewSet.get(SB1_IMAGE)).setImageBitmap(null);
        }

        if (sb2 != null) {
            if (sb2.getType() == SubBlockType.TEXT) {

                viewSet.get(SB2_IMAGE_LAYOUT).setVisibility(View.GONE);
                viewSet.get(SB2_SCROLLVIEWER).setVisibility(View.VISIBLE);

                TextView textView = (TextView)viewSet.get(SB2_TEXT);
                textView.setText(sb2.getText());

                //even though the image view will be invisible for now, reset the image
                //so the GC can collect it
                ((ImageView)viewSet.get(SB2_IMAGE)).setImageBitmap(null);

            } else {

                viewSet.get(SB2_SCROLLVIEWER).setVisibility(View.GONE);
                viewSet.get(SB2_IMAGE_LAYOUT).setVisibility(View.VISIBLE);

                String subtitle = sb2.getSubtitle();
                TextView subtitleView = (TextView) viewSet.get(SB2_SUBTITLE);
                if (subtitle != null && !subtitle.equals("")) {
                    subtitleView.setVisibility(View.VISIBLE);
                    subtitleView.setText(subtitle);
                } else {
                    subtitleView.setVisibility(View.GONE);
                }

                ImageView imageView = (ImageView)viewSet.get(SB2_IMAGE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(null);
                AsyncImageLoader.setImageToImageView(sb2.getImage(), imageView,
                        imageView.getWidth(), imageView.getHeight());

            }
        } else {

            viewSet.get(SB2_SCROLLVIEWER).setVisibility(View.GONE);
            viewSet.get(SB2_IMAGE_LAYOUT).setVisibility(View.GONE);

            //even though the image view will be invisible for now, reset the image
            //so the GC can collect it
            ((ImageView)viewSet.get(SB2_IMAGE)).setImageBitmap(null);
        }

    }

    //Nach links scrollen
    public void swipeLeft(View view) {
        if (leftBlockIndex > 0)
            leftBlockIndex--;

        updateBlocks();
        checkButtonVisibility();
    }

    //Nach rechts scrollen
    public void swipeRight(View view) {
        if (leftBlockIndex < blockCount - 2)
            leftBlockIndex++;

        updateBlocks();
        checkButtonVisibility();
    }

    //check which of the swipe buttons should be visible
    private void checkButtonVisibility() {

        if (leftBlockIndex > 0) {
            buttonLeft.setVisibility(View.VISIBLE);
        } else {
            buttonLeft.setVisibility(View.INVISIBLE);
        }

        if (leftBlockIndex < blockCount - 2) {
            buttonRight.setVisibility(View.VISIBLE);
        } else {
            buttonRight.setVisibility(View.INVISIBLE);
        }

    }

    //Show InfoView
    public void onInfoButtonClicked(View v) {
        new InfoView(this, model).show();
    }

    //Startet den screensaver
    Handler hander = new Handler(){
        public void handleMessage(Message m){
            Intent intent = new Intent (DetailViewActivity.this, screensaver.class);
            startActivity(intent);
            timerThread.interrupt();
        }
    };

    //OnTouchListener zum zurücksetzen des screensaver Timers
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            timerThread.reset();
            return false;
        }
    };

    @Override
    public void onStop(){
        super.onStop();
        foreground = false; //Screensaver Timer stoppen
    }

    @Override
    public void onStart(){
        //Timer starten
        super.onStart();
        foreground = true;
        timerThread = new TimerThread();
        timerThread.setDelay(Integer.parseInt(getResources().getString(R.string.screensaver_delay)));
        timerThread.setContext(this);
        timerThread.start();
    }

    //Screensaver TimerThread
    public class TimerThread extends Thread{
        long delay = 0;
        long endTime;
        DetailViewActivity context;
        public void run(){
            endTime = System.currentTimeMillis()+delay;
            boolean b = false;
            while(System.currentTimeMillis()<endTime&&!b){
                if(context.isDestroyed()||!foreground){
                    b = true;
                }
            }
            if (!b) {
                //Startet den screensaver, wenn die Activity noch läuft und im Vordergrund ist
                hander.sendMessage(new Message());
            }
        }
        public void reset(){
            endTime = System.currentTimeMillis()+delay;
        }
        public void setDelay(long delay){
            this.delay = delay;
        }
        public void setContext(DetailViewActivity context){
            this.context = context;
        }
    }

}
