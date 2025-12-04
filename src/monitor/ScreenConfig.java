package monitor;

/**
 * 
 */
public record ScreenConfig(
		int screenWidth,
		int screenHeight,
		int ledsTop,
		int ledsBottom,
		int ledsLeft,
		int ledsRight,
		int numLeds,
		int sampleSize
		) {
}
