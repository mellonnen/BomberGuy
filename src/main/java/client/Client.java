package client;

import communication.ClientStatus;
import game.GameConstants;
import game.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends JPanel implements ClientWindow {

  private JFrame frame;
  private LobbyPanel startContainer;
  private JPanel gameContainer;
  private JLayeredPane layeredGamePane;
  private PlayerStatusPanel playerStatusPanel;
  private GameGridClient gameGridClient;
  private SetupPanel setupPanel;
  private ClientIO clientIO;

  private static int APP_WINDOW_TOP_OFFSET = 0;
  private static int WINDOW_WIDTH = 40 + (GameConstants.GRID_SIZE * WindowConstants.WINDOW_SIZE_MULTIPLIER);
  private static int WINDOW_HEIGHT =
      65 + (GameConstants.GRID_SIZE * WindowConstants.WINDOW_SIZE_MULTIPLIER + APP_WINDOW_TOP_OFFSET);

  public static void main(String[] args) throws IOException, URISyntaxException {
    Client client = new Client();
    client.gameGridClient = new GameGridClient();
    client.clientIO = new ClientIO(client, client.gameGridClient);
    client.createMainWindow();
    client.createPanels();
    client.createButtons();
    client.createStartWindow();
  }

  public void createMainWindow() {
    frame = new JFrame();
    frame.setTitle("BomberGuy");
    frame.setResizable(false);
    frame.setFocusable(true);
    APP_WINDOW_TOP_OFFSET = frame.getInsets().top;
    frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    frame.getContentPane().setLayout(new BorderLayout());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  public void createPanels() {
    setupPanel = new SetupPanel();
    setupPanel.setOpaque(false);
    setupPanel.setBorder(BorderFactory.createEmptyBorder());
    setupPanel
        .setBounds((int) ((WINDOW_WIDTH / 2) - (WindowConstants.READY_PLAYERS_WIDTH_PERCENTAGE * WINDOW_WIDTH / 2)),
            (int) ((WINDOW_HEIGHT / 2) - (WindowConstants.READY_PLAYERS_HEIGHT_PERCENTAGE * WINDOW_HEIGHT / 4)),
            (int) (WindowConstants.READY_PLAYERS_WIDTH_PERCENTAGE * WINDOW_WIDTH),
            (int) (0.125 * WINDOW_HEIGHT));
    startContainer = new LobbyPanel(WINDOW_WIDTH, WINDOW_HEIGHT);
    startContainer.setBorder(
        BorderFactory.createEmptyBorder(WindowConstants.WINDOW_PADDING, WindowConstants.WINDOW_PADDING,
            WindowConstants.WINDOW_PADDING - 1, WindowConstants.WINDOW_PADDING - 1));
    startContainer.setLayout(null); // absolute positioning
  }

  public void createButtons() {
    JTextField textInput = new JTextField();
    textInput.setOpaque(false);
    textInput.setBackground(new Color(0, 0, 0, 0));
    textInput.setBorder(BorderFactory.createEmptyBorder());
    textInput.setHorizontalAlignment(JTextField.CENTER);
    textInput.setFont(WindowConstants.PLAYER_NAME_INPUT_FONT);
    textInput.setBounds((int) ((WINDOW_WIDTH / 2) - (WindowConstants.NAME_INPUT_WIDTH_PERCENTAGE * WINDOW_WIDTH / 2)),
        (int) (WindowConstants.NAME_INPUT_TOP_OFFSET_PERCENTAGE * WINDOW_HEIGHT),
        368,
        75);

    JButton actionButton = new JButton();
    actionButton.setOpaque(false);
    actionButton.setBorder(null);
    actionButton.setBorderPainted(false);
    actionButton.setContentAreaFilled(false);
    actionButton.setBackground(new Color(0, 0, 0, 0));
    textInput.setBorder(BorderFactory.createEmptyBorder());
    actionButton.addActionListener(e -> {
      actionButton.removeActionListener(actionButton.getActionListeners()[0]);
      String serverIP = textInput.getText();
      clientIO.startServerConnection(serverIP);
      textInput.setText("");
      startContainer.setInLobby();
      actionButton.addActionListener(l -> {
        clientIO.setClientName(textInput.getText());
        clientIO.setReady();
        startContainer.setPressedReady();
        startContainer.remove(textInput);
        startContainer.remove(actionButton);
        new Timer().schedule(new TimerTask() {
          @Override
          public void run() {
            startContainer.add(setupPanel);
          }
        }, WindowConstants.DEFAULT_ATTENTION_DELAY);
      });
    });
    actionButton
        .setBounds((int) ((WINDOW_WIDTH / 2) - (WindowConstants.READY_BUTTON_WIDTH_PERCENTAGE * WINDOW_WIDTH / 2)),
            (int) (WindowConstants.READY_BUTTON_TOP_OFFSET_PERCENTAGE * WINDOW_HEIGHT),
            (int) (WINDOW_WIDTH * WindowConstants.READY_BUTTON_WIDTH_PERCENTAGE),
            (int) (WINDOW_HEIGHT * WindowConstants.READY_BUTTON_HEIGHT_PERCENTAGE));

    startContainer.add(textInput);
    startContainer.add(actionButton);
  }

  public void createStartWindow() {
    frame.getContentPane().add(startContainer, BorderLayout.CENTER);
    frame.pack();
  }

  @Override
  public void createGameWindow() {
    frame.getContentPane().remove(startContainer);
    layeredGamePane = new JLayeredPane();
    layeredGamePane.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    gameContainer = new JPanel();
    gameContainer.setBorder(
        BorderFactory.createEmptyBorder(WindowConstants.WINDOW_PADDING, WindowConstants.WINDOW_PADDING,
            WindowConstants.WINDOW_PADDING - 1, WindowConstants.WINDOW_PADDING - 1));
    gameContainer.setLayout(new BorderLayout());
    gameContainer.add(gameGridClient, BorderLayout.CENTER);
    gameContainer.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    layeredGamePane.add(gameContainer, Integer.valueOf(0));
    frame.getContentPane().add(layeredGamePane, BorderLayout.CENTER);
    frame.addKeyListener(new ClientKeyListener(this.clientIO));
    frame.pack();
  }

  @Override
  public void showClientStatus(ClientStatus status) {
    if (playerStatusPanel != null) return;
    playerStatusPanel = new PlayerStatusPanel(status);
    playerStatusPanel.setBounds(
        WINDOW_WIDTH / 2 - (playerStatusPanel.getWidth() / 2),
        WINDOW_HEIGHT / 2 - (playerStatusPanel.getHeight()),
        playerStatusPanel.getWidth(),
        playerStatusPanel.getHeight());
    layeredGamePane.add(playerStatusPanel, Integer.valueOf(1));
    frame.getContentPane().add(layeredGamePane, BorderLayout.CENTER);
    frame.pack();
  }

  @Override
  public void updateLobbyPanel(Map<Player, Boolean> playerStatus) {
    if (setupPanel == null) return;
    setupPanel.setPlayerStatus(playerStatus);
  }

  @Override
  public void removeStartWindow() {
    if (startContainer != null) frame.getContentPane().remove(startContainer);
  }

}
