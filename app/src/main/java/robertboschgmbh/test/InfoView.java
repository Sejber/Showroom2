package robertboschgmbh.test;

import android.app.Activity;
import android.app.Dialog;
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

        dialog = new Dialog(activity);
        View v = activity.getLayoutInflater().inflate(R.layout.info_view, null);

        ImageView close = (ImageView)v.findViewById(R.id.info_view_close);

        TextView title = (TextView)v.findViewById(R.id.info_view_projectTitle);
        TextView department = (TextView)v.findViewById(R.id.info_view_department);
        TextView date = (TextView)v.findViewById(R.id.info_view_date);

        ListView members = (ListView)v.findViewById(R.id.info_view_members);
        ListView tags = (ListView)v.findViewById(R.id.info_view_tags);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        title.setText(model.getTitle());
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

        dialog.setTitle(model.getTitle());
        dialog.setContentView(v);
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    void show() {
        dialog.show();
    }


}
