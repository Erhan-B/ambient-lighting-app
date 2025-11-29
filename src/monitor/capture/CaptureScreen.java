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
import java.util.List;

import javax.imageio.ImageIO;

import monitor.Pixel;
import monitor.ScreenConfig;
import monitor.sampleType;

import java.awt.Rectangle;

public class CaptureScreen {
	private Robot r;
	private GraphicsDevice gd;
	private ScreenConfig screen;
	private List<Pixel> scanList;
	
	
	public CaptureScreen() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] screens = ge.getScreenDevices();
			
			for(int i = 0; i < screens.length; i++) {
			System.out.printf("Screen %d: display width: %d, display height: %d%n", i, screens[i].getDisplayMode().getWidth(), screens[i].getDisplayMode().getHeight());
			}
			
			//Set as primary
			gd = screens[0];
			Rectangle bounds = gd.getDefaultConfiguration().getBounds();
			screen = new ScreenConfig(bounds.width, bounds.height, 33, 33, 16, 16, 98, 5, sampleType.EDGE_ONLY);
			System.out.printf("Selected monitor %s with width: %d, height: %d%n", 0, screen.screenWidth(), screen.screenHeight());
			r = new Robot(gd);
			scanList = new ArrayList<>();
			
			edgeSample(40, 40, 5, 15);
			
		} catch (AWTException awtex) {
			System.err.println("Low level input control not allowed");
			awtex.printStackTrace();
		} catch (SecurityException securityex) {
			System.err.println("Permission not granted");
			securityex.printStackTrace();
		}
		
		if(r == null) {
			System.err.println("Robot not initialized successfully");
		}
	}
	
	public void readScreen() {
		readPixels();
		
		//Section for reading pixel at center of screen
//		Rectangle bounds = gd.getDefaultConfiguration().getBounds();
//		int relX = bounds.width/2;
//		int relY = bounds.height/2;
//		BufferedImage image = r.createScreenCapture(bounds);
//		//DEBUG ~~~~~~~~~~~~
//		//writeImage(image);
//		//System.out.printf("Capturing pixel at (%d,%d)%n", relX, relY);
//
//		int rgb = image.getRGB(relX, relY);
//		int red = (rgb >> 16) & 0xFF;
//		int green = (rgb >> 8) & 0xFF;
//		int blue = rgb & 0xFF;
//		System.out.println("Red: " + red + ", Green " + green + ", Blue: " + blue);
	}
	
	//Samples the areas around the edge of the screen
	//Splits the edges of the screen into subsample zones
	//Scatters the sample points to create a dispersed sample of each subsample zone
	/**
	 * Samples pixels in a range around the edge of the screen
	 * @param x The x coordinate of the central pixel of the sample
	 * @param y The y coordinate of the central pixel of the sample
	 * @param sparcity The gap between subsequent pixels in the sample
	 * @param sampleSize The length of one side of the square to be sampled
	 */
	private void edgeSample(int x, int y, int sparcity, int sampleSize) {
//		int gapSizeTop = (screen.screenWidth() - (screen.sampleSize() * screen.ledsTop())) / (screen.ledsTop() + 1);
//		int gapSizeBottom = (screen.screenWidth() - (screen.sampleSize() * screen.ledsBottom())) / (screen.ledsBottom() + 1);
//		int gapSizeLeft = (screen.screenHeight() - (screen.sampleSize() * screen.ledsLeft())) / (screen.ledsLeft() + 1);
//		int gapSizeRight = (screen.screenHeight() - (screen.sampleSize() * screen.ledsRight())) / (screen.ledsRight() + 1);
//		
//		//Debug
//		System.out.println(gapSizeTop);
//		System.out.println(gapSizeBottom);
//		System.out.println(gapSizeLeft);
//		System.out.println(gapSizeRight);
		
		if(sparcity >= sampleSize) {
			System.err.println("Sparcity greater than sample size");
			return;
		}
		if(sampleSize <= 0) {
			System.err.println("Sample size cannot be <= 0");
			return;
		}
		if(sparcity <= 0) {
			System.err.println("Sparcity cannot be <=0");
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
		
//		System.out.printf("fx:%d\nix:%d\nfy:%d\niy:%d\n", fx,ix,fy,iy);
		
		int totalScan = 0;
		
		for(int i = ix; i < fx +1; i+= sparcity) {
			for(int j = iy; j < fy +1; j += sparcity) {
				
				if(isInRange(i, j)) {
					
					scanList.add(new Pixel(i, j));
//					System.out.printf("Added entry to scanmap: (%d,%d)\n",i,j);
					totalScan++;
				}
				else {
//					System.out.printf("(%d,%d) is out of range\n",i,j);
				}
			}
		}
//		System.out.printf("Total pixels: %d\n", totalScan);
		
	}
	
	/**
	 * 
	 * @return avgColor the average colour of the scanned zone
	 */
	public int readPixels() {
		Rectangle bounds = gd.getDefaultConfiguration().getBounds();
		BufferedImage image = r.createScreenCapture(bounds);
		
//		BufferedImage debugImage = new BufferedImage(screen.screenWidth(), screen.screenHeight(),BufferedImage.TYPE_INT_RGB);
		long totalR = 0;
		long totalG = 0;
		long totalB = 0;
		
		for(Pixel p: scanList) {
			int rgb = image.getRGB(p.x(), p.y());
			
			totalR += (rgb >> 16) & 0xFF;
			totalG += (rgb >> 8) & 0xFF;
			totalB += rgb & 0xFF;
			
			int red = (rgb >> 16) & 0xFF;
			int green = (rgb >> 8) & 0xFF;
			int blue = rgb & 0xFF;
			
			
//			System.out.printf("X:%d Y:%d R:%d G:%d B:%d\n\n", p.x(), p.y(), red, green, blue);
//			debugImage.setRGB(p.x(), p.y(), rgb);
			
		}
		
		int count = scanList.size();
		int avgR = (int)(totalR / count);
		int avgG = (int)(totalG / count);
		int avgB = (int)(totalB / count);
		
//		System.out.printf("avgR:%d avgG:%d avgB:%d\n", avgR, avgG, avgB);
		
		int avgColor = (avgR << 16) | (avgG << 8) | avgB;
		
//		writeImage(debugImage);
		
		return avgColor;
	}
	
	//Same sampling as edgeSample but factors in pixels in the center of the screen
	private void edgeCenterSample() {
		
	}
	
	private void gridSample() {
		
	}
	
	private void customSample() {
		
	}
	
	private boolean isInRange(int x, int y) {
		return (x >=0 && y >=0 && x <=screen.screenWidth() && y <= screen.screenHeight());
	}
	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//Saves image of edge of screen
	@SuppressWarnings("unused")
	private void drawEdge(int capStrip) {
		Rectangle bounds = gd.getDefaultConfiguration().getBounds();
		
		BufferedImage topStrip = r.createScreenCapture(new Rectangle(bounds.x, bounds.y, bounds.width, capStrip));
		BufferedImage bottomStrip = r.createScreenCapture(new Rectangle(bounds.x, bounds.y + bounds.height - capStrip, bounds.width, capStrip));
		BufferedImage leftStrip = r.createScreenCapture(new Rectangle(bounds.x, bounds.y + capStrip, capStrip, bounds.height - 2*capStrip));
		BufferedImage rightStrip = r.createScreenCapture(new Rectangle(bounds.x + bounds.width - capStrip, bounds.y + capStrip, capStrip, bounds.height - 2*capStrip));
		
		BufferedImage image = new BufferedImage(screen.screenWidth(), screen.screenHeight(),BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.BLACK);
		
		g2d.fillRect(0, 0, screen.screenWidth(), screen.screenHeight());
		
		g2d.drawImage(topStrip, 0, 0, null);
		g2d.drawImage(bottomStrip, 0, screen.screenHeight()-capStrip, null);
		g2d.drawImage(leftStrip, 0, capStrip, null);
		g2d.drawImage(rightStrip, screen.screenWidth()-capStrip,capStrip, null);
		
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
