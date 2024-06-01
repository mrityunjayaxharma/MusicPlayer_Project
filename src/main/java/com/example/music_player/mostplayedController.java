package com.example.music_player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
public class mostplayedController {

    @FXML
    private Button back_more;

    @FXML
    private ListView<String> most_played_list1;

    @FXML
    private ListView<String> most_played_list2;

    @FXML
    private ListView<String> recently_played_list1;

    @FXML
    private ListView<String> recently_played_list2;

    private Connection connection;

    @FXML
    void initialize() {
        // Establish SQL connection
        connection = establishConnection();
        if (connection == null) {
            System.err.println("Failed to establish connection.");
            // Handle the error or show a message to the user
        }

        // Load data into the lists when the controller is initialized
        loadRecentlyPlayed1();
        loadRecentlyPlayed2();
        loadMostPlayed1();
        loadMostPlayed2();
    }



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

    private void loadRecentlyPlayed1() {
        try {
            // Execute query to fetch recently played songs
            String query = "SELECT distinct(song_name) FROM recently_played_view where user_id=1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Populate the list view with recently played songs
            List<String> songs = new ArrayList<>();
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                songs.add(songName);
            }
            recently_played_list1.getItems().addAll(songs);

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void loadRecentlyPlayed2() {
        try {
            // Execute query to fetch recently played songs
            String query = "SELECT distinct(song_name) FROM recently_played_view where user_id=2";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Populate the list view with recently played songs
            List<String> songs = new ArrayList<>();
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                songs.add(songName);
            }
            recently_played_list2.getItems().addAll(songs);

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void loadMostPlayed1() {
        try {
            // Execute query to fetch most played songs
            String query = "SELECT song_name FROM most_played_view where user_id=1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Populate the list view with most played songs
            List<String> songs = new ArrayList<>();
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                songs.add(songName);
            }
            most_played_list1.getItems().addAll(songs);

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void loadMostPlayed2() {
        try {
            // Execute query to fetch most played songs
            String query = "SELECT song_name FROM most_played_view where user_id=2";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Populate the list view with most played songs
            List<String> songs = new ArrayList<>();
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                songs.add(songName);
            }
            most_played_list2.getItems().addAll(songs);

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    @FXML
    void back_more(ActionEvent event) {
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
}
