package models;

/*********************************************************************/
/**  Dateiname: BlockModel.java                                     **/
/**                                                                 **/
/**  Beschreibung:  Beinhaltet die Informationen eines Blocks       **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import java.io.Serializable;

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

    public void setTitle(String title){
        this.title = title;
    }

    public SubBlockModel getSubBlock1() {
        return subBlock1;
    }

    public SubBlockModel getSubBlock2() {
        return subBlock2;
    }

}
