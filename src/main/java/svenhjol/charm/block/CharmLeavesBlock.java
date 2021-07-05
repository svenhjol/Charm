package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.loader.CharmModule;

public abstract class CharmLeavesBlock extends LeavesBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmLeavesBlock(CharmModule module, String name, Properties settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
        this.setFireInfo(30, 60);
    }

    public CharmLeavesBlock(CharmModule module, String name) {
        this(module, name, Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn((state, world, pos, type) -> false)
            .isSuffocating((state, world, pos) -> false)
            .isViewBlocking((state, world, pos) -> false));
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
