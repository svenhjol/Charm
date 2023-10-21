package svenhjol.charm;

import svenhjol.charm.feature.aerial_affinity.AerialAffinity;
import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.feature.auto_recipe_unlock.AutoRecipeUnlock;
import svenhjol.charm.feature.auto_restock.AutoRestock;
import svenhjol.charm.feature.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.feature.beacons_heal_mobs.BeaconsHealMobs;
import svenhjol.charm.feature.beekeepers.Beekeepers;
import svenhjol.charm.feature.campfires_boost_health.CampfiresBoostHealth;
import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.feature.clear_item_frames.ClearItemFrames;
import svenhjol.charm.feature.collection.Collection;
import svenhjol.charm.feature.colored_glint_smithing_templates.ColoredGlintSmithingTemplates;
import svenhjol.charm.feature.copper_pistons.CopperPistons;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.deepslate_dungeons.DeepslateDungeons;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charm.feature.enchantable_horse_armor.EnchantableHorseArmor;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.feature.extra_mob_drops.ExtraMobDrops;
import svenhjol.charm.feature.extra_portal_frames.ExtraPortalFrames;
import svenhjol.charm.feature.extra_recipes.ExtraRecipes;
import svenhjol.charm.feature.extra_repairs.ExtraRepairs;
import svenhjol.charm.feature.extra_stackables.ExtraStackables;
import svenhjol.charm.feature.extra_stews.ExtraStews;
import svenhjol.charm.feature.extra_trades.ExtraTrades;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantments;
import svenhjol.charm.feature.grindable_armor.GrindableArmor;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.feature.improved_mineshafts.ImprovedMineshafts;
import svenhjol.charm.feature.inventory_tidying.InventoryTidying;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.feature.longer_suspicious_effects.LongerSuspiciousEffects;
import svenhjol.charm.feature.lower_noteblock_pitch.LowerNoteblockPitch;
import svenhjol.charm.feature.lumberjacks.Lumberjacks;
import svenhjol.charm.feature.make_suspicious_blocks.MakeSuspiciousBlocks;
import svenhjol.charm.feature.mooblooms.Mooblooms;
import svenhjol.charm.feature.no_crop_trampling.NoCropTrampling;
import svenhjol.charm.feature.no_pet_damage.NoPetDamage;
import svenhjol.charm.feature.open_both_doors.OpenBothDoors;
import svenhjol.charm.feature.parrots_stay_on_shoulder.ParrotsStayOnShoulder;
import svenhjol.charm.feature.path_conversion.PathConversion;
import svenhjol.charm.feature.piglins_follow_gold_blocks.PiglinsFollowGoldBlocks;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;
import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlates;
import svenhjol.charm.feature.potion_of_radiance.PotionOfRadiance;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstations;
import svenhjol.charm.feature.quick_replant.QuickReplant;
import svenhjol.charm.feature.redstone_sand.RedstoneSand;
import svenhjol.charm.feature.revive_pets.RevivePets;
import svenhjol.charm.feature.shulker_box_drag_drop.ShulkerBoxDragDrop;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.feature.spawner_drops.SpawnerDrops;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.stronger_anvils.StrongerAnvils;
import svenhjol.charm.feature.torchflowers_emit_light.TorchflowersEmitLight;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.feature.unlimited_repair_cost.UnlimitedRepairCost;
import svenhjol.charm.feature.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.feature.variant_pistons.VariantPistons;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.villagers_follow_emerald_blocks.VillagersFollowEmeraldBlocks;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.base.DefaultMod;

import java.util.List;

public class Charm extends DefaultMod {
    public static final String MOD_ID = "charm";
    private static Charm instance;

    public static Charm instance() {
        if (instance == null) {
            instance = new Charm();
        }
        return instance;
    }

    @Override
    public String modId() {
        return MOD_ID;
    }

    @Override
    public List<Class<? extends CharmonyFeature>> features() {
        return List.of(
            AerialAffinity.class,
            AmethystNoteBlock.class,
            Atlases.class,
            AutoRecipeUnlock.class,
            AutoRestock.class,
            AzaleaWood.class,
            BatBuckets.class,
            BeaconsHealMobs.class,
            Beekeepers.class,
            CampfiresBoostHealth.class,
            Chairs.class,
            ClearItemFrames.class,
            Collection.class,
            ColoredGlintSmithingTemplates.class,
            CopperPistons.class,
            CoralSeaLanterns.class,
            CoralSquids.class,
            Core.class,
            DeepslateDungeons.class,
            Echolocation.class,
            EnchantableHorseArmor.class,
            EndermitePowder.class,
            ExtraMobDrops.class,
            ExtraPortalFrames.class,
            ExtraRecipes.class,
            ExtraRepairs.class,
            ExtraStackables.class,
            ExtraStews.class,
            ExtraTrades.class,
            ExtractableEnchantments.class,
            GrindableArmor.class,
            HoverSorting.class,
            ImprovedMineshafts.class,
            InventoryTidying.class,
            Kilns.class,
            LongerSuspiciousEffects.class,
            LowerNoteblockPitch.class,
            Lumberjacks.class,
            MakeSuspiciousBlocks.class,
            Mooblooms.class,
            NoCropTrampling.class,
            NoPetDamage.class,
            OpenBothDoors.class,
            ParrotsStayOnShoulder.class,
            PathConversion.class,
            PiglinsFollowGoldBlocks.class,
            PigsFindMushrooms.class,
            PlayerPressurePlates.class,
            PotionOfRadiance.class,
            ProximityWorkstations.class,
            QuickReplant.class,
            RedstoneSand.class,
            RevivePets.class,
            ShulkerBoxDragDrop.class,
            SmoothGlowstone.class,
            SpawnerDrops.class,
            StorageBlocks.class,
            StrongerAnvils.class,
            TorchflowersEmitLight.class,
            TotemOfPreserving.class,
            TotemsWorkFromInventory.class,
            UnlimitedRepairCost.class,
            VanillaWoodVariants.class,
            VariantPistons.class,
            VariantWood.class,
            VillagersFollowEmeraldBlocks.class,
            Woodcutters.class
        );
    }
}
