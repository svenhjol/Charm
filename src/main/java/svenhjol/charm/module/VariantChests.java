package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.block.VariantChestBlock;
import svenhjol.charm.block.VariantTrappedChestBlock;
import svenhjol.charm.blockentity.VariantChestBlockEntity;
import svenhjol.charm.blockentity.VariantChestClient;
import svenhjol.charm.blockentity.VariantTrappedChestBlockEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IVariantMaterial;
import svenhjol.meson.enums.VanillaVariantMaterial;
import svenhjol.meson.iface.Module;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(description = "Chests available in all types of vanilla wood.")
public class VariantChests extends MesonModule {
    public static final Identifier NORMAL_ID = new Identifier("variant_chest");
    public static final Identifier TRAPPED_ID = new Identifier(Charm.MOD_ID, "trapped_chest");

    public static final Map<IVariantMaterial, VariantChestBlock> NORMAL_CHEST_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, VariantTrappedChestBlock> TRAPPED_CHEST_BLOCKS = new HashMap<>();

    public static BlockEntityType<VariantChestBlockEntity> NORMAL_BLOCK_ENTITY;
    public static BlockEntityType<VariantTrappedChestBlockEntity> TRAPPED_BLOCK_ENTITY;

    @Override
    public void register() {
        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            NORMAL_CHEST_BLOCKS.put(type, new VariantChestBlock(this, type));
            TRAPPED_CHEST_BLOCKS.put(type, new VariantTrappedChestBlock(this, type));
        }

        NORMAL_BLOCK_ENTITY = BlockEntityType.Builder.create(VariantChestBlockEntity::new, NORMAL_CHEST_BLOCKS.values().toArray(new Block[0])).build(null);
        TRAPPED_BLOCK_ENTITY = BlockEntityType.Builder.create(VariantTrappedChestBlockEntity::new, TRAPPED_CHEST_BLOCKS.values().toArray(new Block[0])).build(null);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, NORMAL_ID, NORMAL_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, TRAPPED_ID, TRAPPED_BLOCK_ENTITY);
    }

    @Override
    public void initClientWhenEnabled() {
        new VariantChestClient(this);
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        return Arrays.asList(
            new Identifier("minecraft", "chest"),
            new Identifier("minecraft", "trapped_chest")
        );
    }
}
