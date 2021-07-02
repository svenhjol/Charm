package svenhjol.charm.module.variant_bars;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.CharmBarsBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Variant bars crafted from vanilla metal ingots.")
public class VariantBars extends CharmModule {
    public static Map<IMetalMaterial, CharmBarsBlock> BARS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypes()) {
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.IRON_BARS);

            if (material == VanillaMetalMaterial.NETHERITE)
                properties = properties.strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK);

            BARS.put(material, new CharmBarsBlock(this, material.getSerializedName() + "_bars", properties));
        }
    }
}
