package svenhjol.charm.world.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonItem;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.PlayerHelper;

public class TotemOfReturningItem extends MesonItem
{
    private static final String POS = "pos";
    private static final String DIM = "dim";

    /* @todo Tooltip information */

    public TotemOfReturningItem()
    {
        super(new Item.Properties()
            .group(ItemGroup.TRANSPORTATION)
            .rarity(Rarity.UNCOMMON)
            .maxStackSize(1)
        );
        setRegistryName(new ResourceLocation(Charm.MOD_ID, "totem_of_returning"));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return getPos(stack) != null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos = getPos(stack); // the position to teleport to
        int dim = getDim(stack); // the dimension to teleport to

        // if shift is held, or entry isn't set, then bind this totem to current location and exit
        if (pos == null || player.isSneaking()) {
            BlockPos playerPos = player.getPosition();
            int playerDim = player.dimension.getId();
            setPos(stack, playerPos);
            setDim(stack, playerDim);

            player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.8f);
            return super.onItemRightClick(world, player, hand);
        }

        // teleport the player
        PlayerHelper.teleportPlayer(player, pos, dim);
        stack.shrink(1);
        world.playSound(player, pos, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.8f, 1.0f);
        return super.onItemRightClick(world, player, hand);
    }

    public static BlockPos getPos(ItemStack stack)
    {
        long pos = ItemNBTHelper.getLong(stack, POS, 0);
        return pos == 0 ? null : BlockPos.fromLong(pos);
    }

    public static int getDim(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, DIM, 0);
    }

    public static void setPos(ItemStack stack, BlockPos pos)
    {
        ItemNBTHelper.setLong(stack, POS, pos.toLong());
    }

    public static void setDim(ItemStack stack, int dim)
    {
        ItemNBTHelper.setInt(stack, DIM, dim);
    }
}
