package dataloading;

import java.io.File;
import java.util.ArrayList;

import models.Department;
import models.ProjectModel;

/**
 * Created by Freddy on 11.11.2016.
 */

public interface DataLoader {
    ArrayList<ProjectModel> loadData(File directory);
    ArrayList<ProjectModel> getProjectsOfDepartment(Department dep);
}
