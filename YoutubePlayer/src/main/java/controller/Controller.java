package controller;

import com.sun.deploy.ui.ImageLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import webservices.ServicesCalls;
import webservices.Video;

import java.util.List;

/*
 * This controller class responsible for controlling information from the FXML to the application
 * and deliver information from the application back to the FXML interface.
 */

public class Controller {
    //--------------------------------------- FXML CONTROL VARIABLES ------------------------------------
    @FXML
    private Button favoriteBtn;
    @FXML
    private VBox videoBox;
    @FXML
    private ScrollPane centerScrollPane;
    @FXML
    private TextField textField;
    @FXML
    private CheckBox fullscreen;

    //-------------------------------------------- CLASS VARIABLES --------------------------------------
    private String keyword = "";                                //search Q
    protected List<Video> videoList;                            //result videos from search
    protected static ServicesCalls searchServices = null;
    protected final static Player player = new Player();
    private static String currentVideoID;                       //current playing video's id


    public Controller(){}

    //----------------------------------------CONTROL FUNCTIONS------------------------------------------
    public void onFullScreen(){
        fullscreen.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Controller.player.setFullscreen(newValue ? true : false);
            }
        });
    }

    public void onEnterKey(){
        this.keyword = this.textField.getText();
        searchVideo();
    }

    public void getRelatedVideoID(String videoID){
        currentVideoID = videoID;
        videoList = searchServices.getRelatedVideos(currentVideoID);
        setVideoBox();
    }

    //----------------------------------------- SEARCH VIDEOS METHODS---------------------------------------
    /* IF 1st time calling this function, it will initialized the search first
     * Then it will proceed to search for video with Q key word
     *
     * If not the first time, then this method will just replace Q keyword and call search
     */
    private void searchVideo(){
        if (searchServices == null) {
            searchServices = new ServicesCalls();
            searchServices.setKeyWord(keyword);
            searchServices.searchInit();
            videoList = searchServices.search();
        }else {
            videoList.clear();
            searchServices.setKeyWord(keyword);
            videoList = searchServices.search();
        }
        setVideoBox();
    }

    /*
     * Reset the videos box after every search to avoid retain any old search queries.
     * Clear the entire screen of search result before parse new results.
     */
    private void setVideoBox(){
        videoBox.getChildren().clear();
        for (int i = 0; i < videoList.size(); i++){
            VideoElement element = new VideoElement(videoList.get(i));
            element.setAppController(this);
            videoBox.getChildren().add(element.getHbox());
        }
    }
}
