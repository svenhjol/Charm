package svenhjol.charm.module.variant_barrels;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.annotation.Module;

import java.util.*;

@Module(mod = Charm.MOD_ID, priority = 10, description = "Barrels available in all types of vanilla wood.")
public class VariantBarrels extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "barrel");
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
        WorldHelper.addBlockStatesToPointOfInterest(PointOfInterestType.FISHERMAN, barrel.getStateManager().getStates());
        return barrel;
    }

    public static VariantBarrelBlock getRandomBarrel(Random rand) {
        List<VariantBarrelBlock> values = new ArrayList<>(BARREL_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }
}
