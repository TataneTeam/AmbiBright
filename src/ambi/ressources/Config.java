package ambi.ressources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config
{

    public enum Parameters
    {
        CONFIG_LED_NB_TOP, CONFIG_LED_NB_LEFT, CONFIG_SCREEN_DEVICE, CONFIG_ARDUINO_PORT, CONFIG_ARDUINO_DATA_RATE,
        CONFIG_PROCESS_LIST, CONFIG_RGB_R, CONFIG_RGB_G, CONFIG_RGB_B, CONFIG_CHECK_PROCESS, CONFIG_SQUARE_SIZE,
        CONFIG_ANALYSE_PITCH, CONFIG_FPS
    }

    private Properties properties;
    private String path;

    public Config( String path )
    {
        this.path = path;
        properties = new Properties();
    }

    public void load()
    {
        FileInputStream stream = null;
        try
        {
            stream = new FileInputStream( path );
            properties.load( stream );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            if ( null != stream )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void save()
    {
        FileOutputStream stream = null;
        try
        {
            stream = new FileOutputStream( path );
            properties.store( stream, null );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            if ( null != stream )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void put( Parameters parameter, String value )
    {
        properties.put( parameter.toString(), value );
    }

    public String get( Parameters parameter )
    {
        return properties.getProperty( parameter.toString() );
    }

}
