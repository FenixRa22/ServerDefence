package FenixRa.ServerDefence;


import FenixRa.ServerDefence.other.Metrics;
import FenixRa.ServerDefence.other.UpdateChecker;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {
    private static Main plugin;
    protected FileManager fileM;
    protected ProtocolManager protocolManager;
    protected  boolean modBlockerActive;

    @Override
    public void onEnable() {
        getConsole().sendMessage("§fInitializing §eServerDefence §fversion §e"+ this.getDescription().getVersion());
        plugin = this;
        fileM = new FileManager();
        fileM.LoadFiles();
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), this);
        new PermsCheckerTask(plugin).runTaskTimerAsynchronously(plugin, 0, 10);

        new TabComplete();


        plugin.getCommand("serverdefence").setExecutor(new DefenceCommand());

        if(getServerVersion()>12) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.updateCommands();
            }
        }
        ByteMessageListener bml = new ByteMessageListener();
        ChannelControl.RegisterChannels(bml);

        if ((!fileM.config.contains("metrics")||fileM.config.getBoolean("metrics")) && (new Metrics(this, 14524)).isEnabled()) {
            getConsole().sendMessage("§7Metrics loaded successfully");
        }

        getConsole().sendMessage("§eServerDefence §fhas been enabled");
        doAsync(UpdateChecker::Check);
    }

    @Override
    public void onDisable() {
        protocolManager.removePacketListeners(this);

        getConsole().sendMessage("§eServerDefence§f has been disabled!");
    }
    public int getServerVersion(){
        return Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].split("_")[1]);
    }

    public static void doSync(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    public static void doAsync(Runnable runnable) { plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable); }

    public static Main getInstance(){
        return plugin;
    }
    public CommandSender getConsole(){
        return Bukkit.getConsoleSender();
    }

}
