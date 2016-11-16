package models;

import java.io.Serializable;

/**
 * Created by Freddy on 11.11.2016.
 */

public class BlockModel implements Serializable {

    private String title;
    private SubBlockModel subBlock1;
    private SubBlockModel subBlock2;

    public BlockModel(String title, SubBlockModel subBlock1, SubBlockModel subBlock2) {
        this.title = title;
        this.subBlock1 = subBlock1;
        this.subBlock2 = subBlock2;
    }

    public String getTitle() {
        return title;
    }

    public SubBlockModel getSubBlock1() {
        return subBlock1;
    }

    public SubBlockModel getSubBlock2() {
        return subBlock2;
    }

}
