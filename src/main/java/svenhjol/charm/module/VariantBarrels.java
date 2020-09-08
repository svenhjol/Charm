package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.block.VariantBarrelBlock;
import svenhjol.charm.blockentity.VariantBarrelBlockEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IVariantMaterial;
import svenhjol.meson.enums.VanillaVariantMaterial;
import svenhjol.meson.helper.OverrideHandler;
import svenhjol.meson.iface.Module;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(description = "Barrels available in all types of vanilla wood.")
public class VariantBarrels extends MesonModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "barrel");
    public static final Map<IVariantMaterial, VariantBarrelBlock> BARREL_BLOCKS = new HashMap<>();

    public static BlockEntityType<VariantBarrelBlockEntity> BLOCK_ENTITY;

    @Override
    public void register() {
        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            BARREL_BLOCKS.put(type, new VariantBarrelBlock(this, type));
        }

        BLOCK_ENTITY = BlockEntityType.Builder.create(VariantBarrelBlockEntity::new).build(null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, ID, BLOCK_ENTITY);
    }

    @Override
    public void init() {
        OverrideHandler.changeBlockTranslationKey(Blocks.BARREL, "block.charm.fisherman_barrel");
        OverrideHandler.changeItemTranslationKey(Items.BARREL, "item.charm.fisherman_barrel");
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        return Arrays.asList(new Identifier("minecraft", "barrel"));
    }
}
