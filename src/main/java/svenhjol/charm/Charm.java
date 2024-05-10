package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.feature.auto_restock.AutoRestock;
import svenhjol.charm.feature.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.feature.variant_wood.VariantWood;
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
            AutoRestock.class,
//            AzaleaWood.class,
//            BatBuckets.class,
//            Chairs.class,
//            ClearItemFrames.class,
//            Collection.class,
//            ColoredGlints.class,
//            ColoredGlintSmithingTemplates.class,
//            CopperPistons.class,
//            CoralSquids.class,
//            Core.class,
//            CustomWood.class,
//            DeepslateDungeons.class,
//            Diagnostics.class,
//            Echolocation.class,
//            ExtractableEnchantments.class,
//            Firing.class,
//            HoverSorting.class,
//            InventoryTidying.class,
//            Kilns.class,
//            PotionOfRadiance.class,
//            Recipes.class,
//            ShulkerBoxDragDrop.class,
//            SilenceMicrosoft.class,
//            SmoothGlowstone.class,
//            TotemOfPreserving.class,
//            TotemsWorkFromInventory.class,
//            VariantPistons.class,
            VanillaWoodVariants.class,
            VariantWood.class,
//            Woodcutters.class,
            Woodcutting.class
        );
    }
}
