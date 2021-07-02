package svenhjol.charm.module.variant_barrels;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, priority = 10, description = "Barrels available in all types of vanilla wood.")
public class VariantBarrels extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "barrel");
    public static final Map<IVariantMaterial, VariantBarrelBlock> BARREL_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            registerBarrel(this, type);
        }
    }

    public static VariantBarrelBlock registerBarrel(CharmModule module, IVariantMaterial material) {
        VariantBarrelBlock barrel = new VariantBarrelBlock(module, material);
        BARREL_BLOCKS.put(material, barrel);
        RegistryHelper.addBlocksToBlockEntity(BlockEntityType.BARREL, barrel);
        WorldHelper.addBlockStatesToPointOfInterest(PoiType.FISHERMAN, barrel.getStateDefinition().getPossibleStates());
        return barrel;
    }

    public static VariantBarrelBlock getRandomBarrel(Random rand) {
        List<VariantBarrelBlock> values = new ArrayList<>(BARREL_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }
}
