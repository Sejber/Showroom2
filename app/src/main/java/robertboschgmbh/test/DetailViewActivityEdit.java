package robertboschgmbh.test;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dataloading.AsyncImageLoader;
import models.*;

public class DetailViewActivityEdit extends AppCompatActivity {

    private static final int BLOCK_LAYOUT = 0;
    private static final int BLOCK_TITLE = 1;
    private static final int SB1_TEXT = 2;
    private static final int SB1_IMAGE_LAYOUT = 3;
    private static final int SB1_IMAGE = 4;
    private static final int SB1_SUBTITLE = 5;
    private static final int SB2_TEXT = 6;
    private static final int SB2_IMAGE_LAYOUT = 7;
    private static final int SB2_IMAGE = 8;
    private static final int SB2_SUBTITLE = 9;
    private TimerThread timerThread;
    private static boolean foreground = true;

    private ImageView buttonLeft, buttonRight;

    private int leftBlockIndex = 0;
    private int blockCount = 0;

    private ProjectModel model;

    SparseArray<View> block1ViewSet = new SparseArray<>();
    SparseArray<View> block2ViewSet = new SparseArray<>();

    //TODO: Fix that sometimes images aren't getting loaded.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_edit);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.activity_detail_view_edit);
        rl.setOnTouchListener(touchListener);

