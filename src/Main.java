import java.util.concurrent.TimeUnit;

import monitor.capture.CaptureScreen;

public class Main {

	public static void main(String[] args) {
	    CaptureScreen capture = new CaptureScreen();
	    capture.readScreen();
	    while(true) {
	    	try {
				TimeUnit.SECONDS.sleep(10);
				capture.readScreen();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	}


}
