package com.example.music_player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class otherController {

    @FXML
    private ListView<String> electroniclist;

    @FXML
    private Button backother;

    @FXML
    private Button morebutton;

    @FXML
    private ListView<String> Classiclist;

    private Connection connection;

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
        // Establish SQL connection
        connection = establishConnection();
        if (connection == null) {
            System.err.println("Failed to establish connection.");
            // Handle the error or show a message to the user
        } else {
            // Load data into ListViews
            loadElectronicPlaylist();
            loadClassicPlaylist();
        }
    }

    private void loadElectronicPlaylist() {
        // SQL query to select song_name from all_songs joined with electronic_playlist
        String query = "SELECT a.song_name FROM all_songs a, electronic_playlist e WHERE a.song_id=e.song_id";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing items
            electroniclist.getItems().clear();

            // Populate ListView with song names
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                electroniclist.getItems().add(songName);
            }
        } catch (SQLException e) {
            System.err.println("Error loading electronic playlist: " + e.getMessage());
        }
    }

    private void loadClassicPlaylist() {
        // SQL query to select song_name from all_songs joined with classical_playlist
        String query = "SELECT a.song_name FROM all_songs a, classical_playlist c WHERE a.song_id=c.song_id";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing items
            Classiclist.getItems().clear();

            // Populate ListView with song names
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                Classiclist.getItems().add(songName);
            }
        } catch (SQLException e) {
            System.err.println("Error loading country playlist: " + e.getMessage());
        }
    }

    @FXML
    void BackOther(ActionEvent event) {
        try {
            // Load the FXML file for the main scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the main scene
            Scene mainScene = new Scene(root, 600, 400);
            stage.setScene(mainScene);
            stage.setTitle("Music Player");
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML file", e);
        }
    }

    @FXML
    void openmore(ActionEvent event) {
        try {
            // Load the FXML file for the main scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/mostplayed.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the main scene
            Scene mainScene = new Scene(root, 600, 400);
            stage.setScene(mainScene);
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML file", e);
        }

    }

    @FXML
    void playClassic(MouseEvent event) {
        String selectedSong = Classiclist.getSelectionModel().getSelectedItem();

        // Check if a song is selected
        if (selectedSong != null && !selectedSong.isEmpty()) {
            // Query the database to get the song_id
            String query = "SELECT song_id FROM all_songs WHERE song_name LIKE ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, selectedSong);
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if the result set has a record
                if (resultSet.next()) {
                    int songId = resultSet.getInt("song_id");

                    // Invoke the playSongById method from the HelloController class to play the song
                    HelloController helloController = new HelloController();
                    helloController.playSongById(songId);

                    // Get the current stage
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    // Load the FXML file for the main scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/hello-view.fxml"));
                    Parent root = loader.load();

                    // Set the main scene
                    Scene mainScene = new Scene(root, 600, 400);

                    Song_ID.setSong_ID(songId);

                    stage.setScene(mainScene);
                    stage.setTitle("Music Player");
                } else {
                    System.err.println("No song found with the name: " + selectedSong);
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.err.println("No song selected.");
        }
    }
        // Implement logic for playing a song from the classical playlist

    @FXML
    void playElectric(MouseEvent event) {
        // Implement logic for playing a song from the country playlist
        String selectedSong = electroniclist.getSelectionModel().getSelectedItem();

        // Check if a song is selected
        if (selectedSong != null && !selectedSong.isEmpty()) {
            // Query the database to get the song_id
            String query = "SELECT song_id FROM all_songs WHERE song_name LIKE ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, selectedSong);
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if the result set has a record
                if (resultSet.next()) {
                    int songId = resultSet.getInt("song_id");

                    // Invoke the playSongById method from the HelloController class to play the song
                    HelloController helloController = new HelloController();
                    helloController.playSongById(songId);

                    // Get the current stage
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    // Load the FXML file for the main scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/hello-view.fxml"));
                    Parent root = loader.load();

                    // Set the main scene
                    Scene mainScene = new Scene(root, 600, 400);

                    Song_ID.setSong_ID(songId);

                    stage.setScene(mainScene);
                    stage.setTitle("Music Player");
                } else {
                    System.err.println("No song found with the name: " + selectedSong);
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.err.println("No song selected.");
        }
    }
    }
