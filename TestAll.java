package yeet;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import yeet.Click;
import yeet.Level;
import yeet.User;
import yeet.UserData;

public class TestAll {

    private User user;
    private UserData userdata;
    private Level level;
    private Click click;

    @Before
    public void setUp() throws ClassNotFoundException, IOException {
        user = new User(null,0);
        userdata = new UserData();
        level = new Level();
        click = new Click();
    }

    @Test
    public void IdlePlayer(){
        Assert.assertNull(user.getName());
        Assert.assertEquals(0,user.getPoint());
    }
    
    @Test
    public void ChangePlayer(){
    	user.setName("Bandi");
    	user.setPoint(42);
    	Assert.assertEquals("Bandi",user.getName());
    	Assert.assertEquals(42,user.getPoint());
    }
    
    @Test
    public void AddNewUserToUserdata(){
    	userdata.addUser("Bob", 37);
    	Assert.assertEquals(2, userdata.getColumnCount());
    	Assert.assertEquals(37, userdata.getValueAt(0, 1));
    	Assert.assertEquals("Bob", userdata.getValueAt(0, 0));
    	Assert.assertEquals("User", userdata.getColumnName(0));
    	Assert.assertEquals("Points", userdata.getColumnName(1));
    	userdata.setValueAt(48, 0, 1);
    	Assert.assertEquals(48, userdata.getValueAt(0, 1));
    }
    
    @Test
    public void LevelTest() throws IOException{
    	level.saveLevel();
    }
    
    @Test
    public void ClickResetTest(){
    	click.resetAll();
    	Assert.assertTrue(click.happiness);
    	Assert.assertFalse(click.defeat);
    	Assert.assertFalse(click.victory);
    }
}
