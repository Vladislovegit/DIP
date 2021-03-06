package sample;

import imageProcessing.ImageLoader;
import imageProcessing.ImageProcessing;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.MyPair;
import models.Zone;

import java.awt.image.BufferedImage;

public class Main extends Application {

    public TextField fileNameTextField;
    public TextField mnTextField;
    public TextField clustersTextField;
    public Button okButton;
    public Text amountOfZones;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/main.fxml"));
        fxmlLoader.setController(this);
        primaryStage.setTitle("Cluster Analysis");
        primaryStage.setScene(new Scene((Parent) fxmlLoader.load(), 300, 300));
        primaryStage.show();

        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onOkClick();
            }
        });
    }

    private void onOkClick() {
        String fileName = fileNameTextField.getText();
        BufferedImage image = ImageLoader.load(fileName);
        Integer[][] src = ImageProcessing.getBrightess(image);
        ImageLoader.save(src, "images/gray/" + fileName + "_gray", image);
        Integer[][] res = ImageProcessing.dissection(src, Integer.valueOf(mnTextField.getText()));
        ImageLoader.save(res, "images/bin/" + fileName + "_bin", image);

        MyPair<Integer[][], Zone[]> scanResults = ImageProcessing.scan(res, Integer.valueOf(clustersTextField.getText()));

        ImageLoader.saveClusters(
                scanResults.getFirst(),
                "images/res/" + fileName + "_res",
                image,
                Integer.valueOf(clustersTextField.getText())
        );

        amountOfZones.setText(((Integer) scanResults.getSecond().length).toString()
                + '\n' + zonesToString(scanResults.getSecond()));
    }

    private String zonesToString(Zone[] zones) {
        String res = new String();
        for (Zone zone : zones) {
            res = res + zone.cluster + " ";
        }
        return res;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
