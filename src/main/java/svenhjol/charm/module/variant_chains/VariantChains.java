package svenhjol.charm.module.variant_chains;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.CharmChainBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_nuggets.ExtraNuggets;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, description = "Variant chains crafted from vanilla metal ingots and nuggets.")
public class VariantChains extends CharmModule {
    public static Map<IMetalMaterial, CharmChainBlock> CHAINS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypes()) {
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.CHAIN);

            if (material == VanillaMetalMaterial.NETHERITE)
                properties = properties.strength(50.0F, 1200.0F);

            CHAINS.put(material, new CharmChainBlock(this, material.getSerializedName() + "_chain", properties));
        }

        // remove chain recipes if nuggets module is disabled
        if (!Charm.LOADER.isEnabled(ExtraNuggets.class)) {
            RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "variant_chains/copper_chain"));
            RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "variant_chains/netherite_chain"));
        }
    }
}
