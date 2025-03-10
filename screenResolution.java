
import java.awt.*;

public class screenResolution {

    private static Dimension size = Toolkit.getDefaultToolkit().getScreenSize(); 
      
    public static int getScreenWidth() 
    {    // width will store the width of the screen 
        return (int)size.getWidth(); 
    }
    
    public static int getScreenHeight()
    {    // height will store the height of the screen 
        return (int)size.getHeight(); 
    }
}
