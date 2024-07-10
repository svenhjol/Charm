package svenhjol.charm.feature.core;

import svenhjol.charm.feature.core.common.Advancements;
import svenhjol.charm.feature.core.common.Handlers;
import svenhjol.charm.feature.core.common.Registers;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;
import svenhjol.charm.feature.core.custom_pistons.CustomPistons;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipes;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.charmony.helper.ConfigHelper;

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
    private static boolean debug = false;

    @Configurable(
        name = ConfigHelper.MIXIN_DISABLE_MODE,
        description = """
            Enable mixin disable mode. All mixins will be disabled, making Charm mods useless.
            Use this if any Charm mods are crashing to determine if there is a mixin collision with another mod."""
    )
    private static boolean mixinDisable = false;

    @Configurable(
        name = "customOnTakeBehavior",
        description = """
            Enable custom onTake behavior for anvils.
            This changes the vanilla anvil behavior to avoid destroying entire stacks.
            There is a small chance that a mod depends on the default behavior, in which case you should
            disable this config option and change the stack size of ItemStacking "Enchanted book" to 1.
            """
    )
    private static boolean customOnTakeBehavior = true;

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

    public boolean debug() {
        return debug;
    }

    public boolean mixinDisable() {
        return mixinDisable;
    }

    public boolean customOnTakeBehavior() {
        return customOnTakeBehavior;
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.charmony.Feature>> children() {
        return List.of(
            new CustomAdvancements(loader()),
            new CustomPistons(loader()),
            new CustomRecipes(loader()),
            new CustomWood(loader())
        );
    }

    @Override
    public void onEnabled() {
        if (mixinDisable()) {
            log().warn("""
            
            
            -------------------------------------------------------------------
                         CHARM IS RUNNING IN MIXIN DISABLE MODE
            -------------------------------------------------------------------
            
            No Charm-related mods will work as expected! This mode is used
            to determine if Charm mods have mixin conflicts with another mod.
            
            Eliminate mods from your mod pack one by one, testing mixin disable
            mode on and off, to find out where a conflict is happening.
            Then come to Charm's discord and talk to us.
            
            """);
        }
    }
}
