package robertboschgmbh.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import dataloading.XmlDataLoader;
import models.BlockModel;
import models.Department;
import models.ProjectModel;


public class NewProjectActivity extends AppCompatActivity {

    private static boolean image = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ContentView
        setContentView(R.layout.activity_new_project);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.newToolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = (Spinner) findViewById(R.id.newSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Speicherbutton
        ImageButton imageButton1 = (ImageButton)findViewById(R.id.newSaveButton);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image){
                    Date date = XmlDataLoader.parseDate(((EditText)findViewById(R.id.newDate)).getText().toString());
                    if (date!=null){
                        String title = ((EditText)findViewById(R.id.newTitle)).getText().toString();
                        Spinner sp = (Spinner)findViewById(R.id.newSpinner);
                        String department = (String)sp.getSelectedItem();
                        Department de = null;
                        if (department.equals("Informatik")){
                            de = Department.IT;
                        }else if(department.equals("Elektrotechnik")){
                            de = Department.ET;
                        }else if(department.equals("Maschinenbau")){
                            de = Department.MB;
                        }else if(department.equals("Mechatronik")){
                            de = Department.MT;
                        }else if(department.equals("Andere")){
                            de = Department.OTHER;
                        }
                        ArrayList<String> members = StringToArray(((EditText)findViewById(R.id.newMembers)).getText().toString());
                        ArrayList<String> tags = StringToArray(((EditText)findViewById(R.id.newTags)).getText().toString());
                        File directory = null;
                        ArrayList<BlockModel> blocks = null;

                        String titleImage = "";
                        //Hier Bild einfügen

                        ProjectModel pm = new ProjectModel(directory,members,title,de,titleImage,date,tags,blocks);
                    }else{
                        Toast.makeText(NewProjectActivity.this,"Ungültiges Datum",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(NewProjectActivity.this,"Das Titelbild muss gesetzt werden!",Toast.LENGTH_LONG).show();
                }
            }

            ArrayList<String> StringToArray(String s){
                ArrayList<String> a = new ArrayList<String>();
                ArrayList indexList = new ArrayList();
                int index = 0;
                int k = 0;
                indexList.add(0,-1);
                while (s.indexOf(",",index)!=-1){
                    index = s.indexOf(",",index);
                    indexList.add(k+1,index);
                    index++;
                    k++;
                }if (k>0){
                    for (int i = 0;i<k;i++){
                        a.add(i,s.substring((int)indexList.get(i)+1,(int)indexList.get(i+1)));
                    }
                    a.add(k,s.substring(s.lastIndexOf(",")+1));
                }else{
                    a.add(0,s);
                }
                return a;
            }
        });

        //EndButton
        ImageButton imageButton2 = (ImageButton)findViewById(R.id.newEndButton);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(NewProjectActivity.this);
                builder.setTitle("Achtung");
                builder.setMessage("Möchten sie das Projekt schließen ohne zu speichern?");

                final View v = view;
                builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NEIN", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });


        Button addButton = (Button)findViewById(R.id.newAddImage);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hier Leos Dateiexplorer einfügen, dann in die Vorschau laden
                //Wenn das funktioniert hat image auf true setzen
            }
        });
    }
}
