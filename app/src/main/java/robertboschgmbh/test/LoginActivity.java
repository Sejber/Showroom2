package robertboschgmbh.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw = ((EditText)findViewById(R.id.editText)).getText().toString();
                if (pw.equals(getResources().getString(R.string.passwort))){
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.putExtra("admin",true);
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
                startActivity(i);
                finish();
            }
        });

    }
}
