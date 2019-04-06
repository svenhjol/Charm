package svenhjol.charm.misc;

import svenhjol.meson.Module;
import svenhjol.charm.misc.feature.*;

public class CharmMisc extends Module
{
    public CharmMisc()
    {
        features.add(new CactusWater());
        features.add(new CauldronWaterSource());
        features.add(new ExtraRecords());
        features.add(new LeatherArmorInvisibility());
        features.add(new RandomAnimalTextures());
        features.add(new RecordsStopBackgroundMusic());
        features.add(new ShearedFlowers());
        features.add(new ShearedMelons());
        features.add(new SpongesReduceFallDamage());
        features.add(new StackableEnchantedBooks());
        features.add(new StackableMilkBuckets());
        features.add(new StackablePotions());
        features.add(new TamedAnimalsNoDamage());
        features.add(new UseTotemFromInventory());
        features.add(new WaterInHotBiomes());
    }
}