package svenhjol.charm.init;

import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.module.core.Core;

public class CharmLog {
    public static void init() {
        LogHelper.INSTANCE = LogManager.getFormatterLogger("Charm");
        Toml toml = ConfigHelper.readConfig(Charm.MOD_ID);
        Core.debug = ConfigHelper.isDebugMode(toml) || FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
