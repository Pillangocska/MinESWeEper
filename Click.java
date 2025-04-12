package yeet;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
//Osztaly az eger click re torteno eventekhez
public class Click implements MouseListener {
    //Az orahoz szukseges
    public Date startDate = new Date();
    public Date endDate;
    //Veletlenszeru bombak generalasahoz
    Random rand = new Random();
    //Mousemotionlistener
    public Move move = new Move();
    //A palya
    public Level level = new Level();

    //Egyes cellak kozotti tavolsag
    private int spacing = 1;
    public void setSpacing(int i){ spacing = i;}
    public int getSpacing(){ return spacing; }
    private final int spacingX = 90;
    public int getSpacingX(){ return spacingX; }
    private final int spacingY = (80-60)/2;
    public int getSpacingY(){ return spacingY; }
    private final int minusX = spacingX + 160;
    public int getMinusX(){ return minusX; }
    private final int minusY = spacingY;
    public int getMinusY(){ return minusY; }
    private final int plusX = spacingX + 240;
    public int getPlusX(){ return plusX; }
    private final int plusY = spacingY;
    public int getPlusY(){ return plusY; }

    //A cellak megjeloleshez szukseges zaszlo
    private boolean flagger = false;
    public boolean getFlagger(){ return flagger; }

    //Smiley a palya felett amivel ujraindithato a jatek
    public int smileyX = (1280-70)/2;
    public int getSmileyX(){ return smileyX; }
    public int smileyY = 5;
    public int getSmileyY(){ return smileyY; }
    public int smileyCenterX = smileyX +35;
    public int smileyCenterY = smileyY +35;

    //Valtozok amik a jatek veget jelzik
    public int vicMesX = 750;
    public int getVicMesX(){ return vicMesX; }
    public int vicMesY = -50;
    public int getVicMesY(){ return vicMesY; }
    public void setVicMesY(int n){ vicMesY = n; }
    String vicMes = "vmi";
    public String getVicMes(){ return vicMes; }
    public void setVicMes(String s){ vicMes = s; }
    public boolean happiness = true;
    public boolean victory = false;
    public boolean defeat = false;

    public int inBoxX() {
        for(int i = 0 ; i < 16 ; i++){
            for(int j = 0 ; j < 9 ; j++) {
                if(move.getMx() >= spacing+i*80 && move.getMx() < spacing+i*80+80-2*spacing && move.getMy() >= spacing+j*80+80+26 && move.getMy() < spacing+j*80+26+80+80-2*spacing){
                    return i;
                }
            }
        }
        return -1;
    }
    public int inBoxY() {
        for(int i = 0 ; i < 16 ; i++){
            for(int j = 0 ; j < 9 ; j++) {
                if(move.getMx() >= spacing+i*80 && move.getMx() < spacing+i*80+80-2*spacing && move.getMy() >= spacing+j*80+80+26 && move.getMy() < spacing+j*80+26+80+80-2*spacing){
                    return j;
                }
            }
        }
        return -1;
    }

    //Mi tortenjen ha clickelunk kulonbozo helyeken
    @Override
    public void mouseClicked(MouseEvent e) {
        //A cellak kozotti tavolsag allitasa
        if(move.getMx() >= minusX+20 && move.getMx() < minusX+60 && move.getMy() >= minusY+20 && move.getMy() < minusY+60) {
            spacing--;
            if(spacing < 0){
                spacing = 0;
            }
        } else if( move.getMx() >= plusX+20 && move.getMx() < plusX+60 && move.getMy() >= plusY+20 && move.getMy() < plusY+60) {
            spacing++;
            if(spacing > 30) {
                spacing = 30;
            }
        }

        //Ha jobbklikkelunk bekapcsol a zaszlo
        if(e.getButton() == MouseEvent.BUTTON3){
            flagger = !flagger;
        }

         //Mi tortenjen ha egy cellara clickelunk
        if(inBoxX() != -1 && inBoxY() != -1) {
            System.out.println("The mouse is in the{" + inBoxX()+"," +inBoxY()+"}, Number of neighbours:"+ level.neighbours[inBoxX()][inBoxY()]);
            if (flagger && !level.revealed[inBoxX()][inBoxY()]){
                level.flagged[inBoxX()][inBoxY()] = !level.flagged[inBoxX()][inBoxY()];
            } else {
                if(!level.flagged[inBoxX()][inBoxY()]) {
                    level.revealed[inBoxX()][inBoxY()] = true;
                    //innentol
                    if(level.neighbours[inBoxX()][inBoxY()]==0) {
                        ArrayList<Integer> clear = new ArrayList<>();
                        clear.add(inBoxX()*100+inBoxY());
                        dominoFunction(clear);
                    }
                    //idaig
                }
            }
        } else {
            System.out.println("The mouse is not in any box");
        }
        //Ha a smileyra click akkor a jatek ujraindul
        if (inSmiley()) {
            resetAll();
        }
    }

