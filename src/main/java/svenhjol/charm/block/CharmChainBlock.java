package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.loader.CharmModule;

public class CharmChainBlock extends ChainBlock implements ICharmBlock {
    private final CharmModule module;
    private final IMetalMaterial material;

    public CharmChainBlock(CharmModule module, String name, IMetalMaterial material, Properties settings) {
        super(settings);
        this.module = module;
        this.material = material;
        this.register(module, name);
    }

    public CharmChainBlock(CharmModule module, String name, IMetalMaterial material) {
        this(module, name, material, Properties.copy(Blocks.CHAIN)
            .strength(material.getDestroyTime(), material.getResistance()));
    }

    @Override
    public void createBlockItem(ResourceLocation id, Item.Properties properties) {
        if (material.isFireResistant()) {
            properties.fireResistant();
        }
        ICharmBlock.super.createBlockItem(id, properties);
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled()) {
            super.fillItemCategory(group, items);
        }
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}

