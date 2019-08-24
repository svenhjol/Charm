package svenhjol.meson;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.List;

public abstract class MesonModule
{
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean hasSubscriptions = false;
    public String mod = "";
    public String category = "";
    public String name = "";
    public String description = "";

    public boolean isEnabled()
    {
        return enabled;
    }

    public String getName()
    {
        return this.name.isEmpty() ? this.getClass().getSimpleName() : this.name;
    }

    public void init()
    {
        // register blocks, TEs, etc
    }

    public void setup(FMLCommonSetupEvent event)
    {
        // register messages, composter items, etc
    }

    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event)
    {
        // register screens, etc
    }

    public void configChanged(ModConfigEvent event)
    {
        // modules can be enabled/disabled when config changes
    }

    public void loadComplete(FMLLoadCompleteEvent event)
    {
        // do final things
    }

    public void registerTrades(Int2ObjectMap<List<ITrade>> trades, VillagerProfession profession)
    {
        // register villager trades based on profession
    }
}
