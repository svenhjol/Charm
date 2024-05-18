package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.feature.item_restocking.ItemRestocking;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.feature.beekeepers.Beekeepers;
import svenhjol.charm.feature.campfires_boost_health.CampfiresBoostHealth;
import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.feature.chorus_teleport.ChorusTeleport;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;
import svenhjol.charm.feature.collection.Collection;
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplates;
import svenhjol.charm.feature.glint_coloring.GlintColoring;
import svenhjol.charm.feature.copper_pistons.CopperPistons;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.deepslate_dungeons.DeepslateDungeons;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.feature.note_blocks.NoteBlocks;
import svenhjol.charm.feature.nether_portal_blocks.NetherPortalBlocks;
import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm.feature.grindstone_disenchanting.GrindstoneDisenchanting;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.animal_armor_grinding.AnimalArmorGrinding;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;
import svenhjol.charm.feature.mineshaft_improvements.MineshaftImprovements;
import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.feature.lumberjacks.Lumberjacks;
import svenhjol.charm.feature.mooblooms.Mooblooms;
import svenhjol.charm.feature.potion_of_radiance.PotionOfRadiance;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;
import svenhjol.charm.feature.shulker_box_drag_drop.ShulkerBoxDragDrop;
import svenhjol.charm.feature.silence.Silence;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.feature.repair_cost_unlimited.RepairCostUnlimited;
import svenhjol.charm.feature.villager_attracting.VillagerAttracting;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public final class Charm {
    public static final String ID = "charm";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(ID, path);
    }

    public static List<Class<? extends CommonFeature>> features() {
        return List.of(
            ArcanePurpur.class,
            Atlases.class,
            ItemRestocking.class,
            BatBuckets.class,
            Beekeepers.class,
            CampfiresBoostHealth.class,
            Chairs.class,
            ChorusTeleport.class,
            ItemFrameHiding.class,
            Collection.class,
            GlintColorTemplates.class,
            GlintColoring.class,
            CopperPistons.class,
            CoralSeaLanterns.class,
            CoralSquids.class,
            Core.class,
            DeepslateDungeons.class,
            Echolocation.class,
            AnimalArmorEnchanting.class,
            EndermitePowder.class,
            NoteBlocks.class,
            NetherPortalBlocks.class,
            Wood.class,
            GrindstoneDisenchanting.class,
            Firing.class,
            AnimalArmorGrinding.class,
            ItemHoverSorting.class,
            MineshaftImprovements.class,
            ItemTidying.class,
            Kilns.class,
            Lumberjacks.class,
            Mooblooms.class,
            PotionOfRadiance.class,
            AnimalReviving.class,
            ShulkerBoxDragDrop.class,
            Silence.class,
            SmoothGlowstone.class,
            StorageBlocks.class,
            TotemOfPreserving.class,
            TotemsWorkFromInventory.class,
            RepairCostUnlimited.class,
            VillagerAttracting.class,
            Woodcutters.class,
            Woodcutting.class
        );
    }
}
