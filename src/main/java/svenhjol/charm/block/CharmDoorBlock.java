package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.loader.CharmModule;

public abstract class CharmDoorBlock extends DoorBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmDoorBlock(CharmModule module, String name, Properties settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
        this.setBurnTime(200);
    }

    public CharmDoorBlock(CharmModule module, String name, Block block) {
        this(module, name, Properties.of(Material.WOOD, block.defaultMaterialColor())
            .strength(3.0F)
            .sound(SoundType.WOOD)
            .noOcclusion());
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
