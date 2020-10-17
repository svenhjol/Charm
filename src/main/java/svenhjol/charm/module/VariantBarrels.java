package svenhjol.charm.module;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.block.VariantBarrelBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Barrels available in all types of vanilla wood.")
public class VariantBarrels extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "barrel");
    public static final Map<IVariantMaterial, VariantBarrelBlock> BARREL_BLOCKS = new HashMap<>();

    public static BlockEntityType<BarrelBlockEntity> BLOCK_ENTITY;

    @Override
    public void register() {
        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            BARREL_BLOCKS.put(type, new VariantBarrelBlock(this, type));
        }

        BLOCK_ENTITY = BlockEntityType.Builder.create(BarrelBlockEntity::new).build(null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, ID, BLOCK_ENTITY);
    }
}
