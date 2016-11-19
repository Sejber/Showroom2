package models;

/*********************************************************************/
/**  Dateiname: DepartmentToStringConverter.java                    **/
/**                                                                 **/
/**  Beschreibung:  Konvertiert den Enum zu einem String            **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

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
