package com.example.music_player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaErrorEvent;



import java.io.File;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class HelloController {

    int index=1;


    @FXML
    private ProgressBar ProgBar;

    @FXML
    private Label SongName;

    @FXML
    private Button artistname;

    @FXML
    private ImageView albumArt;

    @FXML
    private ImageView heart;

    @FXML
    private ListView<String> listview; // Changed to specify type parameter as String

    @FXML
    private ImageView other;

    @FXML
    private ImageView pop;

    @FXML
    private ImageView rap;

    @FXML
    private ImageView rock;

    @FXML
    private Slider volslide;

    private MediaPlayer mediaPlayer;


    private Connection connection;


    public String singerName;

    int song_ID=-1;





    private Connection establishConnection() {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/musicplayer";
        String username = "root";
        String password = "project";

        try {
            // Attempt to establish connection
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to MySQL database established successfully.");
            return connection;
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL database: " + e.getMessage());
            return null;
        }
    }


    @FXML
    void initialize() {

        // Establish connection
        connection = establishConnection();

        // Check if connection is successful
        if (connection != null) {
            String query = "SELECT audio_file, singer_name, song_name FROM liked_songs_view WHERE user_id = 1 and index_number=1";
            try (PreparedStatement statement = connection.prepareStatement(query)) {

                ResultSet resultSet = statement.executeQuery();

                // Check if there are any records returned
                if (resultSet.next()) {
                    String audioFile = resultSet.getString("audio_file");
                    singerName = resultSet.getString("singer_name");
                    String songName = resultSet.getString("song_name");

                    SongName.setText(songName);

                    artistname.setText(singerName);

                    song_ID=Song_ID.getSong_ID();


                    if(song_ID!=-1){
                        playSongById(song_ID);
                    }else {

                        initializeMediaPlayer(audioFile);
                    }
                } else {
                    System.out.println("No recently played songs found for the user.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            String query = "SELECT a.song_name FROM all_songs a, liked_songs l WHERE a.song_id=l.song_id AND l.user_id=1";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();

                // Clear previous items in the ListView
                listview.getItems().clear();

                // Add song names to the ListView
                while (resultSet.next()) {
                    String songName = resultSet.getString("song_name");
                    listview.getItems().add(songName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to establish connection.");
        }
    }



    private void initializeMediaPlayer(String audioFile) {
        System.out.println("Initializing media player with audio file: " + audioFile); // Print audio file path for debugging
        Media media = new Media(new File(audioFile).toURI().toString());
        //System.out.println("Media duration: " + media.getDuration()); // Print media duration for debugging
        mediaPlayer = new MediaPlayer(media);

        // Add listener for debugging
        mediaPlayer.setOnError(() -> {
            System.err.println("Media error occurred.");
        });



        /*mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("End of media");
        });*/

        // Add more listeners as needed for debugging

        // Add volume and progress listeners
        volslide.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() / 200.0);
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            ProgBar.setProgress(newValue.toMillis() / mediaPlayer.getTotalDuration().toMillis());
        });

        /*mediaPlayer.setOnReady(() -> {
            SongName.setText(media.getMetadata().get("title").toString());
        });*/
    }




    @FXML
    void addtofav(MouseEvent event) {

    }

    @FXML
    void allplaylist(MouseEvent event) {
        try {
            // Pause the music when switching scene
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/other.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }

    }

    @FXML
    void next(MouseEvent event) {
        if (connection != null) {

            index=index+1;
            if(index==20){index=1;}
            playSongByIndex(index);

        }
    }

    void playnext(){
        if (connection != null) {

            index=index+1;
            if(index==19){index=1;}
            playSongByIndex(index);

        }
    }


    @FXML
    void openfavsongs(ActionEvent event) {
        try {
            // Pause the music when switching scene
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/favsongs.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }


    @FXML
    void pause(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    void play(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    void popplaylist(MouseEvent event) {
        try {
            // Pause the music when switching scene
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/pop.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }

    }

    @FXML
    void previous(MouseEvent event) {
        if (connection != null) {

            index=index-1;
            if(index==0){index=18;}

            playSongByIndex(index);

        }
    }


    @FXML
    void rapplaylist(MouseEvent event) {
        try {
            // Pause the music when switching scene
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/rap.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }

    }

    @FXML
    void rockplaylist(MouseEvent event) {
        try {
            // Pause the music when switching scene
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/rock.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }

    }

    @FXML

    public void OpenArtist(ActionEvent event) {
        try {
            // Pause the music when switching scene
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/artist.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            artistController artistController = loader.getController();

            // Call the populateSongsBySinger method with the desired singer name
            artistController.populateSongsBySinger(singerName);

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }



    public void playSongByIndex(int index) {
        if (connection != null) {
            String query = "SELECT audio_file, singer_name, song_name FROM liked_songs_view WHERE user_id = 1 AND index_number = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, index);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String audioFile = resultSet.getString("audio_file");
                    singerName = resultSet.getString("singer_name");
                    String songName = resultSet.getString("song_name");

                    SongName.setText(songName);
                    artistname.setText(singerName);

                    // Stop current song if playing
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    initializeMediaPlayer(audioFile);
                    mediaPlayer.play();
                } else {
                    System.out.println("No song found for the specified index.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void playSongById(int songId) {
        if (connection != null) {
            String query = "SELECT audio_file, singer_name, song_name FROM all_songs WHERE song_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, songId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String audioFile = resultSet.getString("audio_file");
                    singerName = resultSet.getString("singer_name");
                    String songName = resultSet.getString("song_name");

                    SongName.setText(songName);
                    artistname.setText(singerName);

                    // Stop current song if playing
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    initializeMediaPlayer(audioFile);
                    mediaPlayer.play();
                } else {
                    System.out.println("No song found for the specified song ID."+songId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
