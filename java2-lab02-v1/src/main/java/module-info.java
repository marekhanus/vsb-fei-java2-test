import cz.vsb.fei.java2.lab02.common.ScoreStorageInterface;

module cz.vsb.fei.java2.lab02_module {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
	requires cz.vsb.fei.java2.lab02.common_module;
    opens lab.gui to javafx.fxml;
    exports lab.gui to javafx.fxml,javafx.graphics;
    uses ScoreStorageInterface;
}