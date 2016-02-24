package sample;

import com.sun.corba.se.spi.activation.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
        primaryStage.setTitle("Server application");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }



    public static void main(String[] args) throws IOException{
        launch(args);
    }//End of main



}//End of main class
