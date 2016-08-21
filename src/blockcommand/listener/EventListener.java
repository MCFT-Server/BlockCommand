package blockcommand.listener;

import java.util.ArrayList;
import java.util.List;

import blockcommand.Main;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

public class EventListener implements Listener {
	private Main plugin;

	public EventListener(Main plugin) {
		this.plugin = plugin;
		
		Server.getInstance().getPluginManager().registerEvents(this, plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			return false;
		}
		switch (args[0].toLowerCase()) {
		case "add" :
			if (args.length < 2) {
				return false;
			}
			addBlockCommand(args[1]);
			sender.sendMessage(TextFormat.DARK_AQUA + args[1] + "을 금지시켰습니다.");
			return true;
		case "del":
		case "delete":
		case "remove":
			if (args.length < 2) {
				return false;
			}
			removeBlockCommand(args[1]);
			sender.sendMessage(TextFormat.DARK_AQUA + args[1] + "을 금지목록에서 뺏습니다.");
			return true;
		case "list" :
			StringBuilder msg = new StringBuilder();
			getBlockCommand().forEach(cmd -> {
				msg.append(cmd + ", ");
			});
			try {
				sender.sendMessage(TextFormat.DARK_AQUA + "목록: " + msg.toString().substring(0, msg.length() - 2));
			} catch (IndexOutOfBoundsException e) {
				sender.sendMessage(TextFormat.RED + "목록없음");
			}
			return true;
		default :
			return false;
		}
	}
	
	public void addBlockCommand(String cmd) {
		if (cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}
		Config db = plugin.getDB().getDB("list");
		List<Object> list = db.get("list", new ArrayList<>());
		list.add(cmd);
		db.set("list", list);
	}
	
	public void removeBlockCommand(String cmd) {
		if (cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}
		Config db = plugin.getDB().getDB("list");
		List<Object> list = db.get("list", new ArrayList<>());
		list.remove(cmd);
		db.set("list", list);
	}
	
	public List<Object> getBlockCommand() {
		return plugin.getDB().getDB("list").get("list", new ArrayList<>());
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (event.getPlayer().hasPermission("blockcommand.usecommand")) {
			return;
		}
		getBlockCommand().forEach(cmd -> {
			if (event.getMessage().split(" ")[0].equals("/" + cmd)) {
				event.setCancelled();
				event.getPlayer().sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
			}
		});
	}
}
