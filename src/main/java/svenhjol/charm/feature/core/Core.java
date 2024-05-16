package svenhjol.charm.feature.core;

import svenhjol.charm.feature.core.common.Advancements;
import svenhjol.charm.feature.core.common.Handlers;
import svenhjol.charm.feature.core.common.Registers;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;
import svenhjol.charm.foundation.helper.ConfigHelper;

import java.util.List;

@Feature(priority = 100)
public final class Core extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    @Configurable(
        name = ConfigHelper.DEBUG_MODE,
        description = """
            Enable debugging mode. Produces more logging output and adds some testing code."""
    )
    public static boolean debug = false;

    @Configurable(
        name = ConfigHelper.COMPAT_MODE,
        description = """
            Enable mixin compatibility mode. All mixins will be disabled, making Charm mods useless.
            Use this if any Charm mods are crashing to determine if there is a mixin collision with another mod."""
    )
    public static boolean compat = false;

    public Core(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public List<? extends SubFeature<? extends svenhjol.charm.foundation.Feature>> subFeatures() {
        return List.of(
            new CustomWood(loader())
        );
    }

    @Override
    public void onEnabled() {
        if (compat) {
            log().warn("""
            
            
            -----------------------------------------------------------------
                           CHARM IS RUNNING IN COMPAT MODE
            -----------------------------------------------------------------
            
            No Charm-related mods will work as expected! This mode is used
            to determine if Charm mods have mixin conflicts with another mod.
            
            Eliminate mods from your mod pack one by one, testing compat mode
            on and off, to find out where a conflict is happening.
            Then come to Charm's discord and talk to us.
            
            """);
        }
    }
}
