package svenhjol.charm.module.variant_lanterns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.CharmLanternBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_nuggets.ExtraNuggets;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Variants lanterns crafted from vanilla metal nuggets and torches.")
public class VariantLanterns extends CharmModule {
    public static Map<IMetalMaterial, CharmLanternBlock> LANTERNS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypes()) {
            BlockBehaviour.Properties lanternProperties = BlockBehaviour.Properties.copy(Blocks.LANTERN);
            BlockBehaviour.Properties soulLanternProperties = BlockBehaviour.Properties.copy(Blocks.SOUL_LANTERN);

            if (material == VanillaMetalMaterial.NETHERITE) {
                lanternProperties = lanternProperties.strength(50.0F, 1200.0F);
                soulLanternProperties = soulLanternProperties.strength(50.0F, 1200.0F);
            }

            LANTERNS.put(material, new CharmLanternBlock(this, material.getSerializedName() + "_lantern", lanternProperties));
            LANTERNS.put(material, new CharmLanternBlock(this, material.getSerializedName() + "_soul_lantern", soulLanternProperties));
        }
    }

    @Override
    public void runWhenEnabled() {
        // remove lantern recipes if nuggets module is disabled
        if (!Charm.LOADER.isEnabled(ExtraNuggets.class)) {
            RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "variant_lanterns/copper_lantern"));
            RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "variant_lanterns/copper_soul_lantern"));
            RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "variant_lanterns/netherite_lantern"));
            RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "variant_lanterns/netherite_soul_lantern"));
        }
    }
}
