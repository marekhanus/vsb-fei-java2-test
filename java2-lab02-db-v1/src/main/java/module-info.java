import cz.vsb.fei.java2.lab02.common.ScoreStorageInterface;
import cz.vsb.fei.java2.lab02.db.DbConnector;

module cz.vsb.fei.java2.lab02.db_module {
	requires static lombok;
	requires org.apache.logging.log4j;
	requires transitive cz.vsb.fei.java2.lab02.common_module;
	requires java.sql;
	exports cz.vsb.fei.java2.lab02.db;
	provides ScoreStorageInterface with cz.vsb.fei.java2.lab02.db.DbConnector;
}