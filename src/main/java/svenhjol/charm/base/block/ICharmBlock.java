package svenhjol.charm.base.block;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.mixin.accessor.*;

import java.util.function.BiConsumer;

public interface ICharmBlock {
    boolean enabled();

    default void register(CharmModule module, String name) {
        Identifier id = new Identifier(module.mod, name);
        RegistryHandler.block(id, (Block)this);
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

        CharmBlockItem blockItem = new CharmBlockItem(this, settings);
        RegistryHandler.item(id, blockItem);
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
}
