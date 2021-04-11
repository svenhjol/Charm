package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.base.*;
import svenhjol.charm.base.handler.LogHandler;
import svenhjol.charm.module.*;

import java.util.Arrays;

public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("Charm");

    private static boolean hasRunFirst = false;

    public static void runFirst() {
        if (hasRunFirst)
            return;

        new CharmLoader(MOD_ID, Arrays.asList(
            Acquisition.class,
            AerialAffinity.class,
            AnvilImprovements.class,
            ArmorInvisibility.class,
            Astrolabes.class,
            Atlas.class,
            AutomaticRecipeUnlock.class,
            AutoRestock.class,
            BatBuckets.class,
            BeaconsHealMobs.class,
            Beekeepers.class,
            BlockOfEnderPearls.class,
            BlockOfGunpowder.class,
            BlockOfSugar.class,
            Bookcases.class,
            CampfiresNoDamage.class,
            CaveSpidersDropCobwebs.class,
            ChickensDropFeathers.class,
            CoralSeaLanterns.class,
            CoralSquids.class,
            Core.class,
            ExtraRecipes.class,
            DecreaseRepairCost.class,
            DirtToPath.class,
            EndermitePowder.class,
            EntitySpawners.class,
            ExtractEnchantments.class,
            FeatherFallingCrops.class,
            Glowballs.class,
            GoldBars.class,
            GoldChains.class,
            GoldLanterns.class,
            HoeHarvesting.class,
            HuskImprovements.class,
            InventoryTidying.class,
            Kilns.class,
            Lumberjacks.class,
            MapTooltips.class,
            MineshaftImprovements.class,
            Mooblooms.class,
            MorePortalFrames.class,
            MoreVillageBiomes.class,
            MusicImprovements.class,
            ParrotsStayOnShoulder.class,
            PathToDirt.class,
            PlayerPressurePlates.class,
            PlayerState.class,
            PortableCrafting.class,
            RaidHorns.class,
            RedstoneLanterns.class,
            RedstoneSand.class,
            RemoveNitwits.class,
            RemovePotionGlint.class,
            RemoveSpyglassScope.class,
            ShulkerBoxTooltips.class,
            SnowStorms.class,
            StackableEnchantedBooks.class,
            StackablePotions.class,
            StackableStews.class,
            StrayImprovements.class,
            TamedAnimalsNoDamage.class,
            ColoredGlints.class,
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

        CharmLoot.init();
        CharmStructures.init();
        CharmSounds.init();
        CharmTags.init();

        hasRunFirst = true;
    }

    @Override
    public void onInitialize() {
        runFirst();
    }
}
