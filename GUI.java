package yeet;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

// User interface class
public class GUI extends JFrame implements Runnable {
    // For displaying time
    int timeX = 1130;
    int timeY = 5;
    int sec = 0;
    // Needed for hard mode
    boolean randomStuff = false;
    // Objects
    Board board = new Board();
    Click click;
    Scoreboard scoreboard = new Scoreboard();
    PopupUsername popup = new PopupUsername();

    // Creating the game area and adding listeners
    public GUI() throws IOException, ClassNotFoundException {
        this.setTitle("Minesweeper");
        this.setSize(1286, 829);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        // Here we load the previous game state
        click = new Click();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("level.txt"));
        click.level =(Level) ois.readObject();
        ois.close();
        // The game saves the state when we click close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                try {
                    click.level.saveLevel();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        this.setContentPane(board);
        this.addMouseMotionListener(click.move);
        this.addMouseListener(click);
    }

    // During runtime, we repaint the field and check if the player has won
    @Override
    public void run() {
        int i;
        Random rand = new Random();
        while(true){
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.repaint();
            this.checkVictoryStatus();
            if(randomStuff) {
                i = rand.nextInt(20);
                this.click.setSpacing(i);
            }
        }
    }
    // Here we paint the field and draw
    public class Board extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.setColor(Color.MAGENTA);
            g.fillRect(0,0,1280,800);
            for(int i = 0 ; i < 16 ; i++){
                for(int j = 0 ; j < 9 ; j++) {
                    g.setColor(Color.CYAN);
                    if(click.level.revealed[i][j]){
                        g.setColor(Color.white);
                        if(click.level.mines[i][j] == 1){
                            g.setColor(Color.RED);
                        }
                    }
                    if(click.move.getMx() >= click.getSpacing()+i*80 &&
                            click.move.getMx() < click.getSpacing()+i*80+80-2*click.getSpacing() &&
                            click.move.getMy() >= click.getSpacing()+j*80+80+26 &&
                            click.move.getMy() < click.getSpacing()+j*80+26+80+80-2*click.getSpacing()){
                        g.setColor(Color.MAGENTA);
                    }
                    g.fillRect(click.getSpacing() + i*80,click.getSpacing() + j*80+80,80-2*click.getSpacing(),80-2*click.getSpacing());
                    if(click.level.revealed[i][j]){
                        g.setColor(Color.BLACK);
                        if(click.level.mines[i][j] == 0 && click.level.neighbours[i][j] != 0) {
                            if(click.level.neighbours[i][j] == 1){
                                g.setColor(Color.blue);
                            } else if(click.level.neighbours[i][j] == 2) {
                                g.setColor(Color.green);
                            } else if(click.level.neighbours[i][j] == 3) {
                                g.setColor(Color.red);
                            } else if(click.level.neighbours[i][j] == 5) {
                                g.setColor(new Color(178, 34, 34));
                                g.setColor(new Color(0, 0, 128));
                            } else if(click.level.neighbours[i][j] == 6) {
                                g.setColor(new Color(72, 209, 204));
                            } else if(click.level.neighbours[i][j] == 7) {
                                g.setColor(Color.black);
                            } else if(click.level.neighbours[i][j] == 8) {
                                g.setColor(Color.darkGray);
                            }
                            g.setFont(new Font("Tahoma", Font.BOLD, 40));
                            g.drawString(Integer.toString(click.level.neighbours[i][j]), i * 80 + 27, j * 80 + 80 + 55);
                        } else if(click.level.mines[i][j] == 1) {
                            g.fillRect(i*80+10+20,j*80+80+20,20,40);
                            g.fillRect(i*80+20,j*80+80+10+20,40,20);
                            g.fillRect(i*80+5+20,j*80+80+5+20,30,30);
                            g.fillRect(i*80+38,j*80+80+15,4,50);
                            g.fillRect(i*80+15,j*80+80+38,50,4);
                        }
                    }
                    // Drawing flag
                    if(click.level.flagged[i][j]){
                        g.setColor(Color.BLACK);
                        g.fillRect(i*80+32+5,j*80+80+10+5,5,40); // pole
                        g.fillRect(i*80+20+5, j*80+80+50+5,30,10); // base
                        g.setColor(Color.RED);
                        g.fillRect(i*80+16+5,j*80+80+15+5,20,15); // flag
                        g.setColor(Color.BLACK);
                        g.drawRect(i*80+16+5,j*80+80+15+5,20,15);
                        g.drawRect(i*80+17+5,j*80+80+16+5,18,13);
                        g.drawRect(i*80+18+5,j*80+80+17+5,16,11);
                    }
                }
            }
            // spacing
            g.setColor(Color.CYAN);
            g.fillRect(click.getSpacingX(),click.getSpacingY(),300,60);
            // spacing -
            g.setColor(Color.WHITE);
            g.fillRect(click.getMinusX()+5,click.getMinusY()+10,40,40);
            // spacing +
            g.setColor(Color.WHITE);
            g.fillRect(click.getPlusX()+5,click.getPlusY()+10,40,40);
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("Tahoma",Font.PLAIN, 35));
            g.drawString("Spacing",click.getSpacingX()+20,click.getSpacingY()+45);
            // minus and plus
            g.fillRect(click.getMinusX()+15,click.getMinusY()+27,20,6);
            g.fillRect(click.getPlusX()+15,click.getPlusY()+27,20,6);
            g.fillRect(click.getPlusX()+22,click.getPlusY()+20,6,20);
            g.setFont(new Font("Tahoma",Font.PLAIN,30));
            if (click.getSpacing() < 10) {
                g.drawString("0"+ click.getSpacing(), click.getMinusX()+49,click.getMinusY()+40);
            } else {
                g.drawString(Integer.toString(click.getSpacing()),click.getMinusX()+49,click.getMinusY()+40);
            }
            // smiley stuff
            if(click.getFlagger()) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.YELLOW);
            }
            g.fillOval(click.getSmileyX()-65,click.getSmileyY(),200,70);
            g.setColor(Color.YELLOW);
            g.fillOval(click.getSmileyX(),click.getSmileyY(),70,70);
            // eyes
            g.setColor(Color.BLACK);
            g.fillOval(click.getSmileyX()+15,click.getSmileyY()+15,10,10);
            g.fillOval(click.getSmileyX()+15+40,click.getSmileyY()+15,10,10);
            // mouth
            if(click.happiness) {
                g.fillRect(click.getSmileyX()+20,click.getSmileyY()+50,30,5);
                g.fillRect(click.getSmileyX()+17,click.getSmileyY()+45,5,5);
                g.fillRect(click.getSmileyX()+48,click.getSmileyY()+45,5,5);
            } else {
                g.fillRect(click.getSmileyX()+20,click.getSmileyY()+45,30,5);
                g.fillRect(click.getSmileyX()+17,click.getSmileyY()+50,5,5);
                g.fillRect(click.getSmileyX()+48,click.getSmileyY()+50,5,5);
            }
            // timer
            g.setColor(Color.BLACK);
            g.fillRect(timeX,timeY,140,70);
            if(!click.defeat && !click.victory){
                sec = (int) ((new Date().getTime()-click.startDate.getTime())/1000);
            }

            if(sec > 999) sec = 999;

            g.setColor(Color.yellow);
            if(click.victory) g.setColor(Color.GREEN);
            else if (click.defeat) g.setColor(Color.RED);
            g.setFont(new Font("Tahoma",Font.PLAIN,80));
            if(sec < 10) {
                g.drawString("00"+ sec,timeX,timeY+65);
            } else if (sec < 100) {
                g.drawString("0"+ sec,timeX,timeY+65);
            } else {
                g.drawString(Integer.toString(sec), timeX, timeY + 65);
            }
            // victory message painting
            if(click.victory) {
                g.setColor(Color.GREEN);
                click.setVicMes("You win!!!");
            } else if(click.defeat) {
                g.setColor(Color.red);
                click.setVicMes("You lose!!!");
            }
            if(click.victory || click.defeat) {
                click.setVicMesY(-50 + (int) (new Date().getTime()-click.endDate.getTime()));
                if(click.getVicMesX() > 70) click.setVicMesY(70);
                g.setFont(new Font("Tahoma",Font.PLAIN, 70));
                g.drawString(click.getVicMes(),click.getVicMesX(),click.getVicMesY());
            }
        }
    }
    // Saves the user's result
    public void saveResult(){
        scoreboard.getData().addUser(popup.getUsername(),totalBoxesRevealed());
        scoreboard.saveAll();
    }
    // Here we check if the player has lost or won
    public void checkVictoryStatus() {
        if(!click.defeat) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9; j++) {
                    if (click.level.revealed[i][j] && click.level.mines[i][j] == 1) {
                        saveResult();
                        click.defeat = true;
                        click.happiness = false;
                        click.endDate = new Date();
                        break;
                    }
                }
            }
        }
        if(totalBoxesRevealed()  >= 144 - totalMines() && !click.victory) {
            saveResult();
            click.victory = true;
            click.endDate = new Date();
        }
    }
    // Total bombs in the game area
    public int totalMines() {
        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if(click.level.mines[i][j] == 1) {
                    total++;
                }
            }
        }
        return total;
    }
    // Total revealed cells
    public int totalBoxesRevealed() {
        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if(click.level.revealed[i][j]) {
                    total++;
                }
            }
        }
        return total;
    }
}