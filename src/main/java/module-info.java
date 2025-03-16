module cz.vsb.fei.java2.lab05_module {
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	requires org.apache.logging.log4j;
	requires static lombok;
	requires jakarta.persistence;
	requires com.h2database;
	requires org.hibernate.orm.core;
	
	opens lab.gui to javafx.fxml;
	opens lab.data;

	exports lab.gui to javafx.fxml, javafx.graphics;
}