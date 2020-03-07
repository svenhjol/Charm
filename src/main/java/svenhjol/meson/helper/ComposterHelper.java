package svenhjol.meson.helper;

import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unused")
public class ComposterHelper
{
    public static void addInputItem(Item item, float chance)
    {
        ComposterBlock.CHANCES.put(item, chance);
    }

    public static void removeInputItem(Item item)
    {
        ComposterBlock.CHANCES.removeFloat(item);
    }

    public static float getChance(Item item)
    {
        AtomicReference<Float> out = new AtomicReference<>(0F);
        ComposterBlock.CHANCES.forEach((i, c) -> {
            if (item.equals(i)) {
                out.set(c);
            }
        });
        return out.get();
    }

    public static void spawnOutput(World world, BlockPos pos, ItemStack stack)
    {
        if (!world.isRemote) {
            double d0 = (double) (world.rand.nextFloat() * 0.7F) + (double) 0.15F;
            double d1 = (double) (world.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
            double d2 = (double) (world.rand.nextFloat() * 0.7F) + (double) 0.15F;
            ItemEntity itementity = new ItemEntity(world, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
            itementity.setDefaultPickupDelay();
            world.addEntity(itementity);
        }
    }
}
