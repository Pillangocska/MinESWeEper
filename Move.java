package yeet;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
//Osztaly az eger pillanatnyi allapotanak lekerdezesehez
public class Move implements MouseMotionListener {
    private int mx;
    public int getMx(){ return mx; }
    private int my;
    public int getMy(){ return my; }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
    }
}