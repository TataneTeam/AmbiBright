package ambi.ihm;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nico
 * Date: 24/03/13
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public class AmbiFont
{
    public static final String fontName = "Calibri";
    public static final Font font = new Font( fontName, Font.PLAIN, 11 );
    public static final Font fontBold = new Font( fontName, Font.BOLD, 11 );

    public Component setFont( Component component )
    {
        component.setFont( font );
        return component;
    }

    public Component setFontBold( Component component )
    {
        component.setFont( fontBold );
        return component;
    }

    public MenuItem setFont( MenuItem component )
    {
        component.setFont( font );
        return component;
    }
}
