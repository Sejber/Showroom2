package robertboschgmbh.test;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import models.DepartmentToStringConverter;
import models.ProjectModel;

/**
 * Created by Freddy on 19.11.2016.
 *
 */

class InfoView {

    private Dialog dialog;
    private ProjectModel model;

    InfoView(Activity activity, ProjectModel model) {
        this.model = model;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View v = activity.getLayoutInflater().inflate(R.layout.info_view, null);

        TextView department = (TextView)v.findViewById(R.id.info_view_department);
        TextView date = (TextView)v.findViewById(R.id.info_view_date);

        ListView members = (ListView)v.findViewById(R.id.info_view_members);
        ListView tags = (ListView)v.findViewById(R.id.info_view_tags);

        department.setText(DepartmentToStringConverter.convertToString(model.getDepartment()));

        if (model.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.GERMAN);
            date.setText(sdf.format(model.getDate()));
        }

        members.setAdapter(
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1,
                        android.R.id.text1, model.getMembers())
        );

        tags.setAdapter(
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1,
                        android.R.id.text1, model.getTags())
        );

        builder.setView(v);
        builder.setTitle(model.getTitle());
        builder.setNeutralButton("Schließen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    void show() {
        dialog.show();
    }


}
