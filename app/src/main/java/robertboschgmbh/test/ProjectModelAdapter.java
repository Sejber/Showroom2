package robertboschgmbh.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dataloading.AsyncImageLoader;
import models.Department;
import models.ProjectModel;

import static android.support.v4.content.ContextCompat.startActivity;


public class ProjectModelAdapter extends ArrayAdapter<models.ProjectModel> {
    static private boolean admin = false;


    public ProjectModelAdapter(Context context, ArrayList<models.ProjectModel> projectModels,boolean admin){
        super(context,0,projectModels);
        this.admin = admin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ProjectModel projectModel = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row,parent,false);
        }
        TextView noteTitle = (TextView) convertView.findViewById(R.id.listItemNoteTitle);
        TextView noteText = (TextView) convertView.findViewById(R.id.listItemNoteBody);
        ImageView noteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteImg);
        ImageView deleteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteDelete);
        ImageView editIcon = (ImageView) convertView.findViewById(R.id.listItemNoteEdit);
        if(!admin){
            RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.layoutAdmin);
            rl.setVisibility(View.INVISIBLE);
        }

        noteIcon.setTag(projectModel);
        deleteIcon.setTag(projectModel);
        editIcon.setTag(projectModel);

        noteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DetailViewActivity.class);
                i.putExtra("model",(ProjectModel)view.getTag());
                startActivity(getContext(), i, null);
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


            }
        });

        editIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getContext(), DetailViewActivityEdit.class);
                i.putExtra("model",(ProjectModel)view.getTag());
                startActivity(getContext(), i, null);
            }
        });

        noteTitle.setText(projectModel.getTitle());
        noteText.setText(convertToString(projectModel.getDepartment()));

        noteIcon.setImageBitmap(null);

        AsyncImageLoader.setImageToImageView(projectModel.getTitleImage(), noteIcon,
                (int)getContext().getResources().getDimension(R.dimen.tileWidth),
                (int)getContext().getResources().getDimension(R.dimen.tileHeight));

        return convertView;
    }

    private String convertToString(Department d) {
        switch (d) {
            case ET:
                return "Elektrotechnik";
            case IT:
                return "Informatik";
            case MB:
                return "Maschinenbau";
            case MT:
                return "Mechatronik";
            default:
            case OTHER:
                return "Andere";
        }
    }

}
