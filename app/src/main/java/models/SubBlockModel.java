package models;

import java.io.Serializable;

/**
 * Created by Freddy on 11.11.2016.
 */

public class SubBlockModel implements Serializable {

    private SubBlockType type;
    private String text;
    private String image;
    private String subtitle;

    public SubBlockModel(String text) {
        this.text = text;
        this.type = SubBlockType.TEXT;
    }

    public SubBlockModel(String image, String subtitle) {
        this.image = image;
        this.subtitle = subtitle;
        this.type = SubBlockType.IMAGE;
    }

    public SubBlockType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getSubtitle() {
        return subtitle;
    }


    public void setType(SubBlockType type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

}
