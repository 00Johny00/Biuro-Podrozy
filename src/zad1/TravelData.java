package zad1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TravelData {

    private List<String> offersList = new ArrayList<>();

    public TravelData(File dataDir) {
        String descriptionLine;
        int numberOfDescription = 0;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(dataDir + "/data.txt"));
            descriptionLine = reader.readLine();


            while (descriptionLine != null) {
                offersList.add(descriptionLine);
                numberOfDescription++;
                descriptionLine = reader.readLine();
            }
            System.out.println("---------------------");
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getOffersDescriptionsList(String loc, String dateFormat) {
        return offersList.stream().filter(o -> o.contains(loc)).collect(Collectors.toList());
    }

    public List<String> getAll() {
        return offersList;
    }
}
