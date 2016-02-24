/**
 * Multiclient socket server with JavaFX: ServerTask class
 * DATS/ITPE2410 Networking and Cloud Computing, Spring 2016
 * HiOA
 **/
package sample;

import javafx.collections.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *  ServerTask class which waits for a client to connect in a separate thread using (extending) Java's Task<> class
 */


public class ServerTask extends Task<Void> {

    int portNumber;

    private ObservableMap<String, String> clientMessageMap = FXCollections.observableMap(new HashMap<>());
    public ObservableMap<String, String> getClientMessageMap() {return clientMessageMap;}

    public ServerTask(int portNumber)
    {
        this.portNumber = portNumber;
    }





    @Override
    public Void call() throws Exception
    {
        try (ServerSocket serverSocket = new ServerSocket(portNumber))
        {
            while (true)
            {
                Socket conn = serverSocket.accept();

                ClientService cs = new ClientService(conn);
                String client = conn.getInetAddress().getHostAddress();

                // Listens to the changes in the messageProperty of cs object
                cs.messageProperty().addListener((obs, oldMessage, newMessage) ->
                {
                    switch(newMessage.toLowerCase())
                    {
                        case "connected":
                            clientMessageMap.put(client, "");break;
                        case "disconnected":
                            clientMessageMap.remove(client);break;
                        default:
                            clientMessageMap.put(client,newMessage);
                    }
                    updateMessage("Client: [" + client +  "]: " + newMessage);
                });

                cs.start();
            }
        } catch (IOException e)
        {
            System.out.println("Exception!!! "+e.getMessage());
        }

        return null;
    }//End call()

    public void start()
    {
        Thread t = new Thread(this);
        t.start();

    }//End start()


    /**
     *  ClientService class which serves a client in a separate thread using (extending) Java's Service<> class
     */
    private static class ClientService extends Service<Void> {

        //Variabler vi trenger
        Socket connectSocket;
        private String client;


        //Konstruktøren
        public ClientService(Socket connectSocket)
        {
            this.connectSocket = connectSocket;
            client = connectSocket.getInetAddress().getHostAddress();
        }




        //-----------------------Start på tasks----------------------

        int antBil1 = 0;
        String antString1;
        int antBil2 = 0;
        String antString2;
        int antBil3 = 0;
        String antString3;
        int antBil4 = 0;
        String antString4;
        Boolean on = true;

        public String lys1, lys2, lys3, lys4;

        private Service<Void> nedTelling = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        System.out.println("nedtelling kjører");

                        try (PrintWriter out = new PrintWriter(connectSocket.getOutputStream(), true);
                             BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream())))
                        {
                            String inText;
                            while ((inText = in.readLine()) != null) {
                                if (inText.equals("nybil1")) {
                                    antBil1++;
                                }
                                if (inText.equals("nybil2")) {
                                    antBil2++;
                                }
                                if (inText.equals("nybil3")) {
                                    antBil3++;
                                }
                                if (inText.equals("nybil4")) {
                                    antBil4++;
                                }
                            }



                                //Funksjonen starter her----------------


                                while (on == true) {
                                    Thread.sleep(500);

                                    //SjekkeBil();
                                    if ((antBil2 + antBil4) > 0 && (antBil2 + antBil4) > (antBil1 + antBil3)) {

                                        long t = System.currentTimeMillis();
                                        long end = t + 10000;
                                        while (System.currentTimeMillis() < end && (antBil2 + antBil4) > 0) {

                                            System.out.println("lys på 2 & 4");
                                            if (antBil2 > 0) antBil2--;
                                            if (antBil4 > 0) antBil4--;
                                            Thread.sleep(1500);

                                            lys2 = antBil2 + "";
                                            out.println(lys2);

                                            lys4 = antBil4 + "";
                                            out.println(lys4);



                                            //lys2.setText(antBil2 + ""); // Gjøres hos klienten
                                            //lys4.setText(antBil4 + ""); // Gjøres hos klienten

                                        }
                                    } else if ((antBil1 + antBil3) > 0 && (antBil1 + antBil3) >= (antBil2 + antBil4)) {

                                        long t = System.currentTimeMillis();
                                        long end = t + 10000;
                                        while (System.currentTimeMillis() < end && (antBil1 + antBil3) > 0) {

                                            System.out.println("lys på 1 & 3");
                                            if (antBil1 > 0) antBil1--;
                                            if (antBil3 > 0) antBil3--;
                                            Thread.sleep(1500);
                                            lys1 = antBil1 + "";
                                            lys3 = antBil3 + "";

                                            //lys1.setText(antBil1 + ""); //Gjøres hos klienten
                                            //lys3.setText(antBil3 + ""); //Gjøres hos klienten
                                        }
                                    }
                                }
                            }//While lytteren er ferdig her



                                //Funksjonen ferdig her----------------


                            }
                        } catch (IOException e) {
                            System.out.println("Exception!!! "+e.getMessage());
                        }

                        return null;
                    }
                };
            }
        };//End of nedTelling





        @Override
        public Task<Void> createTask()
        {
            Task<Void> task =  new Task<Void>()
            {
                @Override
                public Void call() throws InterruptedException
                {
                    updateMessage("Connected");

                    try (PrintWriter out = new PrintWriter(connectSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream())))
                    {
                        String inText;
                        //int lys1 = 0;

                        while ((inText = in.readLine()) != null) {

                            System.out.println(inText);


                            out.println();


                            if (inText.equals("lys1")) {
                                lys1++;
                                out.println(lys1+"");
                                System.out.println(lys1);
                            }

                            //updateMessage(" " + inText);

                            //String outText = ProcessString(inText);
                            //out.println(outText);

                            updateMessage("Lys1 har nå " + lys1 + " biler");
                        }
                    } catch (IOException e)
                    {
                        System.out.println("Exception!!! "+e.getMessage());
                    }
                    finally
                    {
                        updateMessage("Disconnected"); // null - indicate client disconnection
                    }
                    return null;
                }
            };
            return task ;
        }

        //-----------------------Slutt på tasks----------------------


        //-------------------------Random metoder vi trenger-----------------




        private String ProcessString(String intext)
        {
            //String outtext = new StringBuffer(intext).toString();

            return null;
        }


        public void SjekkeBil( ) {
            int ant1 = (antBil1 + antBil3);
            int ant2 = (antBil2 + antBil4);

            if (ant2 < ant1) {
            /*
            Selve byttingen foregår hos klienten
            bytt1_grønn();
            bytt3_grønn();
            bytt2_rød();
            bytt4_rød();
            */
            } else if (ant2 > ant1) {
            /*
            bytt1_rød();
            bytt3_rød();
            bytt2_grønn();
            bytt4_grønn();
            */
            } else {
            /*
            bytt1_rød();
            bytt2_rød();
            bytt3_rød();
            bytt4_rød();
            */
            }

        }//End of SjekkeBil


        //---------------------------------------------------------------------------
        //-------------------Stæsj fra Kristoffer------------------------------------




    }//End of Client Service
}