package com.example.real;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
public class ContentController {
    @FXML
    private ScrollPane scrPane;

    @FXML
    private ImageView homeImage2;

    @FXML
    private Text txt;
    private Stage stage;
    private Scene scene;

    public ContentController(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }
    public void initialize(){
        homeImage2.setOnMouseClicked(backHome ->{
            stage.hide();
        });
    }

    public void displayContent(LineComponent lineComponent){
        String content = lineComponent.getContent();
        content = content.replace("|", "\n");
        txt.setText(content);
    }
}