    public void dominoFunction(ArrayList<Integer> clear){
        //ha mar ures kilepunk
        if(clear.isEmpty()) return;
        //kell 2 lokalis valtozo az eppen vizsgalt cellahoz mivel a rekurzio miatt ki kell torolni a tombbol
        int x = clear.get(0) / 100;
        int y = clear.get(0) % 100;
        clear.remove(0);
        //ha egy cellanak 0 szomszedja van akkor megnezzuk a korulotte levo cellakat
        if(level.neighbours[x][y] == 0) {
            //bal felso
            if (x > 0 && y > 0 && !(level.revealed[x-1][y-1])) {
                level.revealed[x - 1][y - 1] = true;
                if (level.neighbours[x - 1][y - 1] == 0) {
                    clear.add((x - 1) * 100 + (y - 1));
                }
            }
            //bal
            if (y > 0 && !(level.revealed[x][y-1])) {
                level.revealed[x][y - 1] = true;
                if (level.neighbours[x][y - 1] == 0) {
                    clear.add(x * 100 + (y - 1));
                }
            }
            //jobb
            if (y < 9 - 1 && !(level.revealed[x][y+1])) {
                level.revealed[x][y + 1] = true;
                if (level.neighbours[x][y + 1] == 0) {
                    clear.add(x * 100 + (y + 1));
                }
            }
            //bal also
            if (x < 16 - 1 && y > 0 && !(level.revealed[x+1][y-1])) {
                level.revealed[x + 1][y - 1] = true;
                if (level.neighbours[x + 1][y - 1] == 0) {
                    clear.add((x + 1) * 100 + (y - 1));
                }
            }
            //felso
            if (x > 0 && !(level.revealed[x-1][y])) {
                level.revealed[x - 1][y] = true;
                if (level.neighbours[x - 1][y] == 0) {
                    clear.add((x - 1) * 100 + y);
                }
            }
            //also
            if (x < 16 - 1 && !(level.revealed[x+1][y])) {
                level.revealed[x + 1][y] = true;
                if (level.neighbours[x + 1][y] == 0) {
                    clear.add((x + 1) * 100 + y);
                }
            }
            //jobb felso
            if (x > 0 && y < 9 - 1 && !(level.revealed[x-1][y+1])) {
                level.revealed[x - 1][y + 1] = true;
                if (level.neighbours[x - 1][y + 1] == 0) {
                    clear.add((x - 1) * 100 + (y + 1));
                }
            }
            //jobb also
            if (x < 16 - 1 && y < 9 - 1 && !(level.revealed[x+1][y+1])) {
                level.revealed[x + 1][y + 1] = true;
                if (level.neighbours[x + 1][y + 1] == 0) {
                    clear.add((x + 1) * 100 + (y + 1));
                }
            }
        }
        //rekurzivan meghivjuk a fuggvenyt az ujbol felvett cellakra
        dominoFunction(clear);
    }

    //Ujra csinalja az egesz cuccost
    public void resetAll() {
        flagger = false;
        startDate = new Date();
        vicMesY = -50;
        vicMes = "vmi";
        happiness = true;
        victory = false;
        defeat = false;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (rand.nextInt(100) < 20) {
                    level.mines[i][j] = 1;
                } else {
                    level.mines[i][j] = 0;
                }
                level.revealed[i][j] = false;
                level.flagged[i][j] = false;
            }
        }
        int neighs;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!(m == i && n == j)) {
                            if (level.isN(i, j, m, n)) {
                                neighs++;
                            }
                        }
                    }
                    level.neighbours[i][j] = neighs;
                }
            }
        }
    }

    //Megnezi hogy a smileyban vagyunk e
    public boolean inSmiley() {
        int dif = (int) Math.sqrt(Math.abs(move.getMx()-smileyCenterX)*Math.abs(move.getMx()-smileyCenterX)+
                Math.abs(move.getMy()-smileyCenterY)*Math.abs(move.getMy()-smileyCenterY));
        return dif < 35;
    }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}