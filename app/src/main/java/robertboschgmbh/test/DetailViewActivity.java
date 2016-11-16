package robertboschgmbh.test;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import models.*;

public class DetailViewActivity extends AppCompatActivity {

    private HorizontalScrollView scroll;
    private ImageView buttonLeft, buttonRight;

    private int scrollPosition = 0;
    private int elementWidth = 0;
    private int blockSize = 0;

    private ProjectModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        //mitgeliferte Projektdtaen auslesen

        Bundle extras = getIntent().getExtras();
        model = (ProjectModel)extras.get("model");

        //manuelles Scrollen unterdrücken

        scroll = (HorizontalScrollView) findViewById(R.id.scrollView1);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //sichtbarkeit der Buttons überprüfen

        buttonLeft = (ImageView) findViewById(R.id.imageButtonLeft);
        buttonRight = (ImageView) findViewById(R.id.imageButtonRight);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.HORIZONTAL);

        //Anzahl Blocks auslesen und deren Weite bestimmen

        blockSize = model.getBlocks().size();
        elementWidth = getElementWidth();

        //Blöcke dynamisch laden und hinzufügen:

        for (int i = 0; i < blockSize; i++) {

            LinearLayout block = new LinearLayout(this);
            block.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(elementWidth,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f);
            block.setLayoutParams(params);
            block.setPadding(20, 20, 20, 20);

            TextView headline = new TextView(this);
            headline.setTextSize(30);
            headline.setTextColor(Color.WHITE);
            headline.setText(model.getBlocks().get(i).getTitle());
            headline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f));
            headline.setPadding(20, 20, 20, 20);
            block.addView(headline);


            LinearLayout subBlock1 = new LinearLayout(this);
            subBlock1.setOrientation(LinearLayout.VERTICAL);
            subBlock1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            subBlock1.setPadding(20, 20, 20, 20);

            if (model.getBlocks().get(i).getSubBlock1().getType() == SubBlockType.TEXT) {

                TextView text1 = new TextView(this);
                text1.setText(model.getBlocks().get(i).getSubBlock1().getText());
                text1.setTextColor(Color.WHITE);
                text1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                subBlock1.addView(text1);

            } else {
                ImageView img1 = new ImageView(this);
                img1.setImageResource(R.drawable.back1920x1080);

                img1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

                subBlock1.addView(img1);

                //Bildunterschrift hinzufügen
            }

            LinearLayout subBlock2 = new LinearLayout(this);
            subBlock2.setOrientation(LinearLayout.VERTICAL);
            subBlock2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            subBlock2.setPadding(20, 20, 20, 20);

            if (model.getBlocks().get(i).getSubBlock2().getType() == SubBlockType.TEXT) {
                TextView text2 = new TextView(this);
                text2.setText(model.getBlocks().get(i).getSubBlock2().getText());
                text2.setTextColor(Color.WHITE);
                text2.setTextIsSelectable(true);
                text2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                subBlock2.addView(text2);
            } else {
                ImageView img2 = new ImageView(this);
                img2.setImageResource(R.drawable.logo);
                img2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                subBlock2.addView(img2);
            }


            block.addView(subBlock1);
            block.addView(subBlock2);

            content.addView(block);
        }
        scroll.addView(content);

        checkButtonVisibility();

    }


    //Button links: verschiebt Position im Scrollpanel um 1 nach links

    protected void swipeLeft(View view) {

        if (scrollPosition > 0) {
            scrollPosition--;
            scroll.smoothScrollTo(scrollPosition * elementWidth, 0);
        }
        checkButtonVisibility();
    }

    //Button rechts: verschiebt Position im Scrollpanel um 1 nach rechts

    protected void swipeRight(View view) {

        if (scrollPosition < blockSize - 2) {
            scrollPosition++;
            scroll.smoothScrollTo(scrollPosition * elementWidth, 0);
        }
        checkButtonVisibility();
    }

    //überprüft ob Button sichtbar oder nicht sichtbar sein sollen

    private void checkButtonVisibility() {

        if (scrollPosition == 0) {
            buttonLeft.setVisibility(View.INVISIBLE);
        } else {
            buttonLeft.setVisibility(View.VISIBLE);
        }

        if (scrollPosition == blockSize - 2) {
            buttonRight.setVisibility(View.INVISIBLE);
        } else {
            buttonRight.setVisibility(View.VISIBLE);
        }
    }

    private int getElementWidth() {

        Display display = getWindowManager().getDefaultDisplay();       //Displaygröße auslesen
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        width = width-20;                                   //Padding von Hauptlayout abziehen
        double faktor = 5.0/12.0;                           //Faktor betsimmt sich aus weigth der Elemente (Scrollview hat 5/6)
        double widthTemp =  (double)width*faktor;           //ElementWidth in double
        width = (int)widthTemp;                             //in int

        return width;
    }
}
