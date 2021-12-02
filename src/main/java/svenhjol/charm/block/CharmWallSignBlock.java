package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.loader.CharmModule;

public abstract class CharmWallSignBlock extends WallSignBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmWallSignBlock(CharmModule module, String name, WoodType signType, Properties settings) {
        super(settings, signType);

        this.register(module, name);
        this.module = module;
    }

    public CharmWallSignBlock(CharmModule module, String name, CharmSignBlock block, WoodType signType, MaterialColor color) {
        this(module, name, signType, Properties.of(Material.WOOD, color)
            .noCollission()
            .strength(1.0F)
            .sound(SoundType.WOOD)
            .dropsLike(block));
    }

    @Override
    public void createBlockItem(ResourceLocation id) {
        // no, because infinite loop. No need for item here
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
