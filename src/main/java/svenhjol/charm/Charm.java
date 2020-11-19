package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.CharmStructures;
import svenhjol.charm.base.CharmTags;
import svenhjol.charm.base.handler.LogHandler;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.module.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static final boolean EXPERIMENTAL = true;
    public static LogHandler LOG = new LogHandler("Charm");

    @Override
    public void onInitialize() {
        ModuleHandler.AVAILABLE_MODULES.put(Charm.MOD_ID, new ArrayList<>(Arrays.asList(
            Acquisition.class,
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
            CoralSeaLanterns.class,
            CoralSquids.class,
            Core.class,
            Crates.class,
            DecreaseRepairCost.class,
            DirtToPath.class,
            EndermitePowder.class,
            EntitySpawner.class,
            ExtendNetherite.class,
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
            MineshaftImprovements.class,
            MoreVillageBiomes.class,
            MusicImprovements.class,
            NetheriteNuggets.class,
            ParrotsStayOnShoulder.class,
            PathToDirt.class,
            PlayerState.class,
            PortableCrafting.class,
            PortableEnderChest.class,
            RedstoneLanterns.class,
            RedstoneSand.class,
            RefinedObsidian.class,
            RemoveNitwits.class,
            RemovePotionGlint.class,
            ShulkerBoxTooltips.class,
            SleepImprovements.class,
            SmoothGlowstone.class,
            SnowStorms.class,
            StackableEnchantedBooks.class,
            StackablePotions.class,
            StrayImprovements.class,
            TamedAnimalsNoDamage.class,
            Tinted.class,
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
        )));

        CharmStructures.init();
        CharmSounds.init();
        CharmTags.init();

        ModuleHandler.init();
    }
}
