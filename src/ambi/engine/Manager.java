package ambi.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ambi.ressources.Factory;

/**
 * Created with IntelliJ IDEA.
 * User: Nico
 * Date: 24/03/13
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public class Manager
{
    private ScheduledExecutorService processCheckerService;
    private ScheduledExecutorService aspectRatioService;
    private ScheduledExecutorService colorService;
    private boolean isRunning = false;

    public void start()
    {
        if ( Factory.get().isCheckProcess() )
        {
            processCheckerService = Executors.newScheduledThreadPool( 1 );
            processCheckerService.scheduleAtFixedRate( Factory.get().newProcessCheckerService(), 0, 3,
                TimeUnit.SECONDS );
        }
        else
        {
            startColorsProcessing();
        }
    }

    public void stop()
    {
        if ( null != processCheckerService )
        {
            processCheckerService.shutdown();
            processCheckerService = null;
        }
        stopColorsProcessing();
    }

    public void restart()
    {
        stop();
        start();
    }

    public void startColorsProcessing()
    {
        if ( !isRunning )
        {
            System.out.println( "Starting" );
            aspectRatioService = Executors.newScheduledThreadPool( 1 );
            aspectRatioService.scheduleAtFixedRate( Factory.get().newAspectRatioService(), 0, 500,
                TimeUnit.MILLISECONDS );

            colorService = Executors.newScheduledThreadPool( 1 );
            colorService.scheduleAtFixedRate( Factory.get().newUpdateColorsService(), 50,
                1000 / Factory.get().getFpsWanted(), TimeUnit.MILLISECONDS );

            isRunning = true;
            System.out.println( "Started" );
        }
    }

    public void stopColorsProcessing()
    {
        if ( isRunning )
        {
            System.out.println( "Stopping" );
            aspectRatioService.shutdown();
            aspectRatioService = null;

            colorService.shutdown();
            colorService = null;

            Factory.get().getAmbiFrame().setInfo( "Not running" );
            Factory.get().getArduinoSender().stop();

            isRunning = false;
            System.out.println( "Stopped" );
        }
    }
}
