package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.loader.CharmModule;

public abstract class CharmSignBlock extends StandingSignBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmSignBlock(CharmModule module, String name, WoodType signType, Properties settings) {
        super(settings, signType);

        this.register(module, name);
        this.module = module;
        this.setBurnTime(200);
    }

    public CharmSignBlock(CharmModule module, String name, WoodType signType, MaterialColor color) {
        this(module, name, signType, Properties.of(Material.WOOD, color)
            .noCollission()
            .strength(1.0F)
            .sound(SoundType.WOOD));
    }

    @Override
    public void createBlockItem(ResourceLocation id, Item.Properties properties) {
        // register sign item manually
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
