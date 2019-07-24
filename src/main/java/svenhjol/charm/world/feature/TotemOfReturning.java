package svenhjol.charm.world.feature;

import net.minecraftforge.common.ForgeConfigSpec;
import svenhjol.charm.world.item.TotemOfReturningItem;
import svenhjol.meson.Feature;

public class TotemOfReturning extends Feature
{
    public static ForgeConfigSpec.BooleanValue addToLoot;
    public static TotemOfReturningItem totem;

    @Override
    public void configure()
    {
        super.configure();

        addToLoot = builder
            .comment("Add the totem to mansions, mineshafts and nether fortress loot.")
            .define("Add to loot", true);
    }

    @Override
    public void init()
    {
        super.init();
        totem = new TotemOfReturningItem();
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
