package models;

public class DepartmentToStringConverter {

    public static String convertToString(Department d) {
        switch (d) {
            case IT:
                return "Informatik";
            case ET:
                return "Elektrotechnik";
            case ME:
                return "Mechatronik";
            case MB:
                return "Maschinenbau";
            default:
            case OTHER:
                return "Andere";
        }
    }

}
