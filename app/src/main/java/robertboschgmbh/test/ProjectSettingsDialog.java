package robertboschgmbh.test;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import dataloading.AsyncImageLoader;
import dataloading.ImageFileHelper;
import dataloading.XmlDataLoader;
import filechooser.FileChooser;
import models.Department;
import models.DepartmentToStringConverter;
import models.ProjectModel;

/**
 * Created by Freddy on 19.11.2016.
 *
 */

public class ProjectSettingsDialog {

    private AlertDialog dialog;
    private DetailViewActivityEdit activity;
    private ProjectModel model;
    private View view;

    public ProjectSettingsDialog(final DetailViewActivityEdit activity, ProjectModel model) {

        this.model = model;
        this.activity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        view = activity.getLayoutInflater().inflate(R.layout.activity_new_project, null);

        Spinner spinner = (Spinner) view.findViewById(R.id.newSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        if (model.getTitle() != null) {
            EditText title = (EditText)view.findViewById(R.id.newTitle);
            title.setText(model.getTitle());
        }

        if (model.getMembers() != null) {
            EditText members = (EditText)view.findViewById(R.id.newMembers);
            members.setText(concatWithComma(model.getMembers()));
        }

        if (model.getDepartment() != null) {
            spinner.setSelection(adapter.getPosition(
                    DepartmentToStringConverter.convertToString(model.getDepartment())));
        }

        if (model.getDate() != null) {
            EditText date = (EditText)view.findViewById(R.id.newDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.GERMAN);
            date.setText(sdf.format(model.getDate()));
        }

        if (model.getTags() != null) {
            EditText tags = (EditText)view.findViewById(R.id.newTags);
            tags.setText(concatWithComma(model.getTags()));
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.newImageView);
        imageView.setImageBitmap(null);
        Button loadImage = (Button)view.findViewById(R.id.newBtnLoadImage);
        if (model.getTitleImage() != null) {
            imageView.setVisibility(View.VISIBLE);
            loadImage.setVisibility(View.GONE);
            AsyncImageLoader.setImageToImageView(model.getTitleImage(), imageView,
                    (int)activity.getResources().getDimension(R.dimen.tileWidth),
                    (int)activity.getResources().getDimension(R.dimen.tileHeight));
        } else {
            imageView.setVisibility(View.GONE);
            loadImage.setVisibility(View.VISIBLE);
        }

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new FileChooser(activity).setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        ProjectSettingsDialog.this.applyImage(file);
                    }
                }).showDialog();
                return true;
            }
        });

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FileChooser(activity).setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        ProjectSettingsDialog.this.applyImage(file);
                    }
                }).showDialog();
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Speichern", null);
        builder.setNegativeButton("Abbrechen", null);
        dialog = builder.create();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish(true);
                    }
                });

                b = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish(false);
                    }
                });

            }
        });

    }

    public void show() { dialog.show(); }

    private void applyImage(File f) {
        if (f != null) {
            File inProjectDir = ImageFileHelper.copyFileToDirectory(activity, f, model.getDirectory());
            if (inProjectDir != null) {
                model.setTitleImage(inProjectDir.getAbsolutePath());
                AsyncImageLoader.setImageToImageView(model.getTitleImage(),
                        (ImageView) view.findViewById(R.id.newImageView),
                        (int) activity.getResources().getDimension(R.dimen.tileWidth),
                        (int) activity.getResources().getDimension(R.dimen.tileHeight));
            } else {
                ImageView imageView = (ImageView)view.findViewById(R.id.newImageView);
                imageView.setVisibility(View.GONE);
                Button loadImage = (Button)view.findViewById(R.id.newBtnLoadImage);
                loadImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void finish(boolean dialogResult) {
        if (dialogResult) {
            if (updateModel()) {
                dialog.dismiss();
                activity.onProjectSettingsDialogFinish(true, model);
            }
        } else {
            dialog.dismiss();
            activity.onProjectSettingsDialogFinish(false, model);
        }
    }

    private boolean updateModel() {

        String reason = "";

        EditText et = (EditText)view.findViewById(R.id.newTitle);
        if (et.getText().toString().equals("")) {
            reason = "Titel ungültig";
        } else {
            model.setTitle(et.getText().toString());
        }

        et = (EditText)view.findViewById(R.id.newMembers);
        if (et.getText().toString().equals("")) {
            reason = "Mitglieder ungültig";
        } else {
            model.setMembers(splitByComma(et.getText().toString()));
        }

        Spinner sp = (Spinner)view.findViewById(R.id.newSpinner);
        String department = (String)sp.getSelectedItem();
        if (department != null) {
            Department de = null;
            switch (department) {
                case "Informatik":
                    de = Department.IT;
                    break;
                case "Elektrotechnik":
                    de = Department.ET;
                    break;
                case "Maschinenbau":
                    de = Department.MB;
                    break;
                case "Mechatronik":
                    de = Department.MT;
                    break;
                default:
                    de = Department.OTHER;
                    break;
            }
            model.setDepartment(de);
        } else {
            reason = "Fachrichtung ungültig";
        }

        et = (EditText)view.findViewById(R.id.newDate);
        if (XmlDataLoader.parseDate(et.getText().toString()) == null) {
            reason = "Datum ungültig";
        } else {
            model.setDate(XmlDataLoader.parseDate(et.getText().toString()));
        }

        et = (EditText)view.findViewById(R.id.newTags);
        if (et.getText().toString().equals("")) {
            reason = "Tags ungültig";
        } else {
            model.setTags(splitByComma(et.getText().toString()));
        }

        if (model.getTitleImage() == null) {
            reason = "Titelbild ungültig";
        }

        if (!reason.equals("")) {
            Toast.makeText(activity, reason, Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private ArrayList<String> splitByComma(String s) {

        String[] arr = s.split(",");
        ArrayList<String> returnList = new ArrayList<>();

        for (String t : arr) {
            returnList.add(t.trim());
        }

        return returnList;

    }

    private String concatWithComma(ArrayList<String> list) {

        StringBuilder sb = new StringBuilder();

        for (String s : list) {
            sb.append(s);
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();

    }

}
