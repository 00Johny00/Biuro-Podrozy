package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database extends JFrame {

    private String[] COLUMNS_GB = new String[]{"Lokalizacja", "Kraj", "Data Wyjazdu", "Data Powrotu", "Miejsce", "Cena", "Waluta"};
    private String[] COLUMNS_PL = new String[]{"Language", "Country", "Date Leaving", "Date Arriving", "Place", "Price", "Currency"};
    private String dataPL[][];
    private String dataGB[][];
    private TravelData travelData;
    private String url;
    private String dateFormat = "yyyy-MM-dd";

    List<String> listToDb = new ArrayList<String>();
    Connection conn = null;
    Statement statement =null;
    Statement statementResults =null;
    JFrame frame = new JFrame();
    JTable table = new JTable();
    JButton buttonChangeLanguagePL = new JButton("POLSKI");
    JButton buttonChangeLanguageUS = new JButton("ENGLISH");


    public Database(String url, TravelData travelData) {
        this.url = url;
        this.travelData = travelData;
    }

    public void create() {
        try {
            this.conn = DriverManager.getConnection(this.url, "root", "root");
            System.out.println("Connected to DB");
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from test");

            String dropTable = "DROP TABLE IF EXISTS TravelDB";
            statement.executeUpdate(dropTable);

            String createTable = "CREATE TABLE TravelDB ("
                            + "id INT NOT NULL AUTO_INCREMENT, "
                            + "language VARCHAR(45) NOT NULL,"
                            + "country VARCHAR(45) NOT NULL,"
                            + "dateLeaving DATE NOT NULL,"
                            + "dateArriving DATE NOT NULL,"
                            + "place VARCHAR(45) NOT NULL,"
                            + "price VARCHAR(45) NOT NULL,"
                            + "currency VARCHAR(4) NOT NULL, PRIMARY KEY(id))";

            statement.executeUpdate(createTable);
            fillDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (conn != null && statement != null) {
                try {
                    conn.close();
                    statement.close();
                } catch (SQLException sqlex) {
                    sqlex.printStackTrace();
                }
            }
        }
    }

    private void fillDatabase() {

       listToDb =  travelData.getAll();
        try {
            for (String line : listToDb) {
                String[] offer = line.split("\t");
                String updateSQL = "INSERT INTO TravelDB(language, country, dateLeaving, dateArriving, place, price, currency)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement updatestatement = conn.prepareStatement(updateSQL);
                updatestatement.setString(1, offer[0]);
                updatestatement.setString(2, offer[1]);
                updatestatement.setString(3, offer[2]);
                updatestatement.setString(4, offer[3]);
                updatestatement.setString(5, offer[4]);
                updatestatement.setString(6, offer[5]);
                updatestatement.setString(7, offer[6]);
                updatestatement.executeUpdate();
                System.out.println(line);
            }
                dataPL = fetchDataForLang("pl_PL");
                dataGB = fetchDataForLang("en_GB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showGui() {

        table = new JTable(dataPL, COLUMNS_PL) {
            public boolean isCellEditable(int data, int columns) {
                return false;
            }
        };

        frame.setVisible(true);
        frame.setBounds(300, 300, 600, 600);
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        JScrollPane jScrollPane = new JScrollPane(table);
        frame.add(buttonChangeLanguagePL);
        frame.add(buttonChangeLanguageUS);
        frame.add(table);
        buttonChangeLanguagePL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.removeAll();
                table = new JTable(dataPL, COLUMNS_PL) {
                    public boolean isCellEditable(int data, int columns) {
                        return false;
                    }
                };
                SwingUtilities.updateComponentTreeUI(frame);

            }
        });
        buttonChangeLanguageUS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table = new JTable(dataGB, COLUMNS_GB) {
                    public boolean isCellEditable(int data, int columns) {
                        return false;
                    }
                };
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
    }

    private String[][] fetchDataForLang(String lang) {
        String[][] data;
        try {
            String query = "select language, country, dateLeaving, dateArriving, place, price, currency from TravelDB WHERE language =?";
            PreparedStatement s = conn.prepareStatement(query);
            s.setString(1,lang);
            ResultSet resultSet = s.executeQuery();
            data = new String[50][7];
            int i = 1;
            while (resultSet.next()) {
               for (int j = 0; j < 7; j++) {
                   data[i][j] = resultSet.getString(j + 1);
               }
               i++;
           }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }
}

