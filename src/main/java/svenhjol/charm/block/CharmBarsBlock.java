package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.loader.CharmModule;

public class CharmBarsBlock extends IronBarsBlock implements ICharmBlock {
    private final CharmModule module;
    private final IMetalMaterial material;

    public CharmBarsBlock(CharmModule module, String name, IMetalMaterial material, Properties properties) {
        super(properties);
        this.module = module;
        this.material = material;
        this.register(module, name);
    }

    public CharmBarsBlock(CharmModule module, String name, IMetalMaterial material) {
        this(module, name, material, Properties.copy(Blocks.IRON_BARS)
            .strength(material.getDestroyTime(), material.getResistance()));
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public void createBlockItem(ResourceLocation id, Item.Properties properties) {
        if (material.isFireResistant()) {
            properties.fireResistant();
        }
        ICharmBlock.super.createBlockItem(id, properties);
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
