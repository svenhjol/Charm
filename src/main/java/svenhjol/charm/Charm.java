package svenhjol.charm;

import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.CharmTags;
import svenhjol.charm.module.*;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Charm extends MesonMod {
    public static final String MOD_ID = "charm";

    @Override
    public void onInitialize() {
        super.init(MOD_ID);
        CharmSounds.init();
        CharmTags.init();

        // Charm seems to always be the last to launch, but needs more testing
        Meson.go();
    }

    @Override
    public List<Class<? extends MesonModule>> getModules() {
        return new ArrayList<>(Arrays.asList(
            AnvilImprovements.class,
            ArmorInvisibility.class,
            AutomaticRecipeUnlock.class,
            BatBuckets.class,
            BeaconsHealMobs.class,
            Beekeepers.class,
            BlockOfEnderPearls.class,
            BlockOfGunpowder.class,
            BlockOfSugar.class,
            Bookcases.class,
            CampfiresNoDamage.class,
            Candles.class,
            CaveSpidersDropCobwebs.class,
            ChickensDropFeathers.class,
            Core.class,
            CoralSquids.class,
            Crates.class,
            DecreaseRepairCost.class,
            DirtToPath.class,
            EndermitePowder.class,
            ExtendNetherite.class,
            ExtractEnchantments.class,
            FeatherFallingCrops.class,
            GlowPearls.class,
            GoldBars.class,
            GoldChains.class,
            GoldLanterns.class,
            HoeHarvesting.class,
            HuskImprovements.class,
            InventoryTidying.class,
            Lumberjacks.class,
            MineshaftImprovements.class,
            MoreVillageBiomes.class,
            MusicImprovements.class,
            NetheriteNuggets.class,
            PathToDirt.class,
            ParrotsStayOnShoulder.class,
            PlaceableGlowstoneDust.class,
            PlayerState.class,
            PortableCrafting.class,
            PortableEnderChest.class,
            RedstoneLanterns.class,
            RedstoneSand.class,
            RemoveNitwits.class,
            RemovePotionGlint.class,
            ShulkerBoxTooltips.class,
            SleepImprovements.class,
            StackableEnchantedBooks.class,
            StackablePotions.class,
            StrayImprovements.class,
            TamedAnimalsNoDamage.class,
            UseTotemFromInventory.class,
            VariantBarrels.class,
            VariantBookshelves.class,
            VariantChests.class,
            VariantLadders.class,
            VariantMobTextures.class,
            VillagersFollowEmeraldBlocks.class,
            WanderingTraderImprovements.class,
            WitchesDropLuck.class,
            Woodcutters.class
        ));
    }
}
