package monitor.capture;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import gui.logic.LogicGUI;
import monitor.ScreenConfig;
import java.awt.Rectangle;

public class CaptureScreen {
	private boolean debugMode;
	
	private Robot r;
	private GraphicsDevice selectedScreen;
	private GraphicsDevice[] screenList;
	private ScreenConfig screen;
	private LogicGUI logic;
	private Rectangle bounds;
	private List<int[]> scanZones;
	
	public CaptureScreen(boolean debugMode) {
		this.debugMode = debugMode;
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			screenList = ge.getScreenDevices();
			scanZones = new ArrayList<>();
			
		} catch (SecurityException securityex) {
			System.err.println("Permission not granted");
			securityex.printStackTrace();
		}
	}
	
	public void initRead() {
		bounds = selectedScreen.getDefaultConfiguration().getBounds();
		screen = new ScreenConfig(bounds.width, bounds.height);
		if(debugMode) {
			System.out.printf("Selected monitor %s with width: %d, height: %d%n", 0, screen.getScreenWidth(), screen.getScreenHeight());
		}

		try {
			r = new Robot(selectedScreen);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Samples pixels in a range around the edge of the screen
	 * @param x The x coordinate of the central pixel of the sample
	 * @param y The y coordinate of the central pixel of the sample
	 * @param sparsity The gap between subsequent pixels in the sample
	 * @param sampleSize The length of one side of the square to be sampled
	 * 
	 */
	/*	Samples the areas around the edge of the screen
		Splits the edges of the screen into subsample zones
		Scatters the sample points to create a dispersed sample of each subsample zone
	*/
	public void addSample(int x, int y) {
		int sparsity = screen.getSparsitySize();
		int sampleSize = screen.getSampleSize();
		
		if(sparsity == 0) {
			return;
		}
		
		if(!isInRange(x, y)) {
			System.err.println("Cannot sample pixel outside of monitor range");
			return;
		}
		
		int fx = x + (int)(sampleSize /2);
		int ix = x - (int)(sampleSize /2);
		int fy = y + (int)(sampleSize /2);
		int iy = y - (int)(sampleSize /2);
		
		int maxX = ((fx-ix)/sparsity) + 1;
		int maxY = ((fy-iy)/sparsity) + 1;
		
		int[] temp = new int[maxX * maxY];
		int count = 0;
		
		//Cook temporary ArrayList
		for(int i = ix; i < fx +1; i+= sparsity) {
			for(int j = iy; j < fy +1; j += sparsity) {
				
				if(isInRange(i, j)) {
					temp[count++] = xyToIndex(i,j);
					System.out.printf("Added entry to scanmap: (%d,%d) with index %d\n",i,j,xyToIndex(i,j));
				}
				else {
					System.out.printf("(%d,%d) is out of range\n",i,j);
				}
			}
		}
		
		int[] pixelList = Arrays.copyOf(temp, count);	
		scanZones.add(pixelList);
	}
	
	/**
	 * 
	 * @return avgColor the average colour of the scanned zone
	 */
	//TODO reusable bounds/image
	//Rectangle bounds = selectedScreen.getDefaultConfiguration().getBounds();
	//BufferedImage image = r.createScreenCapture(bounds);
	public int averageZone(BufferedImage image, int zoneIndex) {
		long totalR = 0;
		long totalG = 0;
		long totalB = 0;
		
		int count = 0;
		
		int width = screen.getScreenWidth();
		
		for(int scanIndex : scanZones.get(zoneIndex)) {
			int x = scanIndex % width;
			int y = (int) (scanIndex/width);
			
			int rgb = image.getRGB(x, y);
			totalR += (rgb >> 16) & 0xFF;
			totalG += (rgb >> 8) & 0xFF;
			totalB += rgb & 0xFF;
			
			count++;
		}
		
		int avgR = (int)(totalR/count);
		int avgG = (int)(totalG/count);
		int avgB = (int)(totalB/count);
		int avgColor = (avgR << 16) | (avgG << 8) | (avgB);
		
//		if(debugMode) {
//			System.out.printf("avgR:%d avgG:%d avgB:%d\n", avgR, avgG, avgB);
//		}
		
		return avgColor;
	}
	
	public void CaptureThread() {
		boolean isRunning = true;
		
		if(logic == null) {
			System.err.println("CaptureScreen:LogicGUI is null");
		}
		System.out.println("New capture thread started");
		
		Thread t = new Thread(()-> {
			while(isRunning) {
				BufferedImage screenCap = r.createScreenCapture(bounds);
				for(int i = 0; i < scanZones.size(); i++) {
					logic.updateLed(i, averageZone(screenCap, i));
				}	
				try {
					//16 = 60fps
					//33 = 30fps
					Thread.sleep(33);
				}catch(InterruptedException e) {}
			}
		});
		t.start();
	}
	
	/*
	 * This function takes in an xy coordinate and maps it to a single index
	 * The formula for this is f(x,y) = y * width + x
	 * This formula works as the y*width is encoded in the left most part of the index
	 * and the x is encoded in the right portion
	 * This formula follows from the mapping for memory and is suitable as it can be inverted to obtain x and y
	 */
	private int xyToIndex(int x, int y) {
		return (y * screen.getScreenWidth() + x);
	}

	private boolean isInRange(int x, int y) {
		return (x >=0 && y >=0 && x <=screen.getScreenWidth() && y <= screen.getScreenHeight());
	}
	
	public GraphicsDevice[] getScreens() {
		return screenList;
	}
	
	public void setScreen(GraphicsDevice selectedScreen) {
		this.selectedScreen = selectedScreen;
	}
	
	public void setLogic(LogicGUI logic) {
		this.logic = logic;
	}
	
	public void setSample(int sampleSize) {
		if(sampleSize <= 0) {
			System.err.println("Sample size cannot be <= 0");
			return;
		}
		screen.setSampleSize(sampleSize);
	}
	
	public void setSparsity(int sparsitySize) {
		if(sparsitySize <= 0) {
			System.err.println("Sparsity cannot be <=0");
			return;
		}
		screen.setSparsitySize(sparsitySize);
	}
	
	public boolean sampleSparsityCheck() {
		if(screen.getSparsitySize() >= -1 && screen.getSampleSize() >= -1) {
			if(screen.getSparsitySize() >= screen.getSampleSize()) {
				System.err.println("Sparsity greater than sample size");
				return false;
			}
			return true;
		}
		return false;
	}
	
	public ScreenConfig getScreenConfig() {
		return screen;
	}
	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//Saves image of edge of screen
	@SuppressWarnings("unused")
	private void drawEdge(int capStrip) {
		Rectangle bounds = selectedScreen.getDefaultConfiguration().getBounds();
		
		BufferedImage topStrip = r.createScreenCapture(new Rectangle(bounds.x, bounds.y, bounds.width, capStrip));
		BufferedImage bottomStrip = r.createScreenCapture(new Rectangle(bounds.x, bounds.y + bounds.height - capStrip, bounds.width, capStrip));
		BufferedImage leftStrip = r.createScreenCapture(new Rectangle(bounds.x, bounds.y + capStrip, capStrip, bounds.height - 2*capStrip));
		BufferedImage rightStrip = r.createScreenCapture(new Rectangle(bounds.x + bounds.width - capStrip, bounds.y + capStrip, capStrip, bounds.height - 2*capStrip));
		
		BufferedImage image = new BufferedImage(screen.getScreenWidth(), screen.getScreenHeight(),BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.BLACK);
		
		g2d.fillRect(0, 0, screen.getScreenWidth(), screen.getScreenHeight());
		
		g2d.drawImage(topStrip, 0, 0, null);
		g2d.drawImage(bottomStrip, 0, screen.getScreenHeight()-capStrip, null);
		g2d.drawImage(leftStrip, 0, capStrip, null);
		g2d.drawImage(rightStrip, screen.getScreenWidth()-capStrip,capStrip, null);
		
		g2d.dispose();
		writeImage(image);
	}

	//Writes image to file
	@SuppressWarnings("unused")
	private void writeImage(BufferedImage image) {
		File outputFile = new File("images/" + System.currentTimeMillis() + ".png");
		try {
			ImageIO.write(image, "png", outputFile);
			System.out.println("Successfully wrote new image file");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
