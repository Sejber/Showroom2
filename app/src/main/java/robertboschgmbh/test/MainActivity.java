package robertboschgmbh.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.TabHost;
import models.Department;
import models.DepartmentToStringConverter;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import dataloading.XmlDataManager;
import models.ProjectModel;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ProjectModel> projects;
    static private boolean admin = false;
    private TimerThread timerThread;
    private static boolean foreground = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        timerThread.setDelay(Integer.parseInt(getResources().getString(R.string.screensaver_delay)) * 60000);
        timerThread.start();

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

        TabHost host = (TabHost)findViewById(R.id.tabhost_studiengang);
        host.setup();

        //Tab 'Alle'
        TabHost.TabSpec spec = host.newTabSpec("Alle");
        spec.setContent(R.id.tab_alle);
        spec.setIndicator("Alle");
        host.addTab(spec);
        //Tab ET
        spec = host.newTabSpec(DepartmentToStringConverter.convertToString(Department.ET));
        spec.setContent(R.id.tab_et);
        spec.setIndicator(DepartmentToStringConverter.convertToString(Department.ET));
        host.addTab(spec);
        //Tab IT
        spec = host.newTabSpec(DepartmentToStringConverter.convertToString(Department.IT));
        spec.setContent(R.id.tab_it);
        spec.setIndicator(DepartmentToStringConverter.convertToString(Department.IT));
        host.addTab(spec);
        //Tab MB
        spec = host.newTabSpec(DepartmentToStringConverter.convertToString(Department.MB));
        spec.setContent(R.id.tab_mb);
        spec.setIndicator(DepartmentToStringConverter.convertToString(Department.MB));
        host.addTab(spec);
        //Tab MT
        spec = host.newTabSpec(DepartmentToStringConverter.convertToString(Department.MT));
        spec.setContent(R.id.tab_mt);
        spec.setIndicator(DepartmentToStringConverter.convertToString(Department.MT));
        host.addTab(spec);

    }


    Handler hander = new Handler(){
        public void handleMessage(Message m){
            Intent intent = new Intent (MainActivity.this, screensaver.class);
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
        timerThread.setDelay(Integer.parseInt(getResources().getString(R.string.screensaver_delay)) * 60000 );
        timerThread.setContext(this);
        timerThread.start();
    }

    public class TimerThread extends Thread{
        long delay = 0;
        long endTime;
        MainActivity context;
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
        public void setContext(MainActivity context){
            this.context = context;
        }
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
            //timerThread.interrupt();
            finish();
            return true;
        }else if(id == R.id.action_settings2){
            Intent i = new Intent(this,MainActivity.class);
            i.putExtra("admin",false);
            startActivity(i);
            timerThread.interrupt();
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
        GridView gw = (GridView)findViewById(R.id.gridView_alle);
        GridView gwET = (GridView)findViewById(R.id.gridView_et);
        GridView gwIT = (GridView)findViewById(R.id.gridView_it);
        GridView gwMB = (GridView)findViewById(R.id.gridView_mb);
        GridView gwMT = (GridView)findViewById(R.id.gridView_mt);

        gw.setOnTouchListener(touchListener);
        gwET.setOnTouchListener(touchListener);
        gwIT.setOnTouchListener(touchListener);
        gwMB.setOnTouchListener(touchListener);
        gwMT.setOnTouchListener(touchListener);

        projects = XmlDataManager.loadProjects(Environment.getExternalStorageDirectory());
        gw.setAdapter(new ProjectModelAdapter(this,projects,admin));

        ArrayList<ProjectModel> lProjects = getProjectsOfDepartment(Department.ET);
        gwET.setAdapter(new ProjectModelAdapter(this,lProjects,admin));
        lProjects = getProjectsOfDepartment(Department.IT);
        gwIT.setAdapter(new ProjectModelAdapter(this,lProjects,admin));
        lProjects = getProjectsOfDepartment(Department.MB);
        gwMB.setAdapter(new ProjectModelAdapter(this,lProjects,admin));
        lProjects = getProjectsOfDepartment(Department.MT);
        gwMT.setAdapter(new ProjectModelAdapter(this,lProjects,admin));
    }

    private ArrayList<ProjectModel> getProjectsOfDepartment(Department dep) {
        ArrayList<ProjectModel> projectModels = new ArrayList<>();

        for(ProjectModel prj : projects) {
            if(prj.getDepartment().equals(dep)) {
                projectModels.add(prj);
            }
        }

        return projectModels;
    }

}
