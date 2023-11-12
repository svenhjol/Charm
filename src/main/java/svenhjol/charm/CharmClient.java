package svenhjol.charm;

import svenhjol.charm.feature.arcane_purpur.ArcanePurpurClient;
import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.feature.azalea_wood.AzaleaWoodClient;
import svenhjol.charm.feature.bat_buckets.BatBucketsClient;
import svenhjol.charm.feature.chairs.ChairsClient;
import svenhjol.charm.feature.clear_item_frames.ClearItemFramesClient;
import svenhjol.charm.feature.colored_glint_smithing_templates.ColoredGlintSmithingTemplatesClient;
import svenhjol.charm.feature.compass_overlay.CompassOverlay;
import svenhjol.charm.feature.copper_pistons.CopperPistonsClient;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanternsClient;
import svenhjol.charm.feature.coral_squids.CoralSquidsClient;
import svenhjol.charm.feature.discs_stop_background_music.DiscsStopBackgroundMusic;
import svenhjol.charm.feature.endermite_powder.EndermitePowderClient;
import svenhjol.charm.feature.extra_tooltips.ExtraTooltips;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantmentsClient;
import svenhjol.charm.feature.gentle_potion_particles.GentlePotionParticles;
import svenhjol.charm.feature.hover_sorting.HoverSortingClient;
import svenhjol.charm.feature.inventory_tidying.InventoryTidyingClient;
import svenhjol.charm.feature.kilns.KilnsClient;
import svenhjol.charm.feature.mooblooms.MoobloomsClient;
import svenhjol.charm.feature.nearby_workstations.NearbyWorkstationsClient;
import svenhjol.charm.feature.no_chat_unverified_message.NoChatUnverifiedMessage;
import svenhjol.charm.feature.no_experimental_screen.NoExperimentalScreen;
import svenhjol.charm.feature.no_spyglass_scope.NoSpyglassScope;
import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlatesClient;
import svenhjol.charm.feature.redstone_sand.RedstoneSandClient;
import svenhjol.charm.feature.show_repair_cost.ShowRepairCost;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstoneClient;
import svenhjol.charm.feature.storage_blocks.StorageBlocksClient;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreservingClient;
import svenhjol.charm.feature.variant_mob_textures.VariantMobTextures;
import svenhjol.charm.feature.woodcutters.WoodcuttersClient;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.client.ClientMod;

import java.util.List;

public class CharmClient extends ClientMod {
    @Override
    public String modId() {
        return Charm.ID;
    }

    @Override
    public List<Class<? extends ClientFeature>> features() {
        return List.of(
            ArcanePurpurClient.class,
            AtlasesClient.class,
            AzaleaWoodClient.class,
            BatBucketsClient.class,
            ChairsClient.class,
            ClearItemFramesClient.class,
            ColoredGlintSmithingTemplatesClient.class,
            CompassOverlay.class,
            CopperPistonsClient.class,
            CoralSeaLanternsClient.class,
            CoralSquidsClient.class,
            DiscsStopBackgroundMusic.class,
            EndermitePowderClient.class,
            ExtraTooltips.class,
            ExtractableEnchantmentsClient.class,
            GentlePotionParticles.class,
            HoverSortingClient.class,
            InventoryTidyingClient.class,
            KilnsClient.class,
            MoobloomsClient.class,
            NoChatUnverifiedMessage.class,
            NoExperimentalScreen.class,
            NoSpyglassScope.class,
            PlayerPressurePlatesClient.class,
            NearbyWorkstationsClient.class,
            RedstoneSandClient.class,
            ShowRepairCost.class,
            SmoothGlowstoneClient.class,
            StorageBlocksClient.class,
            TotemOfPreservingClient.class,
            VariantMobTextures.class,
            WoodcuttersClient.class
        );
    }
}
