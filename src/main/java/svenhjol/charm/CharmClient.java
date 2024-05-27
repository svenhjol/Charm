package svenhjol.charm;

import svenhjol.charm.feature.arcane_purpur.ArcanePurpurClient;
import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.feature.bat_buckets.BatBucketsClient;
import svenhjol.charm.feature.chairs.ChairsClient;
import svenhjol.charm.feature.compasses_show_position.CompassesShowPosition;
import svenhjol.charm.feature.cooking_pots.CookingPotsClient;
import svenhjol.charm.feature.copper_pistons.CopperPistonsClient;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanternsClient;
import svenhjol.charm.feature.coral_squids.CoralSquidsClient;
import svenhjol.charm.feature.core.CoreClient;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventoryClient;
import svenhjol.charm.feature.discs_stop_background_music.DiscsStopBackgroundMusic;
import svenhjol.charm.feature.endermite_powder.EndermitePowderClient;
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplatesClient;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;
import svenhjol.charm.feature.grindstone_disenchanting.GrindstoneDisenchantingClient;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHidingClient;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSortingClient;
import svenhjol.charm.feature.item_tidying.ItemTidyingClient;
import svenhjol.charm.feature.kilns.KilnsClient;
import svenhjol.charm.feature.mob_textures.MobTextures;
import svenhjol.charm.feature.mooblooms.MoobloomsClient;
import svenhjol.charm.feature.note_blocks.NoteBlocksClient;
import svenhjol.charm.feature.piglin_pointing.PiglinPointingClient;
import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlatesClient;
import svenhjol.charm.feature.raid_horns.RaidHornsClient;
import svenhjol.charm.feature.redstone_sand.RedstoneSandClient;
import svenhjol.charm.feature.repair_cost_visible.RepairCostVisible;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstoneClient;
import svenhjol.charm.feature.spyglass_scope_hiding.SpyglassScopeHiding;
import svenhjol.charm.feature.storage_blocks.StorageBlocksClient;
import svenhjol.charm.feature.tooltip_improvements.TooltipImprovements;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreservingClient;
import svenhjol.charm.feature.waypoints.WaypointsClient;
import svenhjol.charm.feature.wood.WoodClient;
import svenhjol.charm.feature.woodcutters.WoodcuttersClient;
import svenhjol.charm.foundation.client.ClientFeature;

import java.util.List;

public final class CharmClient {
    public static List<Class<? extends ClientFeature>> features() {
        return List.of(
            ArcanePurpurClient.class,
            AtlasesClient.class,
            BatBucketsClient.class,
            ChairsClient.class,
            CompassesShowPosition.class,
            CookingPotsClient.class,
            CopperPistonsClient.class,
            CoralSeaLanternsClient.class,
            CoralSquidsClient.class,
            CoreClient.class,
            CraftingFromInventoryClient.class,
            DiscsStopBackgroundMusic.class,
            EndermitePowderClient.class,
            GlintColorTemplatesClient.class,
            GlintColoringClient.class,
            GrindstoneDisenchantingClient.class,
            ItemFrameHidingClient.class,
            ItemHoverSortingClient.class,
            ItemTidyingClient.class,
            KilnsClient.class,
            MobTextures.class,
            MoobloomsClient.class,
            NoteBlocksClient.class,
            PiglinPointingClient.class,
            PlayerPressurePlatesClient.class,
            RaidHornsClient.class,
            RedstoneSandClient.class,
            RepairCostVisible.class,
            SmoothGlowstoneClient.class,
            SpyglassScopeHiding.class,
            StorageBlocksClient.class,
            TooltipImprovements.class,
            TotemOfPreservingClient.class,
            WaypointsClient.class,
            WoodClient.class,
            WoodcuttersClient.class
        );
    }
}
