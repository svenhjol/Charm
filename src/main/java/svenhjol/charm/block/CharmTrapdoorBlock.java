package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.module.CharmModule;

public class CharmTrapdoorBlock extends TrapDoorBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmTrapdoorBlock(CharmModule module, String name, Properties settings) {
        super(settings);
        this.register(module, name);
        this.module = module;
        this.setBurnTime(300);
    }

    public CharmTrapdoorBlock(CharmModule module, String name, MaterialColor color) {
        this(module, name, Properties.of(Material.WOOD, color)
            .strength(3.0F)
            .noOcclusion()
            .isValidSpawn((state, world, pos, type) -> false)
            .sound(SoundType.WOOD));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
