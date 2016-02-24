package sample;


import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.io.*;
import java.net.*;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    public Button connect;
    @FXML
    public Button disconnect;
    @FXML
    public Label status;
    @FXML
    public TextArea consoll;
    @FXML
    public TextArea display;
    @FXML
    public ListView<String> liste;

    private ServerTask serverTask;



    public Controller() {
        serverTask = new ServerTask(5555);
    }


    public void initialize(URL location, ResourceBundle resources) {


        liste.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Listen to selection of an item in the list of clients, and accordingly update it's most recent message
        liste.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableval, String oldval, String newval) {
                String client = liste.getSelectionModel().getSelectedItem();
                System.out.println((serverTask.getClientMessageMap().get(client)));
            }
        });

        // bind the list of clients stored as keys in the clientMessageMap to the itemsProperty of the lstClients
        // ListView
        ObservableMap<String, String> clientMessageMap = serverTask.getClientMessageMap();
        liste.itemsProperty().bind(Bindings.createObjectBinding(() ->
                FXCollections.observableArrayList(clientMessageMap.keySet()), clientMessageMap));

        // bind the texLog's text property with the message property of the serverTask object
        display.textProperty().bind(serverTask.messageProperty());

    }


    Thread avslutt = new Thread(){
        public void run() {
            try {
                Thread.sleep(500);
                System.exit(0);
            }catch (InterruptedException e){
                System.out.println("Fitte");
            }
        }
    };
    int tall, temp;
    String tekst = "1 ";


    public void clickConnect() throws IOException {
        status.setText("Online");
        status.setTextFill(Color.GREEN);
        if (!serverTask.isRunning()) {
            serverTask.start();

            System.out.println("Server running on IP: " + InetAddress.getLocalHost().getHostAddress() + " Port: 5555");
            consoll.appendText(" Server Online!");
            consoll.appendText("\n~root$: Running on IP: " + InetAddress.getLocalHost().getHostAddress() + " Port: 5555");
        } else {
            consoll.appendText("\n~root$: Server is already running...!");
        }
    }

    public void clickDisconnect() {
        status.setText("Offline");
        status.setTextFill(Color.RED);

        consoll.appendText("\n~root$: Systemet avsluttes...");
        avslutt.start();
    }



}//End of class Controller
