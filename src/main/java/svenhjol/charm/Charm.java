package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.feature.auto_restock.AutoRestock;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.feature.beekeepers.Beekeepers;
import svenhjol.charm.feature.campfires_boost_health.CampfiresBoostHealth;
import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.feature.chorus_teleport.ChorusTeleport;
import svenhjol.charm.feature.clear_item_frames.ClearItemFrames;
import svenhjol.charm.feature.collection.Collection;
import svenhjol.charm.feature.colored_glint_templates.ColoredGlintTemplates;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.feature.copper_pistons.CopperPistons;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.deepslate_dungeons.DeepslateDungeons;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charm.feature.enchantable_animal_armor.EnchantableAnimalArmor;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.feature.extra_note_blocks.ExtraNoteBlocks;
import svenhjol.charm.feature.extra_wood.ExtraWood;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantments;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.grindable_horse_armor.GrindableHorseArmor;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.feature.inventory_tidying.InventoryTidying;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.feature.lumberjacks.Lumberjacks;
import svenhjol.charm.feature.mooblooms.Mooblooms;
import svenhjol.charm.feature.potion_of_radiance.PotionOfRadiance;
import svenhjol.charm.feature.revive_pets.RevivePets;
import svenhjol.charm.feature.shulker_box_drag_drop.ShulkerBoxDragDrop;
import svenhjol.charm.feature.silence.Silence;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.feature.unlimited_repair_cost.UnlimitedRepairCost;
import svenhjol.charm.feature.variant_pistons.VariantPistons;
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
            AutoRestock.class,
            BatBuckets.class,
            Beekeepers.class,
            CampfiresBoostHealth.class,
            Chairs.class,
            ChorusTeleport.class,
            ClearItemFrames.class,
            Collection.class,
            ColoredGlintTemplates.class,
            ColoredGlints.class,
            CopperPistons.class,
            CoralSeaLanterns.class,
            CoralSquids.class,
            Core.class,
            DeepslateDungeons.class,
            Echolocation.class,
            EnchantableAnimalArmor.class,
            EndermitePowder.class,
            ExtraNoteBlocks.class,
            ExtraWood.class,
            ExtractableEnchantments.class,
            Firing.class,
            GrindableHorseArmor.class,
            HoverSorting.class,
            InventoryTidying.class,
            Kilns.class,
            Lumberjacks.class,
            Mooblooms.class,
            PotionOfRadiance.class,
            RevivePets.class,
            ShulkerBoxDragDrop.class,
            Silence.class,
            SmoothGlowstone.class,
            StorageBlocks.class,
            TotemOfPreserving.class,
            TotemsWorkFromInventory.class,
            UnlimitedRepairCost.class,
            VariantPistons.class,
            Woodcutters.class,
            Woodcutting.class
        );
    }
}
