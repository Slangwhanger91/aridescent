package soundEngine;

import java.io.FileNotFoundException;

public class OpenALEngineTester {
	public static void main(String[] args) {
		OpenALEngine.init();
		try {
			OpenALEngine.play("AAA1.wav");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		sleep(500);

		OpenALEngine.setVolume(0.4f);
		
		try {
			OpenALEngine.play("AAA3.wav");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		sleep(500);
		
		OpenALEngine.setPitch(0.5f);
		
		sleep(2000);

		OpenALEngine.close();
	}
	
	private static void sleep(int amount){
		try {
			Thread.sleep(amount);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
