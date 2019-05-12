package svenhjol.charm.world.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import svenhjol.meson.decorator.MesonDecoratorItems;
import svenhjol.meson.decorator.MesonInnerDecorator;
import vazkii.quark.decoration.entity.EntityGlassItemFrame;

import javax.annotation.Nullable;

public class QuarkDecoratorItems extends MesonDecoratorItems
{
    public QuarkDecoratorItems(MesonInnerDecorator generator)
    {
        super(generator);
    }

    public void addGlassFramedItem(int x, int y, int z, EnumFacing direction, @Nullable ItemStack displayedItem)
    {
        BlockPos place = generator.getRelativePos(new BlockPos(x, y, z));
        if (generator.isNotInBox(place)) return;

        EntityGlassItemFrame frame = new EntityGlassItemFrame(world, place, generator.getRelativeFacing(direction));

        if (displayedItem != null) {
            frame.setDisplayedItem(displayedItem);
        }
        world.spawnEntity(frame);
    }
}
