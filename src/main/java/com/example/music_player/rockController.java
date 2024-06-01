package com.example.music_player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class rockController {

    @FXML
    private ListView<String> rocklist; // Modify the type parameter of ListView to String

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
            // Load data into ListView
            loadRockPlaylist();
        }
    }

    private void loadRockPlaylist() {
        // SQL query to select song_name from all_songs joined with rock_playlist
        String query = "SELECT a.song_name FROM all_songs a, rock_playlist r WHERE a.song_id=r.song_id";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing items
            rocklist.getItems().clear();

            // Populate ListView with song names
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                rocklist.getItems().add(songName);
            }
        } catch (SQLException e) {
            System.err.println("Error loading rock playlist: " + e.getMessage());
        }
    }

    @FXML
    void backrock(ActionEvent event) {
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
    void playrock(MouseEvent event) {
        // Get the selected song name from the ListView
        String selectedSong = rocklist.getSelectionModel().getSelectedItem();

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
