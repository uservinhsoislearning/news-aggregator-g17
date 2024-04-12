package RealT.main.java.com.example.real;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class HelloController implements Initializable {

    @FXML
    private VBox vboxcont;

    @FXML
    private ScrollPane scollableid;

    @FXML
    private VBox vbox_2;
    private List<LineComponent> recentlyAdded;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recentlyAdded = new ArrayList<>(recentlyAdded());
        try {
            for (int i = 0; i < recentlyAdded.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Line.fxml"));
                HBox lineBox = fxmlLoader.load();


                LineController lineController = fxmlLoader.getController();
                lineController.attachValue(recentlyAdded.get(i));
                vboxcont.getChildren().add(lineBox);

                scollableid.setContent(vboxcont);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LineComponent> recentlyAdded() {
        List<LineComponent> lst = new ArrayList<>();

        String path = "output_Cryptopolitan_Test.csv";


        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] columns = rows.get(i);
                LineComponent lineComponent = new LineComponent(columns[0], columns[1], columns[2], columns[3], columns[4], columns[5], columns[6], columns[7], columns[8], columns[9]);
                lst.add(lineComponent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return lst;
    }
}
