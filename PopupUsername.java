package yeet;

import javax.swing.*;
//Felugro ablak osztaly a username beirasahoz
public class PopupUsername {
    JFrame jfr = new JFrame("Hello there!");
    private String username;
    public PopupUsername(){
        username = JOptionPane.showInputDialog(jfr,"Enter Name");
    }

    public String getUsername(){ return this.username; }
}
