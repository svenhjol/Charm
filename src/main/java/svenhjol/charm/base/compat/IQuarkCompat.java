package svenhjol.charm.base.compat;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import svenhjol.meson.enums.ColorVariant;

import javax.annotation.Nullable;
import java.util.Random;

public interface IQuarkCompat {
    boolean hasColorRuneModule();

    boolean isRune(ItemStack stack);

    ColorVariant getRuneColor(ItemStack stack);

    ItemStack getRune(ColorVariant color);

    ItemStack getQuiltedWool(ColorVariant color);

    void applyColor(ItemStack stack, DyeColor color);

    ItemStack getRandomAncientTome(Random rand);

    boolean isInsideBigDungeon(ServerWorld world, BlockPos pos);

    boolean isCrystal(World world, BlockPos pos);

    boolean hasVariantChests();

    boolean hasBigDungeons();

    String getBigDungeonResName();

    Structure<?> getBigDungeonStructure();

    @Nullable
    Block getRandomChest(Random rand);
}
