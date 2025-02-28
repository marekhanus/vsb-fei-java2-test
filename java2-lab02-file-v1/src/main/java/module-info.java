import cz.vsb.fei.java2.lab02.common.ScoreStorageInterface;

module cz.vsb.fei.file_module {
	requires static lombok;
	requires org.apache.logging.log4j;
    requires transitive cz.vsb.fei.java2.lab02.common_module;
	exports cz.vsb.fei.file;
	provides ScoreStorageInterface with cz.vsb.fei.file.FileStorage;
}