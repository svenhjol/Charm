package svenhjol.charm.block;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.block.CharmBlockItem;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.mixin.accessor.*;

import java.util.function.BiConsumer;

public interface ICharmBlock {
    boolean enabled();

    default void register(CharmModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.mod, name);
        RegistryHelper.block(id, (Block)this);
        createBlockItem(id);
    }

    default CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    default int getMaxStackSize() {
        return 64;
    }

    default void createBlockItem(ResourceLocation id) {
        Item.Properties settings = new Item.Properties();

        CreativeModeTab itemGroup = getItemGroup();
        if (itemGroup != null)
            settings.tab(itemGroup);

        settings.stacksTo(getMaxStackSize());

        svenhjol.charm.block.CharmBlockItem blockItem = new CharmBlockItem(this, settings);
        RegistryHelper.item(id, blockItem);
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
