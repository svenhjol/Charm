package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;

public class CharmPressurePlate extends PressurePlateBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmPressurePlate(CharmModule module, String name, PressurePlateBlock.ActivationRule activationRule, Settings settings) {
        super(activationRule, settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmPressurePlate(CharmModule module, String name, Block block) {
        this(module, name, ActivationRule.EVERYTHING, Settings.of(Material.WOOD, block.getDefaultMapColor())
            .noCollision()
            .strength(0.5F)
            .sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.addStacksForDisplay(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
