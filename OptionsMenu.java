package yeet;
import javax.swing.*;
import java.awt.*;
//A beallitasokat egy kulon ablakban erhetjuk el
public class OptionsMenu extends JFrame{
    JFrame jfrm = new JFrame("Options");
    //Bekapcsolhato egy nehez fokozat
    boolean hardmode;
    //A zene ki es be kapcsolhato
    boolean music;

    OptionsMenu() {
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(250,80);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JCheckBox hardmodeOnOff = new JCheckBox("Hard Mode");
        JCheckBox musicOnOff = new JCheckBox("Mute Music");
        JButton backButton = new JButton("Back");

        hardmodeOnOff.addActionListener(e -> {
            //options ben a run mindig vizsgalja igaz e
            hardmode = hardmodeOnOff.isSelected();
        });
        musicOnOff.addActionListener(e -> music = musicOnOff.isSelected());
        backButton.addActionListener(e -> jfrm.setVisible(false));
        jfrm.add(hardmodeOnOff);
        jfrm.add(musicOnOff);
        jfrm.add(backButton);
        jfrm.getContentPane().setBackground(Color.MAGENTA);
        jfrm.setVisible(false);
    }
    //Lathatova teszi az ablakot
    public void makeVisible(){
        jfrm.setVisible(true);
    }
}
