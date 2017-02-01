package controller;

import com.google.api.services.youtube.YouTube;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import webservices.ServicesCalls;
import webservices.Video;

/**
 * This class control the video element that is parsed on the application search result.
 *
 * Each video element is an HBox that nested inside of a VBox.
 * Each Hbox will have the video's thumbnail image and the video title.
 */
public class VideoElement {
    private Controller appController;           //controller object
    private Video video;
    private HBox hbox;

    @FXML
    private VBox videoBox;

    public VideoElement(Video video){
        this.video = video;
        setElement();
    }

    public HBox getHbox(){
        return this.hbox;
    }

    //Get a controller object from the controller class.
    public void setAppController(Controller controller){
        this.appController = controller;
    }

    /*
     * Set a video element to have a thumbnail image and title description.
     * Also set the thumbnail to be clickable.
     */
    private void setElement(){
        hbox = new HBox(8);
        ImageView thumbnail = new ImageView(video.getThumbnailURL());

        /*
         * On click, play the video, then search for related videos that
         * return videos results strictly based on the current playing
         * video's id.
         *
         * case 1: If no player current playing, start the player then play
         *
         * case 2: If there is already an existing player currently playing,
         *         then swap out video id, then play the video.
         */
        thumbnail.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Player player = Controller.player;
                if (!player.getPlayerStatus()){
                    player.setVideo(video.getId());
                    player.start();
                    player.play();
                    appController.getRelatedVideoID(video.getId());
                }else{
                    player.setVideo(video.getId());
                    player.play();
                    appController.getRelatedVideoID(video.getId());
                }
            }
        });
        Label vTitle = new Label(video.getTitle());
        vTitle.setWrapText(true);
        vTitle.setMinWidth(130.0);
        vTitle.setTextFill(Color.web("#ffffff"));
        hbox.getChildren().addAll(thumbnail, vTitle);
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.minHeight(50.0);
    }
}
