package FenixRa.ServerDefence;

import org.bukkit.configuration.file.FileConfiguration;

public class ChannelControl {
    public static boolean presentprotocolsupport = false;
    public static boolean presentviaversion = false;

    public static void RegisterChannels(ByteMessageListener bml) {
        FileConfiguration config = Main.getInstance().fileM.config;
        if (Main.getInstance().getServer().getPluginManager().isPluginEnabled("ProtocolSupport")) {
            presentprotocolsupport = true;
            Main.getInstance().getConsole().sendMessage("§7ProtocolSupport detected. Enabling compatibility.");
        }
        if (Main.getInstance().getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
            presentviaversion = true;
            Main.getInstance().getConsole().sendMessage("§7ViaVersion detected. Enabling compatibility.");
        }
        if (presentviaversion && presentprotocolsupport) {
            Main.getInstance().getConsole().sendMessage("ViaVersion and ProtocolSupport found! Unfortunately they can't work together. Disabling modsBlocker...");
            Main.getInstance().modBlockerActive=false;
            return;
        }
        ChannelControl.Register(bml, "MC|Brand");
        ChannelControl.Register(bml, "minecraft:brand");
        if (config.getBoolean("modsBlocker.5zig.block")) {
            ChannelControl.Register(bml, "5zig_Set");
            ChannelControl.Register(bml, "the5zigmod:5zig_set");
        }
        if (config.getBoolean("modsBlocker.betterSprinting.block")) {
            ChannelControl.Register(bml, "BSM");
            ChannelControl.Register(bml, "BSprint");
            ChannelControl.Register(bml, "bsm:settings");
        }
        if (config.getBoolean("modsBlocker.schematica.block")) {
            ChannelControl.Register(bml, "schematica");
        }
        if (config.getBoolean("modsBlocker.worldDownloader.block")) {
            ChannelControl.Register(bml, "WDL|INIT");
            ChannelControl.Register(bml, "WDL|CONTROL");
            ChannelControl.Register(bml, "WDL|REQUEST");
            ChannelControl.Register(bml, "WorldDownloader");
            ChannelControl.Register(bml, "wdl");
        }
        if (config.getBoolean("modsBlocker.vape.block")) {
            ChannelControl.Register(bml, "LOLIMAHCKER");
        }
        if (config.getBoolean("modsBlocker.forge.block")) {
            ChannelControl.Register(bml, "FML|HS");
            ChannelControl.Register(bml, "FML|MP");
            ChannelControl.Register(bml, "FML");
        }
        if (config.getBoolean("modsBlocker.hyperium.block")) {
            ChannelControl.Register(bml, "hyperium");
        }
        if (config.getBoolean("modsBlocker.pixelClient.block")) {
            ChannelControl.Register(bml, "MC|Pixel");
        }
        if (config.getBoolean("modsBlocker.winterWare.block")) {
            ChannelControl.Register(bml, "LC|Brand");
        }
        if (config.getBoolean("modsBlocker.lunarClient.block")) {
            ChannelControl.Register(bml, "Lunar-Client");
            ChannelControl.Register(bml, "LunarClient");
        }
        if (config.getBoolean("modsBlocker.EMC.block")) {
            ChannelControl.Register(bml, "Subsystem");
        }
        Main.getInstance().getConsole().sendMessage("§7Channels listener loaded successfully.");
    }

    protected static String translateChannel(String Channel) {
        if (Main.getInstance().getServerVersion()>12) {
            if (presentprotocolsupport) {
                String format1 = Channel.toLowerCase().replace("|", "");
                return "l:" + format1;
            }
            if (presentviaversion) {
                String format1 = Channel.toLowerCase();
                return "legacy:" + format1;
            }
            String format1 = Channel.replace("|", ":").toLowerCase();
            if (!format1.contains(":")) {
                return "l:" + format1;
            }
            return format1;
        }
        return Channel;
    }

    private static void Register(ByteMessageListener bml, String Channel) {
        Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), ChannelControl.translateChannel(Channel),bml);
        Main.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(Main.getInstance(), ChannelControl.translateChannel(Channel));
    }
}
