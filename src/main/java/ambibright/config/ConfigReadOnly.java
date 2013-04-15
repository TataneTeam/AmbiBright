package ambibright.config;

import ambibright.engine.squareAnalyser.SquareAnalyser;

/**
 * Interface giving a read-only access to all application properties
 */
public interface ConfigReadOnly
{
    int getLedTotalNumber();

    int getNbLedTop();

    int getNbLedLeft();

    int getScreenDevice();

    String getArduinoSerialPort();

    int getArduinoDataRate();

    String getCheckProcessList();

    int getDeltaRed();

    int getDeltaGreen();

    int getDeltaBlue();

    boolean isCheckProcess();

    int getSquareSize();

    int getAnalysePitch();

    int getFps();

    int getCheckRatioDelay();

    int getCheckProcessDelay();

    String getMonitoringFramePosition();

    SquareAnalyser getSquareAnalyser();

    String getUpdateUrl();

    int getSmoothing();

    boolean isShowFpsFrame();

    boolean isBlackOtherScreens();

    float getGamma();

    float getHue();

    float getSaturation();

    float getBrightness();
}
