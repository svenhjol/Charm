package svenhjol.charm.module.variant_lanterns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.block.CharmLanternBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;

import java.util.*;

@Module(mod = Charm.MOD_ID, client = VariantLanternsClient.class, description = "Variants lanterns crafted from vanilla metal nuggets and torches.")
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
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> remove = new ArrayList<>();

        // remove lantern recipes if nuggets module is disabled
        if (!ModuleHandler.enabled("charm:extra_nuggets")) {
            remove.addAll(Arrays.asList(
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/copper_lantern"),
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/copper_soul_lantern"),
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/netherite_lantern"),
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/netherite_soul_lantern")
            ));
        }

        return remove;
    }
}
