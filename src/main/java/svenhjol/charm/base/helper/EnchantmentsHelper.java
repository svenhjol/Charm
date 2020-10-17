package svenhjol.charm.base.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantmentsHelper {
    /**
     * Add blocks to this list that should provide enchanting power to an enchanting table.
     */
    public static List<Block> ENCHANTING_BLOCKS = new ArrayList<>(Arrays.asList(Blocks.BOOKSHELF));

    public static boolean canBlockPowerEnchantingTable(BlockState state) {
        return ENCHANTING_BLOCKS.stream().anyMatch(b -> b == state.getBlock());
    }

    public static boolean hasFeatherFalling(LivingEntity entity) {
        return EnchantmentHelper.getEquipmentLevel(Enchantments.FEATHER_FALLING, entity) > 0;
    }
}
