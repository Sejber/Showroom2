package robertboschgmbh.test;

/*********************************************************************/
/**  Dateiname: ProjectModelAdapter.java                            **/
/**                                                                 **/
/**  Beschreibung:  Bindeglied zwischen GridView und ihrer Inhalte  **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import dataloading.AsyncImageLoader;
import dataloading.XmlDataManager;
import models.Department;
import models.DepartmentToStringConverter;
import models.ProjectModel;

import static android.support.v4.content.ContextCompat.startActivity;


public class ProjectModelAdapter extends ArrayAdapter<models.ProjectModel> {

    static private boolean admin = false; //Admin Modus Umschalter

    //Kunstruktor
    public ProjectModelAdapter(Context context, ArrayList<models.ProjectModel> projectModels,boolean admin){
        super(context,0,projectModels);
        this.admin = admin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final ProjectModel projectModel = getItem(position); //Aktuelles ProjectModel

        //Neues Column erstellen falls nicht schon vorhanden
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_column,parent,false);
        }

        TextView noteTitle = (TextView) convertView.findViewById(R.id.listItemNoteTitle); //Projekttitel
        TextView noteText = (TextView) convertView.findViewById(R.id.listItemNoteBody); //Projektfachrichtung
        ImageView noteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteImg); //ProjektBild
        ImageView deleteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteDelete); //DeleteButton
        ImageView editIcon = (ImageView) convertView.findViewById(R.id.listItemNoteEdit); //EditButton
        TextView noteDate = (TextView) convertView.findViewById(R.id.listItemNoteDate); //Projektdatum

        //Layout , dass Adminfunktionen beinhaltet gegebenenfalls deaktivieren
        if(!admin){
            RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.layoutAdmin);
            rl.setVisibility(View.INVISIBLE);
        }

        //Jedem klickbaren Item sein ProjectModel anhängen
        noteIcon.setTag(projectModel);
        deleteIcon.setTag(projectModel);
        editIcon.setTag(projectModel);

        //Öffnet ein Projekt
        noteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DetailViewActivity.class);
                i.putExtra("model",(ProjectModel)view.getTag());
                startActivity(getContext(), i, null);
            }
        });

        //Löscht ein Projekt nach erneuter Abfrage
        deleteIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Achtung");
                builder.setMessage("Möchten sie das Projekt wirklich löschen?");

                final View v = view;
                builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (XmlDataManager.deleteProject((ProjectModel)v.getTag())){
                            Toast.makeText(getContext(),"Erfolgreich gelöscht",Toast.LENGTH_LONG).show();
                            ((MainActivity)getContext()).loadData();
                        }else{
                            Toast.makeText(getContext(),"Fehler beim löschen",Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("NEIN", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //Öffnet die Bearbeitungsactivity eines Projektes
        editIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getContext(), DetailViewActivityEdit.class);
                i.putExtra("model",(ProjectModel)view.getTag());
                startActivity(getContext(), i, null);
            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.GERMAN);
        noteDate.setText(sdf.format(projectModel.getDate())); //Setzt Datum
        noteTitle.setText(projectModel.getTitle()); //Setzt Titel
        noteText.setText(DepartmentToStringConverter.convertToString(projectModel.getDepartment())); //Setzt Fachrichtung
        noteIcon.setImageBitmap(null); //Nötig aus Designgründen, falls recycled wird
        AsyncImageLoader.setImageToImageView(projectModel.getTitleImage(), noteIcon,
                (int)getContext().getResources().getDimension(R.dimen.tileWidth),
                (int)getContext().getResources().getDimension(R.dimen.tileHeight)); //Setzt ProjektBild

        return convertView;
    }

}
