package lab.cleaning;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class CleaningBot {

	public static void main(String[] args) {
		new CleaningBot().run();
	}
	
	/**
	 * Can be modified for testing
	 */
	public void run() {
		//Set proper level for testing
		Configurator.setRootLevel(Level.ALL);
		clean( new Shelf(20),generateMess());
	}

	/**
	 * Create a collection of 10 random boxes
	 */
	public List<Box> generateMess(){
		//TODO:
		return null;
	}
	
	/**
	 * The method inserts the boxes into the shelf using the method insert.
	 * 
	 * Log the information that cleaning has started and
	 * how many boxes are being cleaned, use correct level.
	 * 
	 * If an exception occurs, log the exception to the correct
	 * level. Ensure that the exception message is only assembled
	 * if it will actually print.  
	 * 
	 * @param shelf - shelf into which boxes are inserted
	 * @param boxes - list of boxes
	 */
	public void clean(Shelf shelf, List<Box> boxes){
		//TODO:
	}
}
