package blockcommand;

import blockcommand.database.DataBase;
import blockcommand.listener.EventListener;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase {
	private EventListener listener;
	private DataBase db;
	
	@Override
	public void onEnable() {
		db = new DataBase(this);
		listener = new EventListener(this);
	}
	
	@Override
	public void onDisable() {
		db.save();
	}
	
	public DataBase getDB() {
		return db;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return listener.onCommand(sender, command, label, args);
	}
}
