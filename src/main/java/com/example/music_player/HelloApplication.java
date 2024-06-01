package com.example.music_player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {

    private Button loginButton;
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

    @Override
    public void start(Stage primaryStage) {
        // Attempt to establish database connection
        connection = establishConnection();
        if (connection == null) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to database.");
            return;
        }

        // JavaFX setup
        primaryStage.setTitle("Login");

        // Load background image from local file
        Image backgroundImage = new Image("file:///C:/Users/Mrityunjaya Sharma/Desktop/dbms intellij/project/Music_Player/src/main/resources/Images/login.jpg");
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(600, 500, false, false, false, false));
        Background backgroundWithImage = new Background(background);

        // Create a GridPane layout for the login scene
        GridPane loginRoot = new GridPane();
        loginRoot.setBackground(backgroundWithImage);
        loginRoot.setPadding(new Insets(20));
        loginRoot.setHgap(10);
        loginRoot.setVgap(10);

        // Username and Password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Login button
        loginButton = new Button("Login"); // Initialize loginButton
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            // Check username and password
            if (checkCredentials(usernameField.getText(), passwordField.getText())) {
                String username = usernameField.getText();
                openNewScene(primaryStage, usernameField.getText()); // Pass primaryStage to openNewScene method
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        });

        // Add components to the grid
        loginRoot.add(usernameField, 0, 0);
        loginRoot.add(passwordField, 0, 1);
        loginRoot.add(loginButton, 1, 1);

        // Align the login button to the bottom right
        loginRoot.setAlignment(Pos.CENTER);

        // Create a new scene with the login layout
        Scene loginScene = new Scene(loginRoot, 600, 500);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private boolean checkCredentials(String username, String password) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'");
            return resultSet.next(); // If a record is found, credentials are correct
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openNewScene(Stage primaryStage, String username) {
        try {

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/music_player/hello-view.fxml"));
            Parent root = loader.load();


            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
