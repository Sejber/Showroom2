package robertboschgmbh.test;

/*********************************************************************/
/**  Dateiname: screensaver.java                                    **/
/**                                                                 **/
/**  Beschreibung:  Zeigt den Bildschirmschoner an                  **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class screensaver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ContentView
        setContentView(R.layout.activity_screensaver);

        //Schließen bei Berührung
        ImageView v = (ImageView)findViewById(R.id.screensaverView);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });



    }
}
