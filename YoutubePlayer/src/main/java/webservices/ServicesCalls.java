package webservices;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This class is the service provider to access the YouTube API to queries searches.
 * This class manage search method and search result handling.
 */
public class ServicesCalls {
    private String apiKey = "";             //API Key variable
    private final long MAX_RESULTS = 30;    //max video return results
    private String key_word;                // keyword (Q) for searching

    //YouTube Object
    private YouTube youtube;
    YouTube.Search.List search;

    public ServicesCalls(){}

    public void setKeyWord(String key_word){
        this.key_word = key_word;
    }


    //-----------------------------------------------------------IMPLEMENTING API---------------------------------------
    /*
     * Initialized search Object by providing api key and search option.
     * Define HttpTransport, JsonFactory, and HttpRequest initializer to build a search request.
     * A YouTube object will use these factories and transport object to make a request.
     *
     * ** A searchInit() method only call once.
     */
    public void searchInit(){
        try {
            File apiKeyFile = new File("c:/Users/Daniel Dang/YouTubeAPIKey.txt");       //API file location
            Scanner sc = new Scanner(apiKeyFile);                                       //Get API key
            this.apiKey = sc.nextLine();

            //Setup procedure for API calls
            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
                public void initialize(HttpRequest httpRequest) throws IOException {
                }
            };
            //Initialized YouTube object and search object.
            youtube = new YouTube.Builder(transport, jsonFactory, httpRequestInitializer).setApplicationName("YoutubePlayer").build();
            search = youtube.search().list("snippet");      //search.list type always of type "snippet"
            search.setKey(apiKey);                          //set api key
        } catch (GoogleJsonResponseException e) {
            System.err.println("Service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    /*
     * Execute a search and process search results
     * Transform search result and extract information from search results
     *
     * Can be call more than once after SearchInit() method is called.
     */
    public List<Video> search(){
        //Limited search field and set options for the search
        //More option can be set. -> Refer to YouTube Data API documentations.
        search.setFields("items(id/videoId, snippet/title, snippet/thumbnails/default/url)");
        search.setType("Video");
        search.setMaxResults(MAX_RESULTS);
        search.setQ(this.key_word);

        SearchListResponse searchResult;                            //Responses from executing a search routine via API Call
        List<SearchResult> results = new ArrayList<SearchResult>();            //Store results into list (unprocessed data)
        List<Video> videoSearchResults = new ArrayList<Video>();        //Store videos result into list (processed data)
        try {
            searchResult = search.execute();
            results = searchResult.getItems();
            if (results != null || results.size() > 0) {
                videoSearchResults = processResult(results);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoSearchResults;
    }

    /*
     * Process a list of unprocessed data search results into Video object.
     */
    private List<Video> processResult(List<SearchResult> results){
        //exit if list is empty
        if (results == null){
            System.out.println("no video in the list, exit now");
            System.exit(1);
        }

        ArrayList<Video> videoResultsList = new ArrayList<Video>();
        /*
         * Process response result into video object.
         * Get only Video{id, title, thumbnail URL}
         * Store a video object into a list.
         */
        for (int i = 0; i < results.size(); i++){
            if (results.get(i).getId() == null) {
                continue;
            }else {
                Video temp = new Video(results.get(i).getId().getVideoId(), results.get(i).getSnippet().getTitle(), results.get(i).getSnippet().getThumbnails().getDefault().getUrl());
                videoResultsList.add(temp);
                System.out.println("ID: " + temp.getId() + "Title: " + temp.getTitle() + "Image URL: " + temp.getThumbnailURL());
            }
        }
        return videoResultsList;
    }

    /*
     * Get related video by creating an new YouTube and Search object.
     *
     * Could make use of the searchInit() method from above, however currently encounter runtime error
     * occur on get related video queries via API call.
     */
    public List<Video> getRelatedVideos(String currentVideoID){
        YouTube.Search.List searchRel = null;
        YouTube youtubeTemp;

        SearchListResponse responseResults = null;
        List<SearchResult> videoResults = new ArrayList<SearchResult>();
        List<Video> videoSearchResults = new ArrayList<Video>();
        try {
            //Setup API call procedure
            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
                public void initialize(HttpRequest httpRequest) throws IOException {
                }
            };
            //Initialized YouTube and Search Object
            youtubeTemp = new YouTube.Builder(transport, jsonFactory, httpRequestInitializer).setApplicationName("YoutubePlayer").build();

            //Set search options
            searchRel = youtubeTemp.search().list("snippet");
            searchRel.setKey(apiKey);
            searchRel.setPart("snippet");
            searchRel.setType("video");
            searchRel.setMaxResults(MAX_RESULTS);
            searchRel.setRelatedToVideoId(currentVideoID);

            //execute search
            responseResults = searchRel.execute();
            videoResults = responseResults.getItems();
            videoSearchResults = processResult(videoResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoSearchResults;
    }
}
