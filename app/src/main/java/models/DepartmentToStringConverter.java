package models;

public class DepartmentToStringConverter {

    public static String convertToString(Department d) {
        switch (d) {
            case ET:
                return "Elektrotechnik";
            case IT:
                return "Informatik";
            case MB:
                return "Maschinenbau";
            case MT:
                return "Mechatronik";
            default:
            case OTHER:
                return "Andere";
        }
    }

}
