package models;

/**
 * Created by Freddy on 11.11.2016.
 */

public class SubBlockModel {

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

}
