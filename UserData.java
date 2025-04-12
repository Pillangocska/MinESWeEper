package yeet;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

// Class that stores user data
public class UserData extends AbstractTableModel {
    List<User> users = new ArrayList<>();
    // User data is stored in a list

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    // Returns the given object
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        if(columnIndex == 0){ return user.getName();}
        else return user.getPoint();
    }

    // Add listener
    @Override
    public void addTableModelListener(TableModelListener tl){
        super.addTableModelListener(tl);
    }

    // Not required but included anyway
    @Override
    public String getColumnName(int i){
        if(i == 0){ return "User";}
        else return "Points";
    }

    // Returns the class stored in the given column, also not required but good to have
    @Override
    public Class<?> getColumnClass(int i){
        if(i==0){ return String.class;}
        else return Integer.class;
    }

    // For modifying data
    @Override
    public void setValueAt(Object o, int i, int j){
        User u = users.get(i);
        if(j==0){
            u.setName((String) o);
        } else {
            u.setPoint((Integer)o);
        }
        users.set(i,u);
        this.fireTableRowsUpdated(i,i);
    }

    // Add new user
    public void addUser(String name, int point){
        users.add(new User(name,point));
        this.fireTableDataChanged();
    }
}