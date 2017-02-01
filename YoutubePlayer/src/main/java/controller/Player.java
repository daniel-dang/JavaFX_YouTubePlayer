package controller;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This player class manage and control the status of the player that
 * either playing a video or inactive.
 *
 * This class have the responsibilities is manage and control the player
 * via Webview component to exit when new video is selected, or close when
 * the user no longer playing video.
 */
public class Player {
    private final String youtube = "https://www.youtube.com/embed/";        //URL structure to play a YouTube video through Webview component
    private String id;                                                      //video id

    private Stage player;               //player window
    private WebView webView;            //webview component

    private boolean showed = false;     //player window showing status
    private boolean started = false;    //player window playing status


    public Player(){
        player = new Stage();
        webView = new WebView();
    }

    //--------------------------------GETTERS AND SETTERS------------------------------
    public boolean getPlayerStatus(){
        return this.started;
    }

    public boolean getPlayStatus(){
        return this.showed;
    }

    public void setVideo(String id){
        this.id = id;
    }

    //----------------------------------CONTROL METHODS--------------------------------

    /*
     * Initialized a player's window.
     * Set window name
     * Set default dimension
     */
    public void start(){
        player.setTitle("YouTube Player");
        Scene scene = new Scene(webView, 1166, 668);
        player.setScene(scene);
        player.show();
        started = true;
    }

    /*
     * Play a video by concatenate the URL structure to have additional
     * video id and autoplay option.
     *
     * ***NOTE: More option can be added.
     */
    public void play(){
        String loadURL = youtube + id + "?autoplay=1";
        webView.getEngine().load(loadURL);

        /*
         * This case prevent multiple video window showing when click on different
         * video. This case ensure one player window is open each time a video is clicked.
         */
        if (!showed) {
            player.show();
            showed = true;
        }
        /*
         * If close button detect, set showed to false and close the player window
         */
        player.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                webView.getEngine().load(null);
                showed = false;
                player.close();
            }
        });
    }

    /*
     * Method toggle player on fullscreen mode
     */
    public void setFullscreen(boolean fullscreen){
        player.setFullScreen(fullscreen);
    }
}
