package Chat.Server;

import javax.swing.*;
//Driver code for the server
public class ServeTest {
    public static void main(String[] args) {
        Server sally = new Server();
        sally.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sally.startRun();
    }
}
