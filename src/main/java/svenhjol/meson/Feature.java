package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class Feature
{
    public Module module;
    public MesonLoader loader;
    public ForgeConfigSpec.Builder builder;
    public ForgeConfigSpec.BooleanValue enabled;

    public void setup(Module module)
    {
        this.module = module;
        loader = module.loader;
        builder = module.builder;

        Meson.log("Configuring feature " + getName());
        configure();
    }

    public void configure()
    {
        // allow the feature to define its own configuration items
    }

    public void init()
    {
        if (hasSubscriptions()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void initClient()
    {
        // no op
    }

    public boolean isEnabled()
    {
        return enabled.get();
    }

    public boolean isEnabledByDefault()
    {
        return true;
    }

    public boolean hasSubscriptions()
    {
        return false;
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    public String getDescription()
    {
        return getName();
    }

    public void registerBlocks(final IForgeRegistry<Block> registry)
    {
        // no op
    }

    public void registerComposterItems()
    {
        // no op
    }

    public void registerContainers(final IForgeRegistry<ContainerType<?>> registry)
    {
        // no op
    }

    public void registerItems(final IForgeRegistry<Item> registry)
    {
        // no op
    }

    public void registerEffects(final IForgeRegistry<Effect> registry)
    {
        // no op
    }

    public void registerEnchantments(final IForgeRegistry<Enchantment> registry)
    {
        // no op
    }

    public void registerMessages()
    {
        // no op
    }

    public void registerPotions(final IForgeRegistry<Potion> registry)
    {
        // no op
    }

    @OnlyIn(Dist.CLIENT)
    public void registerScreens()
    {
        // no op
    }

    public void registerSounds(final IForgeRegistry<SoundEvent> registry)
    {
        // no op
    }

    public void registerTileEntities(final IForgeRegistry<TileEntityType<?>> registry)
    {
        // no op
    }
}
