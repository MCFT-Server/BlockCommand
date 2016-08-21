package blockcommand.database;

import java.io.File;

import blockcommand.Main;
import cn.nukkit.utils.Config;

public class DataBase extends BaseDB<Main> {

	public DataBase(Main plugin) {
		super(plugin);
		initDB("list", new File(plugin.getDataFolder(), "list.json"), Config.JSON);
	}

}
