package yeet;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
// The game's main menu
public class Menu extends JFrame implements ActionListener, Runnable {
    JLabel jlab = new JLabel();
    JFrame jfrm = new JFrame("Minesweeper");
    OptionsMenu optionsMenu = new OptionsMenu();
    Scoreboard scoreboard = new Scoreboard();
    // Constructor
    Menu() throws IOException, ClassNotFoundException {
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(700,480);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar jmb = new JMenuBar();
        // Adding image
        BufferedImage myPicture = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("imag.jpg")));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        // Adding menu items (game menu)
        JMenu jmGame = new JMenu("Game");
        JMenuItem jmiNewGame = new JMenuItem("Play Game");
        JMenuItem jmiScore = new JMenuItem("Show Scoreboard");
        JMenuItem jmiExit = new JMenuItem("Exit Game");
        jmGame.add(jmiNewGame);
        jmGame.add(jmiScore);
        jmGame.add(jmiExit);
        jmb.add(jmGame);
        // Adding options menu
        JMenu jmOptions = new JMenu("Options");
        JMenuItem jmiOptions = new JMenuItem("Set Options");
        jmOptions.add(jmiOptions);
        jmb.add(jmOptions);
        // Adding action listeners
        jmiNewGame.addActionListener(this);
        jmiScore.addActionListener(this);
        jmiOptions.addActionListener(this);
        jmiExit.addActionListener(this);
        // Content pane
        jfrm.getContentPane().setBackground(Color.MAGENTA);
        jfrm.add(picLabel);
        jfrm.add(jlab);
        jfrm.setJMenuBar(jmb);
        jfrm.setVisible(true);
    }
    // During runtime we start the muzsika.wav file
    @Override
    public void run() {
        // Audio file
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();

            // Safely get the input stream, checking for null to avoid NullPointerException
            InputStream inputStream = Menu.class.getResourceAsStream("muzsika.wav");
            if (inputStream != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream);
                clip.open(ais);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("Could not find audio file: muzsika.wav");
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        // Checking options (just for music)
        while(true) {
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Only control audio if clip was successfully loaded
            if (clip != null && clip.isOpen()) {
                if (!optionsMenu.music) {
                    clip.start();
                } else {
                    clip.stop();
                }
            }
        }
    }
    // Setting what happens when each menu item is clicked
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        String cmStr = ae.getActionCommand();
        switch (cmStr) {
            case "Play Game":
                GUI gui = null;
                try {
                    gui = new GUI();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                new Thread(gui).start();
                if(this.optionsMenu.hardmode){
                    assert gui != null;
                    gui.randomStuff = true;
                }
                jfrm.dispose();
                break;
            case "Show Scoreboard":
                scoreboard.setVisible(true);
                break;
            case "Set Options":
                optionsMenu.makeVisible();
                break;
            case "Exit Game":
                System.exit(0);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cmStr);
        }
    }
}