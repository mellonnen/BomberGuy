package server;

import server.utility.MessageConsole;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Server {

  private GameGridServer gameGridServer;

  public static void main(String[] args) throws IOException {
    Server server = new Server();
    server.createMainWindow();
    server.gameGridServer = new GameGridServer();
    new ServerIO(server.gameGridServer);
  }

  public void createMainWindow() {
    JFrame frame = new JFrame();
    frame.setTitle("BomberGuy Server");
    frame.setResizable(false);
    frame.setFocusable(true);
    frame.setPreferredSize(new Dimension(800, 400));
    frame.getContentPane().setLayout(new BorderLayout());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    JTextArea consoleTextArea = new JTextArea();
    consoleTextArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
    consoleTextArea.setForeground(Color.white);
    consoleTextArea.setBackground(Color.black);
    JScrollPane scrollPane = new JScrollPane(consoleTextArea);

    MessageConsole mc = new MessageConsole(consoleTextArea);
    mc.redirectOut();
    mc.redirectErr(Color.RED, null);
    frame.getContentPane().add(scrollPane);
    frame.pack();
  }

}
