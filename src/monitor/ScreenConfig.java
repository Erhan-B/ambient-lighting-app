package monitor;

/**
 * 
 */
public class ScreenConfig {
	private int screenWidth;
	private int screenHeight;
	private int numLeds;
	private int sampleSize;
	private int sparsitySize;
	
	public ScreenConfig(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		numLeds = 0;
		sampleSize = 0;
		sparsitySize = 0;
	}
	
	
	/**
	 * @return the screenWidth
	 */
	public int getScreenWidth() {
		return screenWidth;
	}
	/**
	 * @param screenWidth the screenWidth to set
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	/**
	 * @return the screenHeight
	 */
	public int getScreenHeight() {
		return screenHeight;
	}
	/**
	 * @param screenHeight the screenHeight to set
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	/**
	 * @return the numLeds
	 */
	public int getNumLeds() {
		return numLeds;
	}
	/**
	 * @param numLeds the numLeds to set
	 */
	public void setNumLeds(int numLeds) {
		this.numLeds = numLeds;
	}
	/**
	 * @return the sampleSize
	 */
	public int getSampleSize() {
		return sampleSize;
	}
	/**
	 * @param sampleSize the sampleSize to set
	 */
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}


	/**
	 * @return the sparsitySize
	 */
	public int getSparsitySize() {
		return sparsitySize;
	}


	/**
	 * @param sparsitySize the sparsitySize to set
	 */
	public void setSparsitySize(int sparsitySize) {
		this.sparsitySize = sparsitySize;
	}
	
	
		
	
}
