package ambibright;

import java.awt.Rectangle;

import ambibright.config.Config;
import ambibright.engine.AspectRatioService;
import ambibright.engine.capture.ImageMock;
import ambibright.engine.capture.ScreenCapture;
import ambibright.ressources.CurrentBounds;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Nicolas Morel
 */
public class AspectRatioServiceTest {

	private Rectangle fullscreenBounds = new Rectangle(0, 0, 1920, 1080);
	private Config config = Config.getInstance();
	private CurrentBounds currentBounds = mock(CurrentBounds.class);
	private ScreenCapture screenCapture = mock(ScreenCapture.class);

	@Before
	public void init() {

		config.setScreenDevice(0);
		config.setCheckProcess(false);
		config.setNbLedLeft(14);
		config.setNbLedTop(24);
		config.setScreenCapture(screenCapture);
	}

	private void resetCurrentBounds() {
		reset(currentBounds);
		when(currentBounds.getScreenDeviceNoLock()).thenReturn(0);
		when(currentBounds.getFullscreenBoundsNoLock()).thenReturn(fullscreenBounds);
	}

	@Test
	public void test() {
		when(currentBounds.getFullscreenBounds()).thenReturn(fullscreenBounds);
		AspectRatioService service = new AspectRatioService(currentBounds, config);

		// Test with a 4:3 image.
		resetCurrentBounds();
		when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format4_3.png"));
		service.run();
		service.run();
		verify(currentBounds, times(1)).updateBounds(new Rectangle(240, 0, 1440, 1080));

		// We call run multiple times to be sure we don't call the update bounds
		// if it doesn't change
		resetCurrentBounds();
		service.run();
		service.run();
		verify(currentBounds, never()).updateBounds(new Rectangle(240, 0, 1440, 1080));

		// Test with a 16:9 image now
		resetCurrentBounds();
		when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format16_9.png"));
		service.run();
		service.run();
		verify(currentBounds, times(1)).updateBounds(new Rectangle(0, 0, 1920, 1080));

		// Test with a 2:40:1 image
		resetCurrentBounds();
		when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format2_40_1.png"));
		service.run();
		service.run();
		verify(currentBounds, times(1)).updateBounds(new Rectangle(0, 140, 1920, 800));

		// Test with a 2:35:1 image
		resetCurrentBounds();
		when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format2_35_1.png"));
		service.run();
		service.run();
		verify(currentBounds, times(1)).updateBounds(new Rectangle(0, 132, 1920, 816));

		// Test with a 1:85:1 image
		resetCurrentBounds();
		when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format1_85_1.png"));
		service.run();
		service.run();
		verify(currentBounds, times(1)).updateBounds(new Rectangle(0, 20, 1920, 1040));

		// Test with a 16:9 image now
		resetCurrentBounds();
		when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format16_9-LeftBlack.png"));
		service.run();
		service.run();
		verify(currentBounds, times(1)).updateBounds(new Rectangle(0, 0, 1920, 1080));

        // Test with a 2:40:1 image with subtitles on top
        resetCurrentBounds();
        when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format2_40_1-SubTop.png"));
        service.run();
        service.run();
        verify(currentBounds, times(1)).updateBounds(new Rectangle(0, 140, 1920, 800));

        // Test with a 2:40:1 image with a different size on top/bottom black strip
        resetCurrentBounds();
        when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format2_40_1-DiffSize.png"));
        service.run();
        service.run();
        verify(currentBounds, times(1)).updateBounds(new Rectangle(0, 141, 1920, 798));
	}

//	@Test
	public void perf() {
		resetCurrentBounds();
		when(currentBounds.getFullscreenBounds()).thenReturn(fullscreenBounds);
		AspectRatioService service = new AspectRatioService(currentBounds, config);
		when(screenCapture.captureScreen(fullscreenBounds, 0)).thenReturn(new ImageMock("Format2_35_1.png"));

		// first one to initialize
		service.run();

		long startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			service.run();
		}
		long finishTime = System.nanoTime();

		long nanotime = (finishTime - startTime) / 1000l;

		System.out.println("Average time aspect ratio calculation : " + (nanotime / 1000l) + " " + "" + "µs");
	}
}
