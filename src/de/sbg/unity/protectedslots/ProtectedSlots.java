package de.sbg.unity.protectedslots;

import de.sbg.unity.configmanager.ConfigData;
import de.sbg.unity.configmanager.ConfigManager;
import java.io.IOException;
import java.net.URISyntaxException;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;

public class ProtectedSlots extends Plugin {

    public int MaxSlots;
    
    public Config Config;
    private psConsole Console;

    @Override
    public void onEnable() {
        this.Console = new psConsole(this);

        Console.sendInfo("Enabled");
        MaxSlots = Server.getMaxPlayerCount();
        Console.sendInfo("Config", "Load Config...");
        Config = new Config(this);
        try {
            Config.iniConfig();
        } catch (IOException ex) {
            Console.sendErr("Config", "Can not load Config!");
        }
        Console.sendInfo("Config", "Load Config...Done!");
        Console.sendInfo("Config", "Maximal Slots: " + MaxSlots);
        Console.sendInfo("Config", "Protected Slots: " + Config.ProtectedSlotsAmonth);

        try {
            Update up = new Update(this, "http://gs.sandboxgamer.de/downloads/Plugins/risingworld/unity/ProtectedSlots/version.txt");
        } catch (URISyntaxException | IOException ex) {
            Console.sendWarning("Update", "Can not find the version-file!");
        }

        registerEventListener(new EventListener(this));

    }

    @Override
    public void onDisable() {
        Console.sendInfo("Disabled");
    }

    public class Config {

        private final String PluginPath;
        
        private final String PluginName;
        private ConfigData ConfigData;

        public int ProtectedSlotsAmonth;
        public String NoAdminPlayers;
        
        private final ConfigManager Manager;
        
        public Config(ProtectedSlots ps) {
            this.PluginPath = ps.getPath();
            PluginName = ps.getDescription("name");
            System.out.println("Plugin: " + ps.getPluginByName("ConfigManager"));
            Manager = (ConfigManager) ps.getPluginByName("ConfigManager");
            Console.sendInfo("Connect to ConfigManager: " + Manager);

        }

        public void iniConfig() throws IOException {
            if (Manager != null) {
                ConfigData = Manager.newConfig(PluginName, PluginPath);

                ConfigData.addCommend("# Config of ProtectedSlots");
                ConfigData.addEmptyLine();
                ConfigData.addCommend("# Protected Slots Amonth");
                ConfigData.addSetting("ProtectedSlotsAmonth", 1);
                ConfigData.addEmptyLine();
                ConfigData.addCommend("# Player UIDs without admin permissions (UID1 UID2 UID3 etc.)");
                ConfigData.addSetting("NoAdminPlayers", "");
                ConfigData.CreateConfig();

                ProtectedSlotsAmonth = Integer.parseInt(ConfigData.getSetting("ProtectedSlotsAmonth"));
                NoAdminPlayers = ConfigData.getSetting("NoAdminPlayers");
            }
        }

    }

}
