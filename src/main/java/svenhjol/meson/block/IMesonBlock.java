package svenhjol.meson.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.mixin.accessor.FireBlockAccessor;
import svenhjol.charm.mixin.accessor.HoeItemAccessor;
import svenhjol.charm.mixin.accessor.PickaxeItemAccessor;
import svenhjol.charm.mixin.accessor.ShovelItemAccessor;
import svenhjol.meson.MesonModule;

import java.util.function.BiConsumer;

// TODO itemStackRenderer cannot be translated from Forge, mixins?
// TODO burnTime cannot be translated from Forge, research alternative
public interface IMesonBlock {
    boolean enabled();

    default void register(MesonModule module, String name) {
        Identifier id = new Identifier(module.mod.getId(), name);
        Registry.register(Registry.BLOCK, id, (Block)this);
        createBlockItem(id);
    }

    ItemGroup getItemGroup();

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

    default int getBurnTime() {
        return 0;
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
