package dataloading;

/*********************************************************************/
/**  Dateiname: XmlDataManager.java                                 **/
/**                                                                 **/
/**  Beschreibung:  Verwaltet exestierende Xml Projektdateien       **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import models.BlockModel;
import models.Department;
import models.ProjectModel;
import models.SubBlockModel;
import models.SubBlockType;

public class XmlDataManager {

    public static ArrayList<ProjectModel> loadProjects(File directory) {
        XmlDataLoader loader = new XmlDataLoader();
        return loader.loadData(directory);
    }

    public static boolean deleteProject(ProjectModel pm) {

        if (pm == null)
            return false;

        File dir = pm.getDirectory();

        if (dir == null)
            return false;

        return DeleteRecursive(dir);

    }

    public static boolean DeleteRecursive(File Directory) {
        if (Directory.isDirectory())
            for (File child : Directory.listFiles())
                DeleteRecursive(child);

        return Directory.delete();
    }

    public static boolean changeProject(ProjectModel pm) {

        if (pm == null)
            return false;

        try {

            String[] textFiles = pm.getDirectory().list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(".txt");
                }
            });

            for (String s : textFiles) {
                new File(pm.getDirectory(), s).delete();
            }


            FileOutputStream fos = new FileOutputStream(new File(pm.getDirectory(), "project.xml"));

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "project");

            printTag(serializer, "title", pm.getTitle());
            printTag(serializer, "department", departmentToString(pm.getDepartment()));

            String path = pm.getTitleImage();
            printTag(serializer, "titleimage", path.substring(path.lastIndexOf("/") + 1));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.GERMAN);
            printTag(serializer, "date", sdf.format(pm.getDate()));

            serializer.startTag(null, "members");
            for (String s : pm.getMembers()) {
                printTag(serializer, "member", s);
            }
            serializer.endTag(null, "members");

            serializer.startTag(null, "tags");
            for (String s : pm.getTags()) {
                printTag(serializer, "tag", s);
            }
            serializer.endTag(null, "tags");

            serializer.startTag(null, "content");

            for (BlockModel b : pm.getBlocks()) {

                serializer.startTag(null, "block");

                if (b.getTitle() != null && !b.getTitle().equals("")) {
                    serializer.attribute(null, "title", b.getTitle());
                }

                if (b.getSubBlock1() != null) {
                    printSubblock(serializer, b.getSubBlock1(), pm.getDirectory());
                }

                if (b.getSubBlock2() != null) {
                    printSubblock(serializer, b.getSubBlock2(), pm.getDirectory());
                }

                serializer.endTag(null, "block");

            }

            serializer.endTag(null, "content");
            serializer.endTag(null, "project");
            serializer.flush();
            serializer.endDocument();

            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    private static void printSubblock(XmlSerializer serializer, SubBlockModel sb, File directory) throws IOException {
        serializer.startTag(null, "subblock");
        if (sb.getType() == SubBlockType.TEXT) {
            serializer.attribute(null, "type", "text");
            serializer.text(printTextToFile(directory, sb.getText()));
        } else {
            serializer.attribute(null, "type", "image");

            if (sb.getSubtitle() != null && !sb.getSubtitle().equals("")) {
                serializer.attribute(null, "subtitle", sb.getSubtitle());
            }

            serializer.text(sb.getImage().substring(sb.getImage().lastIndexOf("/") + 1));
        }
        serializer.endTag(null, "subblock");
    }

    private static String printTextToFile(File directory, String text) throws IOException {

        int i = 0;
        File f = new File(directory, "text" + i + ".txt");

        while (f.exists()) {
            i++;
            f = new File(directory, "text" + i + ".txt");
        }

        FileWriter fw = new FileWriter(f);
        fw.write(text);
        fw.flush();
        fw.close();

        return f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("/") + 1);
    }

    private static void printTag(XmlSerializer serializer, String tagName, String text) throws IOException {
        serializer.startTag(null, tagName);
        serializer.text(text);
        serializer.endTag(null, tagName);
    }

    private static String departmentToString(Department d) {
        switch (d) {
            case IT:
                return "IT";
            case ET:
                return "ET";
            case MT:
                return "MT";
            case MB:
                return "MB";
            case OTHER:
            default:
                return "OTHER";
        }
    }

    public static ProjectModel initializeProject(File directory) {

        ProjectModel pm = new ProjectModel(null, null, "Neues Projekt", Department.OTHER, null, null, null, null);

        //check if there is a 'projects' folder in the specified directory
        File projectsDirectory = new File(directory, "projects");
        if (!projectsDirectory.exists() || !projectsDirectory.isDirectory()) {
            return null;
        }

        File newProjectDir;

        //create random directory names until we find one that
        //doesnt exist yet.
        do {
            newProjectDir = new File(projectsDirectory, createRandomFilename(12));
        } while (newProjectDir.exists());

        if (newProjectDir.mkdir()) {
            pm.setDirectory(newProjectDir);
            return pm;
        } else {
            return null;
        }

    }

    private static String createRandomFilename(int count) {

        char[] allowedChars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder builder = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < count; i++) {
            builder.append(allowedChars[r.nextInt(allowedChars.length)]);
        }

        return  builder.toString();

    }

}
