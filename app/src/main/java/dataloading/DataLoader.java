package dataloading;

/*********************************************************************/
/**  Dateiname: DataLoader.java                                     **/
/**                                                                 **/
/**  Beschreibung:  Interface für loader Klassen                    **/
/**                 (Dependency-Inversion-Principle)                **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import java.io.File;
import java.util.ArrayList;

import models.Department;
import models.ProjectModel;

public interface DataLoader {
    ArrayList<ProjectModel> loadData(File directory);
}
