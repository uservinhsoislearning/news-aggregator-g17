package RealT.main.java.com.example.real;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public  class HelloApplication extends Application {
    private Stage window;
    private Parent root;
    @Override
    public void start(Stage stage) throws IOException {

        window = stage;
        window.setTitle("Page đầu");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

        try {
            root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {

            e.printStackTrace();
            System.out.println(STR."Error loading FXML file: \{e.getMessage()}");
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}