package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.aerial_affinity.AerialAffinity;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;
import svenhjol.charm.feature.animal_armor_grinding.AnimalArmorGrinding;
import svenhjol.charm.feature.animal_damage_immunity.AnimalDamageImmunity;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;
import svenhjol.charm.feature.anvils_last_longer.AnvilsLastLonger;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.feature.beacons_heal_mobs.BeaconsHealMobs;
import svenhjol.charm.feature.beekeepers.Beekeepers;
import svenhjol.charm.feature.campfires_heal_players.CampfiresHealPlayers;
import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.feature.collection.Collection;
import svenhjol.charm.feature.copper_pistons.CopperPistons;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.crop_feather_falling.CropFeatherFalling;
import svenhjol.charm.feature.crop_replanting.CropReplanting;
import svenhjol.charm.feature.deepslate_dungeons.DeepslateDungeons;
import svenhjol.charm.feature.doors_open_together.DoorsOpenTogether;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplates;
import svenhjol.charm.feature.glint_coloring.GlintColoring;
import svenhjol.charm.feature.grindstone_disenchanting.GrindstoneDisenchanting;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;
import svenhjol.charm.feature.item_repairing.ItemRepairing;
import svenhjol.charm.feature.item_restocking.ItemRestocking;
import svenhjol.charm.feature.item_stacking.ItemStacking;
import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.feature.lumberjacks.Lumberjacks;
import svenhjol.charm.feature.mineshaft_improvements.MineshaftImprovements;
import svenhjol.charm.feature.mob_drops.MobDrops;
import svenhjol.charm.feature.mooblooms.Mooblooms;
import svenhjol.charm.feature.nether_portal_blocks.NetherPortalBlocks;
import svenhjol.charm.feature.note_block_lower_pitch.NoteBlockLowerPitch;
import svenhjol.charm.feature.note_blocks.NoteBlocks;
import svenhjol.charm.feature.parrots_stay_on_shoulder.ParrotsStayOnShoulder;
import svenhjol.charm.feature.path_converting.PathConverting;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;
import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlates;
import svenhjol.charm.feature.potion_of_radiance.PotionOfRadiance;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.feature.redstone_sand.RedstoneSand;
import svenhjol.charm.feature.repair_cost_unlimited.RepairCostUnlimited;
import svenhjol.charm.feature.shulker_box_transferring.ShulkerBoxTransferring;
import svenhjol.charm.feature.silence.Silence;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.feature.spawners_drop_items.SpawnersDropItems;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.suspicious_block_creating.SuspiciousBlockCreating;
import svenhjol.charm.feature.suspicious_effect_improvements.SuspiciousEffectImprovements;
import svenhjol.charm.feature.torchflowers_emit_light.TorchflowersEmitLight;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.feature.trade_improvements.TradeImprovements;
import svenhjol.charm.feature.villager_attracting.VillagerAttracting;
import svenhjol.charm.feature.wood.Wood;
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
            AerialAffinity.class,
            AnimalArmorEnchanting.class,
            AnimalArmorGrinding.class,
            AnimalDamageImmunity.class,
            AnimalReviving.class,
            AnvilsLastLonger.class,
            ArcanePurpur.class,
            Atlases.class,
            BatBuckets.class,
            BeaconsHealMobs.class,
            Beekeepers.class,
            CampfiresHealPlayers.class,
            Chairs.class,
            Collection.class,
            CopperPistons.class,
            CoralSeaLanterns.class,
            CoralSquids.class,
            Core.class,
            CropFeatherFalling.class,
            CropReplanting.class,
            DeepslateDungeons.class,
            DoorsOpenTogether.class,
            Echolocation.class,
            EndermitePowder.class,
            Firing.class,
            GlintColorTemplates.class,
            GlintColoring.class,
            GrindstoneDisenchanting.class,
            ItemFrameHiding.class,
            ItemHoverSorting.class,
            ItemRepairing.class,
            ItemRestocking.class,
            ItemStacking.class,
            ItemTidying.class,
            Kilns.class,
            Lumberjacks.class,
            MineshaftImprovements.class,
            MobDrops.class,
            Mooblooms.class,
            NetherPortalBlocks.class,
            NoteBlockLowerPitch.class,
            NoteBlocks.class,
            ParrotsStayOnShoulder.class,
            PathConverting.class,
            PigsFindMushrooms.class,
            PlayerPressurePlates.class,
            PotionOfRadiance.class,
            RecipeImprovements.class,
            RedstoneSand.class,
            RepairCostUnlimited.class,
            ShulkerBoxTransferring.class,
            Silence.class,
            SmoothGlowstone.class,
            SpawnersDropItems.class,
            StorageBlocks.class,
            SuspiciousBlockCreating.class,
            SuspiciousEffectImprovements.class,
            TorchflowersEmitLight.class,
            TotemOfPreserving.class,
            TotemsWorkFromInventory.class,
            TradeImprovements.class,
            VillagerAttracting.class,
            Wood.class,
            Woodcutters.class,
            Woodcutting.class
        );
    }
}
