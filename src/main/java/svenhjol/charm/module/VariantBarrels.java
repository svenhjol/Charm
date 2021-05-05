package svenhjol.charm.module;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.RegistryHelper;
import svenhjol.charm.base.helper.WorldHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.VariantBarrelBlock;

import java.util.*;

@Module(mod = Charm.MOD_ID, priority = 10, description = "Barrels available in all types of vanilla wood.")
public class VariantBarrels extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "barrel");
    public static final Map<IVariantMaterial, VariantBarrelBlock> BARREL_BLOCKS = new HashMap<>();

    public static BlockEntityType<BarrelBlockEntity> BLOCK_ENTITY;

    @Override
    public void register() {
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, BarrelBlockEntity::new);

        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            BARREL_BLOCKS.put(type, registerBarrel(this, type));
        }
    }

    public static VariantBarrelBlock registerBarrel(CharmModule module, IVariantMaterial material) {
        VariantBarrelBlock barrel = new VariantBarrelBlock(module, material);
        BARREL_BLOCKS.put(material, barrel);
        RegistryHelper.addBlocksToBlockEntity(BLOCK_ENTITY, barrel);
        WorldHelper.addBlockStatesToPointOfInterest(PointOfInterestType.FISHERMAN, barrel.getStateManager().getStates());
        return barrel;
    }

    public static VariantBarrelBlock getRandomBarrel(Random rand) {
        List<VariantBarrelBlock> values = new ArrayList<>(BARREL_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }

    @Override
    public List<String> mixins() {
        return Collections.singletonList("BarrelBlockEntityMixin");
    }
}
