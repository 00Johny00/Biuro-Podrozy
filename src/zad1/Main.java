/**
 *
 *  @author Biegańska Kaja PD3184
 *
 */

package zad1;


import java.io.*;
import java.util.*;

public class Main {

  public static void main(String[] args) {
    File dataDir = new File("data");
    TravelData travelData = new TravelData(dataDir);
    String dateFormat = "yyyy-MM-dd";
    for (String locale : Arrays.asList("pl_PL", "en_GB")) {
      List<String> odlist = travelData.getOffersDescriptionsList(locale, dateFormat);
      for (String od : odlist) System.out.println(od);
    }
    String url = "jdbc:mysql://localhost:3306/traveldb";
    Database db = new Database(url, travelData);
    db.create();
    db.showGui();
  }
}
