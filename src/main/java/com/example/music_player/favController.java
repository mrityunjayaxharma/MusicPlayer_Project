package com.example.music_player;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class favController {

    @FXML
    private Button RemoveButton;

    @FXML
    private ListView<String> favlist; // Assuming you have a ListView of Strings

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
            // Populate ListView with data from the liked_songs table
            populateListView();
        }
    }

    private void populateListView() {
        try {
            String query = "SELECT song_name FROM all_songs a JOIN liked_songs l ON a.song_id = l.song_id where user_id=1 ";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear previous items in the ListView
            favlist.getItems().clear();

            // Add song names to the ListView
            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                favlist.getItems().add(songName);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Remove() {
        // Get the selected item
        String selectedItem = favlist.getSelectionModel().getSelectedItem();

        // Remove the selected item from the list
        favlist.getItems().remove(selectedItem);

        // Delete the corresponding entry from the liked_songs table
        try {
            String query = "DELETE FROM liked_songs WHERE song_id in (SELECT song_id FROM all_songs WHERE song_name = ? And user_id = 1)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedItem);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void back(javafx.event.ActionEvent actionEvent) {

        try {
            // Load the FXML file for the main scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the main scene
            Scene mainScene = new Scene(root, 600, 400);
            stage.setScene(mainScene);
            stage.setTitle("Music Player");
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML file", e);
        }
    }
    }


