package svenhjol.charm.init;

import org.apache.logging.log4j.LogManager;
import svenhjol.charm.helper.LogHelper;

public class CharmLog {
    public static void init() {
        LogHelper.INSTANCE = LogManager.getFormatterLogger("Charm");
    }
}
