package FenixRa.ServerDefence.other;

import FenixRa.ServerDefence.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {
    public static void Check() {
        String currentVersion = Main.getInstance().getDescription().getVersion();
        URL url;
        try {
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource=100438");
        }
        catch (MalformedURLException e) {
            return;
        }
        URLConnection conn;
        try {
            conn = url.openConnection();
        }
        catch (IOException e) {
            return;
        }
        try {
            assert (conn != null);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String newVersion=reader.readLine();
            if (!newVersion.equals(currentVersion)) {
                Main.getInstance().getConsole().sendMessage("§6A new version available! Download §aServerDefence v"
                        +newVersion+" §6at https://www.spigotmc.org/resources/100438/");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
