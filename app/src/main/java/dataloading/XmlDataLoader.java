package dataloading;

/*********************************************************************/
/**  Dateiname: XmlDataLoader.java                                  **/
/**                                                                 **/
/**  Beschreibung:  Lädt alle Projekte in ein ProjektModel Array    **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import models.BlockModel;
import models.Department;
import models.ProjectModel;
import models.SubBlockModel;

class XmlDataLoader implements DataLoader {

    private File currentProjectDirectory;

    public ArrayList<ProjectModel> loadData(File directory) {

        ArrayList<ProjectModel> projectModels = new ArrayList<>();

        //find all directories
        File[] files = new File(directory, "projects").listFiles();

        if (files == null)
            return projectModels;

        for (File f : files) {
            if (f.isDirectory()) {

                currentProjectDirectory = f;

                //This is a project directory, so load and parse the XML-File
                //Check if the XML-File exists
                File xmlFile = new File(f, "project.xml");
                if (!xmlFile.isFile()) {
                    //XML-File doesn't exist, print to log and continue with next project
                    Log.e("XMLLoader", String.format("project.xml of %s doesn't exist.", f.getName()));
                    continue;
                }

                ProjectModel pm = loadProject(xmlFile);
                if (pm != null) {
                    projectModels.add(pm);
                }

            }
        }

        return projectModels;
    }

    private ProjectModel loadProject(File projectFile) {

        InputStream is;

        String title = null;
        ArrayList<String> members = null;
        Department department = null;
        String titleImage = null;
        Date date = null;
        ArrayList<String> tags = null;
        ArrayList<BlockModel> blocks = null;

        try {
            XmlPullParser xpp = Xml.newPullParser();
            is = new FileInputStream(projectFile);
            xpp.setInput(is, "UTF-8");
            xpp.nextTag();

            xpp.require(XmlPullParser.START_TAG, null, "project");
            while (xpp.nextTag() != XmlPullParser.END_TAG) {
                switch (xpp.getName()) {
                    case "title":
                        title = readText(xpp);
                        break;
                    case "department":
                        department = parseDepartment(readText(xpp));
                        break;
                    case "titleimage":
                        titleImage = new File(currentProjectDirectory, readText(xpp)).getAbsolutePath();
                        break;
                    case "date":
                        date = parseDate(readText(xpp));
                        break;
                    case "tags":
                        tags = readTags(xpp);
                        break;
                    case "members":
                        members = readMembers(xpp);
                        break;
                    case "content":
                        blocks = readContent(xpp);
                        break;
                }
            }

            return new ProjectModel(currentProjectDirectory,
                    members, title, department, titleImage, date, tags, blocks);

        } catch (FileNotFoundException e) {
            Log.e("XMLLoader", "FileNotFoundException (no permission?)");
        } catch (XmlPullParserException e) {
            Log.e("XMLLoader", "XmlPullParserException");
        } catch (IOException e) {
            Log.e("XMLLoader", "IOException");
        }

        return null;

    }

    private ArrayList<BlockModel> readContent(XmlPullParser xpp) throws XmlPullParserException, IOException {

        ArrayList<BlockModel> blocks = new ArrayList<>();
        BlockModel b;

        xpp.require(XmlPullParser.START_TAG, null, "content");
        while (xpp.nextTag() != XmlPullParser.END_TAG) {
            if ((b = readBlock(xpp)) != null) {
                blocks.add(b);
            }
        }

        return blocks;

    }

    private BlockModel readBlock(XmlPullParser xpp) throws XmlPullParserException, IOException {

        String title;
        SubBlockModel subBlock1 = null;
        SubBlockModel subBlock2 = null;

        xpp.require(XmlPullParser.START_TAG, null, "block");
        title = xpp.getAttributeValue(null, "title");

        int i = 0;
        while (xpp.nextTag() != XmlPullParser.END_TAG) {
            if (i == 0) {
                subBlock1 = readSubBlock(xpp);
            } else {
                subBlock2 = readSubBlock(xpp);
            }
            i++;
        }

        return new BlockModel(title, subBlock1, subBlock2);

    }

    private SubBlockModel readSubBlock(XmlPullParser xpp) throws XmlPullParserException, IOException {

        xpp.require(XmlPullParser.START_TAG, null, "subblock");
        String type = xpp.getAttributeValue(null, "type");
        switch (type) {
            case "text":
                //load file contents

                File textFile = new File(currentProjectDirectory, readText(xpp));
                FileInputStream fs = new FileInputStream(textFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                return new SubBlockModel(sb.toString());
            case "image":

                //will be null if no subtitle exists
                String subtitle = xpp.getAttributeValue(null, "subtitle");
                String image = new File(currentProjectDirectory, readText(xpp)).getAbsolutePath();

                return new SubBlockModel(image, subtitle);

            default:
                return null;
        }

    }

    private ArrayList<String> readTags(XmlPullParser xpp) throws XmlPullParserException, IOException {

        ArrayList<String> tags = new ArrayList<>();

        xpp.require(XmlPullParser.START_TAG, null, "tags");
        while (xpp.nextTag() != XmlPullParser.END_TAG) {
            tags.add(readText(xpp));
        }

        return tags;

    }

    private ArrayList<String> readMembers(XmlPullParser xpp) throws XmlPullParserException, IOException {

        ArrayList<String> members = new ArrayList<>();

        xpp.require(XmlPullParser.START_TAG, null, "members");
        while (xpp.nextTag() != XmlPullParser.END_TAG) {
            members.add(readText(xpp));
        }

        return members;

    }

    private String readText(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String result = "";
        if (xpp.next() == XmlPullParser.TEXT) {
            result = xpp.getText();
            xpp.nextTag();
        }
        return result;
    }

    private Department parseDepartment(String s) {
        switch (s) {
            case "ET":
                return Department.ET;
            case "IT":
                return Department.IT;
            case "MB":
                return Department.MB;
            case "MT":
                return Department.MT;
            default:
                return Department.OTHER;
        }
    }

    private Date parseDate(String s) {
        DateFormat df = new SimpleDateFormat("yyyy", Locale.GERMAN);

        try {
            return df.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

}