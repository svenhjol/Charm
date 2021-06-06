package svenhjol.charm.module.variant_chests;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.module.variant_chests.VariantChestBlock;
import svenhjol.charm.module.variant_chests.VariantChestBlockEntity;
import svenhjol.charm.module.variant_chests.VariantChestsClient;
import svenhjol.charm.module.variant_chests.VariantTrappedChestBlock;
import svenhjol.charm.module.variant_chests.VariantTrappedChestBlockEntity;

@Module(mod = Charm.MOD_ID, priority = 10, client = VariantChestsClient.class, description = "Chests available in all types of vanilla wood.",
    requiresMixins = {"StitchTextureCallback", "RenderBlockItemCallback"})
public class VariantChests extends CharmModule {
    public static final ResourceLocation NORMAL_ID = new ResourceLocation(Charm.MOD_ID, "variant_chest");
    public static final ResourceLocation TRAPPED_ID = new ResourceLocation(Charm.MOD_ID, "trapped_chest");

    public static final Map<IVariantMaterial, svenhjol.charm.module.variant_chests.VariantChestBlock> NORMAL_CHEST_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, svenhjol.charm.module.variant_chests.VariantTrappedChestBlock> TRAPPED_CHEST_BLOCKS = new HashMap<>();

    public static BlockEntityType<svenhjol.charm.module.variant_chests.VariantChestBlockEntity> NORMAL_BLOCK_ENTITY;
    public static BlockEntityType<svenhjol.charm.module.variant_chests.VariantTrappedChestBlockEntity> TRAPPED_BLOCK_ENTITY;

    @Override
    public void register() {
        NORMAL_BLOCK_ENTITY = RegistryHelper.blockEntity(NORMAL_ID, VariantChestBlockEntity::new, NORMAL_CHEST_BLOCKS.values().toArray(new Block[0]));
        TRAPPED_BLOCK_ENTITY = RegistryHelper.blockEntity(TRAPPED_ID, VariantTrappedChestBlockEntity::new, TRAPPED_CHEST_BLOCKS.values().toArray(new Block[0]));

        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            registerChest(this, type);
            registerTrappedChest(this, type);
        }
    }

    public static svenhjol.charm.module.variant_chests.VariantChestBlock registerChest(CharmModule module, IVariantMaterial material) {
        svenhjol.charm.module.variant_chests.VariantChestBlock chest = new VariantChestBlock(module, material);
        NORMAL_CHEST_BLOCKS.put(material, chest);
        RegistryHelper.addBlocksToBlockEntity(NORMAL_BLOCK_ENTITY, chest);
        return chest;
    }

    public static svenhjol.charm.module.variant_chests.VariantTrappedChestBlock registerTrappedChest(CharmModule module, IVariantMaterial material) {
        svenhjol.charm.module.variant_chests.VariantTrappedChestBlock chest = new VariantTrappedChestBlock(module, material);
        TRAPPED_CHEST_BLOCKS.put(material, chest);
        RegistryHelper.addBlocksToBlockEntity(TRAPPED_BLOCK_ENTITY, chest);
        return chest;
    }
}
