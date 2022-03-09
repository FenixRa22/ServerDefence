package FenixRa.ServerDefence;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileManager {
    private File cmdFile;
    protected FileConfiguration cmdC;
    private File tabFile;
    protected FileConfiguration tabC;
    private File langFile;
    protected FileConfiguration langFileC;
    private File configFile;
    protected FileConfiguration config;
    private File dataFile;
    protected FileConfiguration data;


    protected void LoadFiles() {
        try {
            configFile = new File(Main.getInstance().getDataFolder(), "config.yml");
            if(configFile.exists() && !configFile.isDirectory()) {
                config = YamlConfiguration.loadConfiguration(configFile);
                if(config.contains("adminsOnlyPerms")&&!config.contains("PermsChecker.permissions")){
                    Main.getInstance().getConsole().sendMessage("§c[ServerDefence] Found outdated adminsOnlyPerms setting in config.yml, changing it to PermsChecker...");
                    config.set("PermsChecker.active",true);
                    config.set("PermsChecker.permissions",config.getList("adminsOnlyPerms"));
                    config.set("adminsOnlyPerms",null);
                    config.save(configFile);
                }
            }else {
                config = loadFromResource("config.yml", configFile);
                config.save(configFile);
            }


            langFile = new File(Main.getInstance().getDataFolder(), "lang.yml");
            if(langFile.exists() && !langFile.isDirectory()) {
                langFileC = YamlConfiguration.loadConfiguration(langFile);
            }else {
                langFileC = loadFromResource("lang.yml", langFile);
                langFileC.save(langFile);
            }

            cmdFile = new File(Main.getInstance().getDataFolder(), "blocked-cmds.yml");
            if(cmdFile.exists() && !cmdFile.isDirectory()) {
                cmdC = YamlConfiguration.loadConfiguration(cmdFile);
            }else {
                cmdC = loadFromResource("blocked-cmds.yml", cmdFile);
                cmdC.save(cmdFile);
            }

            tabFile = new File(Main.getInstance().getDataFolder(), "tab_blocked-cmds.yml");
            if(tabFile.exists() && !tabFile.isDirectory()) {
                tabC = YamlConfiguration.loadConfiguration(tabFile);
            }else {
                tabC = loadFromResource("tab_blocked-cmds.yml", tabFile);
                tabC.save(tabFile);
            }


            dataFile = new File(Main.getInstance().getDataFolder(), "data.yml");
            if(dataFile.exists() && !dataFile.isDirectory()) {
                data = YamlConfiguration.loadConfiguration(dataFile);
            }else {
                dataFile=new File(Main.getInstance().getDataFolder(),"data.yml");
                dataFile.createNewFile();
                data = YamlConfiguration.loadConfiguration(dataFile);
                data.save(dataFile);
            }
        }catch (IOException e){
            e.printStackTrace();

        }
    }

    protected FileConfiguration loadFromResource(String name, File out) {
        try {
            InputStream is = Main.getInstance().getResource(name);
            FileConfiguration f = YamlConfiguration.loadConfiguration(out);
            if (is != null) {
                InputStreamReader isReader = new InputStreamReader(is);
                f.setDefaults(YamlConfiguration.loadConfiguration(isReader));
                f.options().copyDefaults(true);
                f.save(out);
            }
            return f;
        } catch (IOException e) {
            return null;
        }
    }

    protected void reloadFiles(){
        config = YamlConfiguration.loadConfiguration(configFile);
        langFileC = YamlConfiguration.loadConfiguration(langFile);
        cmdC = YamlConfiguration.loadConfiguration(cmdFile);
        tabC = YamlConfiguration.loadConfiguration(tabFile);
        data = YamlConfiguration.loadConfiguration(dataFile);
    }




    protected void setPlayerAdmin(String name, String ip){
        data.set("admins."+name,ip);
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void removePlayerAdmin(String name){
        data.set("admins."+name,null);
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
