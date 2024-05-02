package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.feature.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.feature.clear_item_frames.ClearItemFrames;
import svenhjol.charm.feature.colored_glint_smithing_templates.ColoredGlintSmithingTemplates;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.feature.diagnostics.Diagnostics;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantments;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.inventory_tidying.InventoryTidying;
import svenhjol.charm.feature.potion_of_radiance.PotionOfRadiance;
import svenhjol.charm.feature.recipes.Recipes;
import svenhjol.charm.feature.silence_microsoft.SilenceMicrosoft;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.feature.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.feature.variant_pistons.VariantPistons;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class Charm {
    public static final String ID = "charm";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(ID, path);
    }

    public static List<Class<? extends CommonFeature>> features() {
        return List.of(
            Advancements.class,
            AmethystNoteBlock.class,
            Atlases.class,
            AzaleaWood.class,
            Chairs.class,
            ClearItemFrames.class,
            ColoredGlints.class,
            ColoredGlintSmithingTemplates.class,
            CoralSquids.class,
            Core.class,
            CustomWood.class,
            Diagnostics.class,
            ExtractableEnchantments.class,
            Firing.class,
            InventoryTidying.class,
            PotionOfRadiance.class,
            Recipes.class,
            SilenceMicrosoft.class,
            SmoothGlowstone.class,
            VariantPistons.class,
            VanillaWoodVariants.class,
            VariantWood.class,
            Woodcutters.class,
            Woodcutting.class
        );
    }
}
