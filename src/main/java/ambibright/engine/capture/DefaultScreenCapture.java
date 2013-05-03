package ambibright.engine.capture;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ScreenCapture} that uses
 * {@link Robot#createScreenCapture(java.awt.Rectangle)} to capture the screen.
 */
public class DefaultScreenCapture implements ScreenCapture {

	private static class RGBImpl implements Image.RGB {
		private final int red, green, blue;

		private RGBImpl(int color) {
			red = (color & 0x00ff0000) >> 16;
			green = (color & 0x0000ff00) >> 8;
			blue = color & 0x000000ff;
		}

		@Override
		public int red() {
			return red;
		}

		@Override
		public int green() {
			return green;
		}

		@Override
		public int blue() {
			return blue;
		}

        @Override
        public boolean equals( Object o )
        {
            if ( this == o )
            {
                return true;
            }
            if ( o == null || getClass() != o.getClass() )
            {
                return false;
            }

            RGBImpl rgb = (RGBImpl) o;

            if ( blue != rgb.blue )
            {
                return false;
            }
            if ( green != rgb.green )
            {
                return false;
            }
            if ( red != rgb.red )
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = red;
            result = 31 * result + green;
            result = 31 * result + blue;
            return result;
        }
    }

	private static class ImageImpl implements Image {

		private final BufferedImage image;

		private ImageImpl(BufferedImage image) {
			this.image = image;
		}

		@Override
		public RGB getRGB(int x, int y) {
			return new RGBImpl(image.getRGB(x, y));
		}

		@Override
		public BufferedImage getBufferedImage() {
			return image;
		}

        @Override
        public void flush()
        {
            image.flush();
        }

        @Override
        public int getWidth()
        {
            return image.getWidth();
        }

        @Override
        public int getHeight()
        {
            return image.getHeight();
        }
    }

	private static final Logger logger = LoggerFactory.getLogger(DefaultScreenCapture.class);
	private static DefaultScreenCapture instance;

	public static DefaultScreenCapture getInstance() {
		if (null == instance) {
			instance = new DefaultScreenCapture();
		}
		return instance;
	}

	private final Robot robot;

	private DefaultScreenCapture() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			logger.error("Error instantiating a Robot", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Image captureScreen(Rectangle bounds) {
		return new ImageImpl(robot.createScreenCapture(bounds));
	}
}
