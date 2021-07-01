package svenhjol.charm.block;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.loader.CharmCommonModule;

public abstract class CharmLogBlock extends RotatedPillarBlock implements ICharmBlock {
    private final CharmCommonModule module;

    public CharmLogBlock(CharmCommonModule module, String name, Properties settings) {
        super(settings);
        this.register(module, name);
        this.module = module;
        this.setFireInfo(5, 20);
        this.setBurnTime(300);
    }

    public CharmLogBlock(CharmCommonModule module, String name, MaterialColor fromTop, MaterialColor fromSide) {
        this(module, name, BlockBehaviour.Properties.of(Material.WOOD, state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? fromTop : fromSide)
            .strength(2.0F)
            .sound(SoundType.WOOD));
    }

    public CharmLogBlock(CharmCommonModule module, String name, MaterialColor color) {
        this(module, name, BlockBehaviour.Properties.of(Material.WOOD, color)
            .strength(2.0F)
            .sound(SoundType.WOOD));
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
