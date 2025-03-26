package lab;

import lab.cleaning.CleaningBot;
import lab.jpa.ArchitectManager;
import lab.travel.TravelAgency;

/**
 * Class <b>App</b> is an entry point of the program 
 * 
 */
public class App {

	public static void main(String[] args) {
		System.out.println("Application lauched");

		DatabaseControl.startDBWebServer();
		
		new CleaningBot().run();
		new TravelAgency().run();
		new ArchitectManager().run();

		DatabaseControl.waitForKeyAndStopDBWebServer();
	}
	
}