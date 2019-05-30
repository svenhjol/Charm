package svenhjol.meson.helper;

import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;
import svenhjol.meson.Meson;

import java.io.File;

public class FileHelper
{
    public static void backupConfigFile(File configFile)
    {
        Configuration testConfig = new Configuration(configFile);
        testConfig.load();

        String version = testConfig.getLoadedConfigVersion();

        if (version == null || version.isEmpty()) {
            // old version, back it up
            String backupName = configFile.getAbsolutePath().replaceFirst(".cfg", ".backup.cfg");
            File backupFile = new File(backupName);

            try {
                FileUtils.copyFile(configFile, backupFile);
                Meson.log("Backed up config");
            } catch (Exception e) {
                Meson.log("Could not backup config file - will be overwritten");
            }
            FileUtils.deleteQuietly(configFile);
        } else {
            Meson.log("Config version " + version + ", no backup required");
        }
    }
}
