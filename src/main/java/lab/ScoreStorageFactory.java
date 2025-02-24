package lab;

public class ScoreStorageFactory {

	private static DbConnector instance;
	
	public static DbConnector getInstance() {
		if(instance	== null) {
			instance = new DbConnector();
		}
		return instance;
	}
}
