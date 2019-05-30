package svenhjol.charm.tweaks;

import svenhjol.meson.Module;
import svenhjol.charm.tweaks.feature.*;

public class CharmTweaks extends Module
{
    public CharmTweaks()
    {
        features.add(new CactusWater());
        features.add(new CauldronWaterSource());
        features.add(new CompassBinding());
        features.add(new DispensersUseAnyBow());
        features.add(new ExtraRecords());
        features.add(new FurnacesRecycleMore());
        features.add(new LeatherArmorInvisibility());
        features.add(new PickaxesBreakPistons());
        features.add(new RandomAnimalTextures());
        features.add(new RecordsStopBackgroundMusic());
        features.add(new RestrictFurnaceInput());
        features.add(new ShearedMelons());
        features.add(new SpongesReduceFallDamage());
        features.add(new StackableEnchantedBooks());
        features.add(new StackableMilkBuckets());
        features.add(new StackablePotions());
        features.add(new TamedAnimalsHealing());
        features.add(new TamedAnimalsNoDamage());
        features.add(new UnearthItems());
        features.add(new UseTotemFromInventory());
        features.add(new WaterInHotBiomes());
        features.add(new WitchesDropDecay());
        features.add(new WitchesDropLuck());
        features.add(new WitchHatProtection());
    }
}