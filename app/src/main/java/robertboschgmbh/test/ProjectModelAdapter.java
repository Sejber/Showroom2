package robertboschgmbh.test;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dataloading.AsyncImageLoader;
import models.Department;
import models.ProjectModel;

import static android.support.v4.content.ContextCompat.startActivity;


public class ProjectModelAdapter extends ArrayAdapter<models.ProjectModel> {

    public ProjectModelAdapter(Context context, ArrayList<models.ProjectModel> projectModels){
        super(context,0,projectModels);
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

        noteIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                projectModel.getBlocks();
            }
        });

        editIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(this,DetailViewActivity.class);
                i.putExtra("model",projectModel);
                startActivity(i);
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

    private int dpToPixels(int dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private String convertToString(Department d) {
        switch (d) {
            case IT:
                return "Informatik";
            case ET:
                return "Elektrotechnik";
            case ME:
                return "Mechatronik";
            default:
            case OTHER:
                return "Andere";
        }
    }

}
