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
		int sampleSize,
		sampleType type
		) {
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
}
