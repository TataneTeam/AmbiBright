import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessList
{

    public static void main( String[] args )
    {
        try
        {
            String line;
            Process p = Runtime.getRuntime().exec
                (System.getenv("windir") +"\\system32\\"+"tasklist.exe /FO CSV /NH");
            BufferedReader input =
                new BufferedReader( new InputStreamReader( p.getInputStream() ) );
            while ( (line = input.readLine()) != null )
            {
                System.out.println( line.substring( 1, line.indexOf( "\"," ) ) ); //<-- Parse data here.
            }
            input.close();
        } catch ( Exception err )
        {
            err.printStackTrace();
        }
    }
}
