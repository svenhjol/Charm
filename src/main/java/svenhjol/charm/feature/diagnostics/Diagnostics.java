package svenhjol.charm.feature.diagnostics;

import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;

public class Diagnostics extends CommonFeature {
    public static final String DEBUG_MODE = "Debug mode";
    public static final String COMPAT_MODE = "Compat mode";

    @Configurable(
        name = DEBUG_MODE,
        description = """
            Enable debugging mode. Produces more logging output and adds some testing code."""
    )
    public static boolean debug = false;

    @Configurable(
        name = COMPAT_MODE,
        description = """
            Enable mixin compatibility mode. All Charmony mixins will be disabled, making Charmony mods useless.
            Use this if any Charmony mods are crashing to determine if there is a mixin collision with another mod."""
    )
    public static boolean compat = false;

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public void onEnabled() {
        if (compat) {
            log.warn(getClass(), """
            
            
            -----------------------------------------------------------------
                           CHARM IS RUNNING IN COMPAT MODE
            -----------------------------------------------------------------
            
            No Charmony-related mods will work as expected! This mode is used
            to determine if Charmony has mixin conflicts with another mod.
            
            Eliminate mods from your mod pack one by one, testing compat mode
            on and off, to find out where a conflict is happening.
            Then come to Charm's discord and talk to us.
            
            """);
        }
    }
}
