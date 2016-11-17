package robertboschgmbh.test;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import dataloading.DataLoader;
import dataloading.XmlDataLoader;
import models.ProjectModel;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ProjectModel> projects;
    private ProjectModelAdapter projectModelAdapter;
    static private boolean admin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        try{

            Bundle extras = getIntent().getExtras();
            if (extras.getBoolean("admin")){
                this.admin = true;
            }else{
                this.admin = false;
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        checkPermission();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (admin){
            getMenuInflater().inflate(R.menu.menu_main2, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }else if(id == R.id.action_settings2){
            Intent i = new Intent(this,MainActivity.class);
            i.putExtra("admin",false);
            startActivity(i);
            finish();
            return true;
        }else if (id == R.id.action_settings3){
            finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {
            Log.d("Main", "Having permission");
            loadData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)     {
                    Log.d("Main", "Got permission");
                    loadData();
                } else {
                    checkPermission();
                }
            }
        }
    }



    private void loadData() {
        GridView gw = (GridView)findViewById(R.id.gridView1);

        DataLoader loader = new XmlDataLoader();
        projects = loader.loadData(Environment.getExternalStorageDirectory());

        projectModelAdapter = new ProjectModelAdapter(this,projects,admin);

        gw.setAdapter(projectModelAdapter);
    }

}
