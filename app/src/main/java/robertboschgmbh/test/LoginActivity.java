package robertboschgmbh.test;

/*********************************************************************/
/**  Dateiname: LoginActivity.java                                  **/
/**                                                                 **/
/**  Beschreibung:  Passwortabfrage für den Admin Modus             **/
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TimerThread timerThread; //Steuert den Bildschirmschoner
    private static boolean foreground = true; //Variable zur Steuerung des Bildschirmschoners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ContentView
        setContentView(R.layout.activity_login);

        //Anmeldebutten Passwortüberprüfung
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw = ((EditText)findViewById(R.id.editText)).getText().toString();
                if (pw.equals(getResources().getString(R.string.passwort))){
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.putExtra("admin",true);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"Falsches Passwort",Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.editText)).setText("");
                }
            }
        });

        //Abbrechen und zurückkehren zur MainActivity
        Button cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                i.putExtra("admin",false);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        });

    }

    //Startet den screensaver
    Handler hander = new Handler(){
        public void handleMessage(Message m){
            Intent intent = new Intent (LoginActivity.this, screensaver.class);
            startActivity(intent);
            timerThread.interrupt();
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
        timerThread.setDelay(Integer.parseInt(getResources().getString(R.string.screensaver_delay)) );
        timerThread.setContext(this);
        timerThread.start();
    }

    //Screensaver TimerThread
    public class TimerThread extends Thread{
        long delay = 0;
        long endTime;
        LoginActivity context;
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
        public void setDelay(long delay){
            this.delay = delay;
        }
        public void setContext(LoginActivity context){
            this.context = context;
        }
    }

}
