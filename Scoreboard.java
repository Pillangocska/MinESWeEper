package yeet;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
// Serializable scoreboard class
public class Scoreboard extends JFrame {
    private UserData data;
    private JTextField newName;
    private JTextField newPoint;
    // Adding components
    private void initComponents(){
        this.setLayout(new BorderLayout());
        JTable jt = new JTable(data);
        jt.setFillsViewportHeight(rootPaneCheckingEnabled);
        jt.setRowSorter(new TableRowSorter<>(data));
        this.add(new JScrollPane(jt),BorderLayout.CENTER);
        // addition
        JPanel adderPanel=new JPanel(new FlowLayout());
        adderPanel.add(new JLabel("Name:"));
        newName = (JTextField)adderPanel.add(new JTextField(15));

        adderPanel.add(new JLabel("Point:"));
        newPoint = (JTextField)adderPanel.add(new JTextField(6));

        JButton backButton = (JButton) adderPanel.add(new JButton("Back"));
        backButton.addActionListener(ae -> {
            this.setVisible(false);
            saveAll();
        });
        JButton adderButton = (JButton) adderPanel.add(new JButton("Add"));
        adderButton.addActionListener(ae -> {
            data.addUser(newName.getText(), Integer.parseInt(newPoint.getText()));
            jt.updateUI();
        });
        this.add(adderPanel,BorderLayout.SOUTH);
    }
    // Creation and initializing listeners
    public Scoreboard() throws IOException, ClassNotFoundException {
        super("Scoreboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Loading data
        data = new UserData();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.txt"));
        data.users = (List<User>) ois.readObject();
        ois.close();
        // Save on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAll();
            }
        });

        setMinimumSize(new Dimension(450,300));
        initComponents();
        this.getContentPane().setBackground(Color.MAGENTA);
        this.setVisible(false);
    }
    // Saves the current state to data.txt
    public void saveAll(){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("data.txt"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            assert oos != null;
            oos.writeObject(data.users);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            oos.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    // Returns the list filled with users
    public UserData getData(){ return this.data;}
}