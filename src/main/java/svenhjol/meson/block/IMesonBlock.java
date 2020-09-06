package svenhjol.meson.block;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.meson.mixin.accessor.FireBlockAccessor;
import svenhjol.charm.mixin.accessor.HoeItemAccessor;
import svenhjol.charm.mixin.accessor.PickaxeItemAccessor;
import svenhjol.charm.mixin.accessor.ShovelItemAccessor;
import svenhjol.meson.MesonModule;

import java.util.function.BiConsumer;

public interface IMesonBlock {
    boolean enabled();

    default void register(MesonModule module, String name) {
        Identifier id = new Identifier(module.mod.getId(), name);
        Registry.register(Registry.BLOCK, id, (Block)this);
        createBlockItem(id);
    }

    default ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    default int getMaxStackSize() {
        return 64;
    }

    default void createBlockItem(Identifier id) {
        Item.Settings settings = new Item.Settings();

        ItemGroup itemGroup = getItemGroup();
        if (itemGroup != null)
            settings.group(itemGroup);

        settings.maxCount(getMaxStackSize());

        MesonBlockItem blockItem = new MesonBlockItem(this, settings);
        Registry.register(Registry.ITEM, id, blockItem);
    }

    default BiConsumer<ItemStack, Boolean> getInventoryTickConsumer() {
        return null;
    }

    default void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add((Block)this, burnTime);
    }

    default void setFireInfo(int encouragement, int flammability) {
        ((FireBlockAccessor) Blocks.FIRE).invokeRegisterFlammableBlock((Block)this, encouragement, flammability);
    }

    default void setEffectiveTool(Class<? extends MiningToolItem> clazz) {
        if (clazz == PickaxeItem.class)
            PickaxeItemAccessor.getEffectiveBlocks().add((Block)this);

        if (clazz == ShovelItem.class)
            ShovelItemAccessor.getEffectiveBlocks().add((Block)this);

        if (clazz == HoeItem.class)
            HoeItemAccessor.getEffectiveBlocks().add((Block)this);
    }
}
