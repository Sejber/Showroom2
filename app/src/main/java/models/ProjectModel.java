package models;

/*********************************************************************/
/**  Dateiname: ProjectModel.java                                   **/
/**                                                                 **/
/**  Beschreibung:  Objekt mit allen Informationen eines Projektes  **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ProjectModel implements Serializable {

    private File directory;
    private ArrayList<String> members;
    private String title;
    private Department department;
    private String titleImage;
    private Date date;
    private ArrayList<String> tags;
    private ArrayList<BlockModel> blocks;

    public ProjectModel(File directory, ArrayList<String> members, String title,
                        Department department, String titleImage, Date date,
                        ArrayList<String> tags, ArrayList<BlockModel> blocks) {

        this.directory = directory;
        this.members = members;
        this.title = title;
        this.department = department;
        this.titleImage = titleImage;
        this.date = date;
        this.tags = tags;
        this.blocks = blocks;

    }

    public void setMembers(ArrayList<String> members) { this.members = members; }
    public ArrayList<String> getMembers() {
        return members;
    }

    public void setTitle(String title) { this.title=title; }
    public String getTitle() {
        return title;
    }

    public void setDepartment(Department d) { this.department = d; }
    public Department getDepartment() {
        return department;
    }

    public void setTitleImage(String s) { this.titleImage = s; }
    public String getTitleImage() {
        return titleImage;
    }

    public void setDate(Date d) { this.date = d; }
    public Date getDate() {
        return date;
    }

    public void setTags(ArrayList<String> tags) { this.tags = tags; }
    public ArrayList<String> getTags() {
        return tags;
    }

    public void addBlock(BlockModel newBlock) {
        if (this.blocks == null)
            this.blocks = new ArrayList<>();

        this.blocks.add(newBlock);
    }
    public ArrayList<BlockModel> getBlocks() {
        return blocks;
    }

    public void setDirectory(File f) {
        directory = f;
    }
    public File getDirectory() { return directory; }

}
