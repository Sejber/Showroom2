package robertboschgmbh.test;

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
    private TimerThread timerThread;
    private static boolean foreground = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
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
                hander.sendMessage(new Message());
            }
        }
        public void reset(){
            endTime = System.currentTimeMillis()+delay;
        }
        public void setDelay(long delay){
            this.delay = delay;
        }
        public void setContext(LoginActivity context){
            this.context = context;
        }
    }

}
