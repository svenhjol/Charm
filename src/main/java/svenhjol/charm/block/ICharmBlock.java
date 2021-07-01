package svenhjol.charm.block;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.mixin.accessor.FireBlockAccessor;
import svenhjol.charm.loader.CommonModule;

import java.util.function.BiConsumer;

public interface ICharmBlock {
    boolean enabled();

    default void register(CommonModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.getModId(), name);
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

    /**
     * {@link AbstractFurnaceBlockEntity#getFuel()} for vanilla fuel burn times
     */
    default void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add((Block)this, burnTime);
    }

    default void setFireInfo(int encouragement, int flammability) {
        ((FireBlockAccessor) Blocks.FIRE).invokeSetFlammable((Block)this, encouragement, flammability);
    }

    default void setFireproof() {
        FuelRegistry.INSTANCE.remove((Block)this);
        ((FireBlockAccessor)Blocks.FIRE).getBurnOdds().put((Block)this, 0);
        ((FireBlockAccessor)Blocks.FIRE).getFlameOdds().put((Block)this, 0);
    }
}
