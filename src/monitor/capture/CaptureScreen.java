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

import javax.imageio.ImageIO;

import monitor.ScreenConfig;
import monitor.sampleType;

import java.awt.Rectangle;

public class CaptureScreen {
	private Robot r;
	private GraphicsDevice gd;
	private ScreenConfig screen;
//	private int screenWidth;
//	private int screenHeight;
	
	//The strip around the edge of the screen to be captured (pixels)
	private int capStrip = 20;
	
	
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
//			screenWidth = bounds.width;
//			screenHeight = bounds.height;
//			System.out.printf("Selected monitor %s with width: %d, height: %d%n", 0, screenWidth, screenHeight);
			System.out.printf("Selected monitor %s with width: %d, height: %d%n", 0, screen.getScreenWidth(), screen.getScreenHeight());
			r = new Robot(gd);
			
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
		//Section for reading entire edge of screen
		Rectangle bounds = gd.getDefaultConfiguration().getBounds();
		
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
	
	private void sparseSampling() {
		
	}
	
	//For debug
	//Saves image to /img
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
