package yeet;
import java.io.*;
import java.util.Random;

public class Level implements Serializable {
    private static final long serialVersionUID = -6864297078244951686L;
    public int[][] neighbours = new int[16][9];        //Szomszedokat tarolo matrix
    public boolean[][] revealed = new boolean[16][9];  //Ugyanez felfedettre
    public boolean[][] flagged = new boolean[16][9];   //Ugyanez megzaszlozottra
    public int[][] mines = new int[16][9];             //Es vegul a bombat tartalmazo cellakra

    //Szomszedos bombak szamolasa
    public boolean isN( int mX , int mY, int cX , int cY) {
        return mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1;
    }

    public Level(){
        Random rand = new Random();
        for(int i = 0 ; i < 16 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                if(rand.nextInt(100) < 20){
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
            }
        }
        //Megszamoljuk mindegyik cella szomszedsagat
        int neighs;
        for(int i = 0 ; i < 16 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                neighs = 0;
                for(int m = 0 ; m < 16 ; m++) {
                    for(int n = 0 ; n < 9 ; n++) {
                        if(!(m==i && n==j)) {
                            if (isN(i, j, m, n)) {
                                neighs++;
                            }
                        }
                    }
                    neighbours[i][j] = neighs;
                }
            }
        }
    }
    //Elmenti a palya allapotat
    public void saveLevel() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("level.txt"));
        oos.writeObject(this);
        oos.close();
    }
}
