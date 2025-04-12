package yeet;
import java.io.Serializable;
// Class for storing user data
public class User implements Serializable {
    private String name;
    private int point;
    // Create a user
    public User(String n, int p){
        this.name = n;
        this.point = p;
        System.out.println("User Created");
    }
    // Getters and setters
    public String getName(){ return this.name;}
    public int getPoint(){ return this.point;}
    public void setName(String s){ this.name = s;}
    public void setPoint(int p){ this.point = p;}
}