package Chat.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame{
    private JTextField userTxt;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    //Connection btw 2 computer
    private Socket connection;
    private ServerSocket server;

    //Setup the GUI
    //Constructor
    public Server() {
        super("Instant Messaging");
        userTxt = new JTextField();
        userTxt.setEditable(false);
        userTxt.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userTxt.setText("");
                    }
                }
        );
        add(userTxt, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        //Set size pf the chat windows
        chatWindow.setSize(800,800);
        setVisible(true);
    }

    //Set up and run the server
    public void startRun() {
        try {
            //Edit the port here
            //Where do you want to connect
            //TODO: port is 6789
            int port = 6789;
            server = new ServerSocket(port,100);
            //Main programme
            while (true) {
                try {
                    //Connect and have conversation
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException e) {
                    showMessage("\n Server ended connection! ");
                } finally {
                    //House keeping stuff
                    closeCrap();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Wait for connection, then display connection information
    private void waitForConnection() throws IOException{
        showMessage(" Waiting for someone to connect...\n");
        //Setup Socket
        //Accept the upcomming connection to the socket
        //Create connection once time
        connection = server.accept();
        showMessage("... Connected to " + connection.getInetAddress().getHostName());

    }
    //Get stream to send and receive data
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        //Flush the crap
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup! \n");
    }

    //During the conversation
    private void whileChatting() throws IOException {
        String message = "  You are now connected! ";
        sendMessage(message);
        ableToType(true);
        do {
            try {
                message = (String)  input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException e) {
                showMessage("\n wtf is that!");
            }
        } while (!message.equals("CLIENT - END"));
        //The program gonna stop if user enter END
    }

    private void closeCrap() {
        showMessage("\n Closing connections... \n");
        //TODO: Implement Able to type
        ableToType(false);
        try {
            //Close io stream
            output.close();
            input.close();
            connection.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Send a message to client
    private void sendMessage (String input) {
        try {
            output.writeObject("SERVER - " + input);
            output.flush();
            showMessage("\nSERVER - " + input);
        } catch (IOException e) {
            chatWindow.append("\n ERROR . Can't send message -.-");
        }
    }

    //Update chat windows
    private void showMessage(final String text) {
	    //Update part of GUI
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        //Update the actual chat windows
                        chatWindow.append(text);
                    }
                }
                );
    }

    //let the user type into chatbox
    private void ableToType(final boolean trueorfalse) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        //Update the actual chat windows
                        userTxt.setEditable(trueorfalse);
                    }
                }
        );
    }
}
