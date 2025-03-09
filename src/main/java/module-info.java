module cz.vsb.fei.java2.lab03_module {
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	requires org.apache.logging.log4j;

	opens lab.gui to javafx.fxml;
	opens lab.data to javafx.base;

	exports lab.gui to javafx.fxml, javafx.graphics;
}