package RealT.main.java.com.example.real;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;


public class LineController {
    @FXML
    private Button author;
//    @FXML
//    private HBox TagBox;
    @FXML
    private FlowPane flowp;

    @FXML
    private HBox date_type;

    @FXML
    private Button title;

    @FXML
    private Label label;
    public void attachValue(LineComponent lineComponent){
        label.setText(lineComponent.getCreationDate() + "/" + lineComponent.getType());
        title.setText(lineComponent.getTitle());
        author.setText(lineComponent.getAuthor());
        // bắt đầu tạo button tags
        String[] LTags = lineComponent.getTags().split("\\|");
        if (LTags.length == 0) {
//            TagBox.setVisible(false);
            

        } else {

            flowp.setVisible(true);
            for (String T: LTags) {
                Button buttonTag = new Button();
                buttonTag.setPrefWidth(200);
                buttonTag.setPrefHeight(17);
                buttonTag.setText(T);
                flowp.getChildren().add(buttonTag);

            }
        }
    }
}

