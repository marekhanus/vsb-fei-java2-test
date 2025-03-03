package lab.data;

import java.util.Random;

public class Score {

	private static final Random RANDOM = new Random();
	
	private String name;
	private int points;
	
	public Score(String name, int points) {
		this.name = name;
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "Score [name=" + name + ", points=" + points + "]";
	}
	
	public static Score generate() {
		return new Score(getRandomNick(), RANDOM.nextInt(50, 300));
	}
	
	public static final String[] nicks = { "CyberSurfer", "PixelPioneer", "SocialSavvy", "DigitalDynamo", "ByteBuddy", "InstaGuru",
			"TikTokTornado", "SnapMaster", "TweetTrendsetter", "ChatChampion", "HashtagHero", "EmojiEnthusiast",
			"StoryStylist", "SelfieStar", "FilterFanatic", "VlogVirtuoso", "Memelord", "InfluencerInsider",
			"StreamSupreme", "GeekyGizmo", "CodeCommander", "JavaJuggernaut", "ByteNinja", "SyntaxSamurai",
			"ClassyCoder", "ObjectOmnipotent", "LoopLegend", "VariableVirtuoso", "DebugDemon", "CompilerCrusader",
			"PixelProdigy", "VirtualVoyager", "AlgorithmAce", "DataDynamo", "ExceptionExpert", "BugBuster",
			"SyntaxSorcerer", "CodeCrusader", "JavaJester", "NerdyNavigator", "CryptoCaptain", "SocialButterfly",
			"AppArchitect", "WebWizard", "FunctionFreak", "PixelArtist", "CyberPhantom", "HackHero", "CacheChampion",
			"ScreenSage", "WebWeaver", "LogicLover", "BitBlazer", "NetworkNomad", "ProtocolPioneer", "BinaryBoss",
			"StackSultan", "SocialScribe", "RenderRuler", "ScriptSorcerer", "HTMLHero", "PixelProwler", "FrameFreak",
			"DataDreamer", "BotBuilder", "ByteBishop", "KeyboardKnight", "DesignDaredevil", "JavaJuggler",
			"SyntaxStrategist", "TechTactician", "ProgramProdigy", "BinaryBard", "PixelPoet", "GigabyteGuru",
			"TechTrekker", "NetworkNinja", "DataDetective", "MatrixMaster", "CodeConductor", "AppAlchemist",
			"ServerSage", "ClusterChampion", "ScriptSensei", "KeyboardKicker", "CacheCrafter", "SocialSpark",
			"BinaryBeast", "CodeConnoisseur", "BitBrain", "VirtualVanguard", "SystemSculptor", "RenderRogue",
			"CryptoConqueror", "MachineMonarch", "PixelPal", "CompilerCaptain", "BitBuilder", "TechTitan",
			"CloudConqueror", "EchoExplorer", "FunctionFanatic", "RobotRanger" };
	
	public static String getRandomNick() {
		return nicks[RANDOM.nextInt(nicks.length)];
	}

}