        //Homebutton
        ImageButton imageButton1 = (ImageButton)findViewById(R.id.main_screen_top_toolbar_settings);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                }
            });

        //Get the corresponding model for this activity
        Bundle extras = getIntent().getExtras();
        model = (ProjectModel)extras.get("model");

        //Put the image views into variables so we save on findViewById-calls
        buttonLeft = (ImageView) findViewById(R.id.imageButtonLeft_edit);
        buttonRight = (ImageView) findViewById(R.id.imageButtonRight_edit);

        //Get total block counts
        blockCount = model.getBlocks().size();

        fillViewSets();

        updateBlocks();
        checkButtonVisibility();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings4){
            
			//adding new Block

            BlockModel newBlock = new BlockModel("Titel(optional)", new SubBlockModel("subtext1"), new SubBlockModel("subtext2"));
            model.addBlock(newBlock);

            blockCount = model.getBlocks().size();

            //Move view so the new block is visible
            leftBlockIndex = blockCount - 2;
            updateBlocks();
            checkButtonVisibility();
			
		}
            return super.onOptionsItemSelected(item);
    }

    private void fillViewSets() {

        //Fill view set for block 1
        block1ViewSet.put(BLOCK_LAYOUT, findViewById(R.id.block1_layout_edit));
        block1ViewSet.put(BLOCK_TITLE, findViewById(R.id.block1_title_edit));
        block1ViewSet.put(SB1_TEXT, findViewById(R.id.block1_sb1_text_edit));
        block1ViewSet.put(SB1_IMAGE_LAYOUT, findViewById(R.id.block1_sb1_imageLayout_edit));
        block1ViewSet.put(SB1_IMAGE, findViewById(R.id.block1_sb1_image_edit));
        block1ViewSet.put(SB1_SUBTITLE, findViewById(R.id.block1_sb1_subtitle_edit));
        block1ViewSet.put(SB2_TEXT, findViewById(R.id.block1_sb2_text_edit));
        block1ViewSet.put(SB2_IMAGE_LAYOUT, findViewById(R.id.block1_sb2_imageLayout_edit));
        block1ViewSet.put(SB2_IMAGE, findViewById(R.id.block1_sb2_image_edit));
        block1ViewSet.put(SB2_SUBTITLE, findViewById(R.id.block1_sb2_subtitle_edit));

        //Fill view set for block 2
        block2ViewSet.put(BLOCK_LAYOUT, findViewById(R.id.block2_layout_edit));
        block2ViewSet.put(BLOCK_TITLE, findViewById(R.id.block2_title_edit));
        block2ViewSet.put(SB1_TEXT, findViewById(R.id.block2_sb1_text_edit));
        block2ViewSet.put(SB1_IMAGE_LAYOUT, findViewById(R.id.block2_sb1_imageLayout_edit));
        block2ViewSet.put(SB1_IMAGE, findViewById(R.id.block2_sb1_image_edit));
        block2ViewSet.put(SB1_SUBTITLE, findViewById(R.id.block2_sb1_subtitle_edit));
        block2ViewSet.put(SB2_TEXT, findViewById(R.id.block2_sb2_text_edit));
        block2ViewSet.put(SB2_IMAGE_LAYOUT, findViewById(R.id.block2_sb2_imageLayout_edit));
        block2ViewSet.put(SB2_IMAGE, findViewById(R.id.block2_sb2_image_edit));
        block2ViewSet.put(SB2_SUBTITLE, findViewById(R.id.block2_sb2_subtitle_edit));

    }

    private void updateBlocks() {

        //load left block
        loadDataFromModel(model.getBlocks().get(leftBlockIndex), block1ViewSet);

        if (blockCount == 1) {
            //only display one block, stretched to the full display width,
            //so make the other block 'gone'
            block2ViewSet.get(BLOCK_LAYOUT).setVisibility(View.GONE);
        } else {

            loadDataFromModel(model.getBlocks().get(leftBlockIndex + 1), block2ViewSet);


        }

    }


    private void loadDataFromModel(BlockModel bm, SparseArray<View> viewSet) {

        String blockTitle = bm.getTitle();
        SubBlockModel sb1 = bm.getSubBlock1();
        SubBlockModel sb2 = bm.getSubBlock2();

        EditText blockTitleView = (EditText)viewSet.get(BLOCK_TITLE);
        if (blockTitle != null && !blockTitle.equals("")) {
            blockTitleView.setVisibility(View.VISIBLE);
            blockTitleView.setText(blockTitle);
        } else {
            blockTitleView.setVisibility(View.GONE);
        }

        if (sb1 != null) {
            if (sb1.getType() == SubBlockType.TEXT) {

                viewSet.get(SB1_IMAGE_LAYOUT).setVisibility(View.GONE);

                EditText textView = (EditText)viewSet.get(SB1_TEXT);
                textView.setVisibility(View.VISIBLE);
                textView.setText(sb1.getText());

                //even though the image view will be invisible for now, reset the image
                //so the GC can collect it
                ((ImageView)viewSet.get(SB1_IMAGE)).setImageBitmap(null);

            } else {

                viewSet.get(SB1_TEXT).setVisibility(View.GONE);
                viewSet.get(SB1_IMAGE_LAYOUT).setVisibility(View.VISIBLE);

                String subtitle = sb1.getSubtitle();
                EditText subtitleView = (EditText) viewSet.get(SB1_SUBTITLE);
                if (subtitle != null && !subtitle.equals("")) {
                    subtitleView.setVisibility(View.VISIBLE);
                    subtitleView.setText(subtitle);
                } else {
                    subtitleView.setVisibility(View.GONE);
                }

                ImageView imageView = (ImageView)viewSet.get(SB2_IMAGE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(null);
                AsyncImageLoader.setImageToImageView(sb1.getImage(), imageView,
                        imageView.getWidth(), imageView.getHeight());

            }
        } else {

            viewSet.get(SB1_TEXT).setVisibility(View.GONE);
            viewSet.get(SB1_IMAGE_LAYOUT).setVisibility(View.GONE);

            //even though the image view will be invisible for now, reset the image
            //so the GC can collect it
            ((ImageView)viewSet.get(SB1_IMAGE)).setImageBitmap(null);
        }

        if (sb2 != null) {
            if (sb2.getType() == SubBlockType.TEXT) {

                viewSet.get(SB2_IMAGE_LAYOUT).setVisibility(View.GONE);

                EditText textView = (EditText)viewSet.get(SB2_TEXT);
                textView.setVisibility(View.VISIBLE);
                textView.setText(sb2.getText());

                //even though the image view will be invisible for now, reset the image
                //so the GC can collect it
                ((ImageView)viewSet.get(SB2_IMAGE)).setImageBitmap(null);

            } else {

                viewSet.get(SB2_TEXT).setVisibility(View.GONE);
                viewSet.get(SB2_IMAGE_LAYOUT).setVisibility(View.VISIBLE);

                String subtitle = sb2.getSubtitle();
                EditText subtitleView = (EditText) viewSet.get(SB2_SUBTITLE);
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

            viewSet.get(SB2_TEXT).setVisibility(View.GONE);
            viewSet.get(SB2_IMAGE_LAYOUT).setVisibility(View.GONE);

            //even though the image view will be invisible for now, reset the image
            //so the GC can collect it
            ((ImageView)viewSet.get(SB2_IMAGE)).setImageBitmap(null);
        }

    }

    public void saveBlocks() {

        EditText blockTitleView1 = (EditText)block1ViewSet.get(BLOCK_TITLE);
        model.getBlocks().get(leftBlockIndex).setTitle( blockTitleView1.getText().toString());

        if(model.getBlocks().get(leftBlockIndex).getSubBlock1().getType()==SubBlockType.TEXT) {

            EditText textView1 = (EditText)block1ViewSet.get(SB1_TEXT);
            model.getBlocks().get(leftBlockIndex).getSubBlock1().setText(textView1.getText().toString());

        } else {

            EditText subtitleView1 = (EditText) block1ViewSet.get(SB1_SUBTITLE);
            model.getBlocks().get(leftBlockIndex).getSubBlock1().setSubtitle(subtitleView1.getText().toString());

        }

        if(model.getBlocks().get(leftBlockIndex).getSubBlock2().getType()==SubBlockType.TEXT) {

            EditText textView2 = (EditText)block1ViewSet.get(SB2_TEXT);
            model.getBlocks().get(leftBlockIndex).getSubBlock2().setText(textView2.getText().toString());

        } else {

            EditText subtitleView2 = (EditText) block1ViewSet.get(SB2_SUBTITLE);
            model.getBlocks().get(leftBlockIndex).getSubBlock2().setSubtitle(subtitleView2.getText().toString());

        }


        if(blockCount>1) {


            EditText blockTitleView2 = (EditText)block2ViewSet.get(BLOCK_TITLE);
            model.getBlocks().get(leftBlockIndex+1).setTitle( blockTitleView2.getText().toString());

            if(model.getBlocks().get(leftBlockIndex+1).getSubBlock1().getType()==SubBlockType.TEXT) {

                EditText textView3 = (EditText)block2ViewSet.get(SB1_TEXT);
                model.getBlocks().get(leftBlockIndex+1).getSubBlock1().setText(textView3.getText().toString());

            } else {

                EditText subtitleView3 = (EditText) block2ViewSet.get(SB1_SUBTITLE);
                model.getBlocks().get(leftBlockIndex+1).getSubBlock1().setSubtitle(subtitleView3.getText().toString());

            }

            if(model.getBlocks().get(leftBlockIndex+1).getSubBlock2().getType()==SubBlockType.TEXT) {

                EditText textView4 = (EditText)block2ViewSet.get(SB2_TEXT);
                model.getBlocks().get(leftBlockIndex+1).getSubBlock2().setText(textView4.getText().toString());

            } else {

                EditText subtitleView4 = (EditText) block2ViewSet.get(SB2_SUBTITLE);
                model.getBlocks().get(leftBlockIndex+1).getSubBlock2().setSubtitle(subtitleView4.getText().toString());

            }
        }

    }


    public void swipeLeft(View view) {

        saveBlocks();

        if (leftBlockIndex > 0)
            leftBlockIndex--;

        updateBlocks();
        checkButtonVisibility();
    }

    public void swipeRight(View view) {

        saveBlocks();

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


    Handler hander = new Handler(){
        public void handleMessage(Message m){
            Intent intent = new Intent (DetailViewActivityEdit.this, screensaver.class);
            startActivity(intent);
            timerThread.interrupt();
        }
    };

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
        foreground = false;
    }

    @Override
    public void onStart(){
        super.onStart();
        foreground = true;
        timerThread = new TimerThread();
        timerThread.setDelay(Integer.parseInt(getResources().getString(R.string.screensaver_delay)) * 60000  );
        timerThread.setContext(this);
        timerThread.start();
    }

    public class TimerThread extends Thread{
        long delay = 0;
        long endTime;
        DetailViewActivityEdit context;
        public void run(){
            endTime = System.currentTimeMillis()+delay;
            boolean b = false;
            while(System.currentTimeMillis()<endTime&&!b){
                if(context.isDestroyed()||!foreground){
                    b = true;
                }
            }
            if (!b) {
                hander.sendMessage(new Message());
            }
        }
        public void reset(){
            endTime = System.currentTimeMillis()+delay;
        }
        public void setDelay(long delay){
            this.delay = delay;
        }
        public void setContext(DetailViewActivityEdit context){
            this.context = context;
        }
    }
}
