package svenhjol.charm.loot.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.loot.feature.TotemOfReturning;
import svenhjol.meson.IMesonItem;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.SoundHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTotemOfReturning extends Item implements IMesonItem
{
    private static final String POS = "pos";
    private static final String DIM = "dim";
    private static final String USES = "uses";

    public ItemTotemOfReturning()
    {
        register("totem_of_returning");
        setCreativeTab(CreativeTabs.TRANSPORTATION);
        setMaxStackSize(1);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return getPos(stack) != null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos = getPos(stack); // the position to teleport to
        int dim = getDim(stack); // the dimension to teleport to
        int uses = getUses(stack); // default number of times the totem can be used

        // if shift is held, or entry isn't set, then bind this totem to current location and exit
        if (pos == null || player.isSneaking()) {
            BlockPos playerPos = player.getPosition();
            int playerDim = player.dimension;
            setPos(stack, playerPos);
            setDim(stack, playerDim);

            SoundHelper.playSoundAtPos(world, playerPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.8f);
            return super.onItemRightClick(world, player, hand);
        }

        PlayerHelper.teleportPlayer(player, pos, dim);

        if (uses > 1) {
            setUses(stack, uses - 1);
            SoundHelper.playSoundAtPos(world, pos, SoundEvents.ENTITY_SHULKER_TELEPORT, 0.8f, 1.0f);
        } else {
            stack.shrink(1);
            SoundHelper.playSoundAtPos(world, pos, SoundEvents.ITEM_TOTEM_USE, 0.8f, 1.0f);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        int uses = getUses(stack);
        TextFormatting color = uses == 1 ? TextFormatting.RED : TextFormatting.GREEN;
        tooltip.add(color + I18n.format("number_of_uses") + ": " + uses);

        BlockPos pos = getPos(stack);
        if (pos != null) {
            String x = String.valueOf(pos.getX());
            String y = String.valueOf(pos.getY());
            String z = String.valueOf(pos.getZ());
            String dim = String.valueOf(getDim(stack));
            tooltip.add(TextFormatting.BLUE + x + " " + y + " " + z + ", " + I18n.format("dimension") + " " + dim);
        }
    }

    public static void setPos(ItemStack stack, BlockPos pos)
    {
        ItemNBTHelper.setLong(stack, POS, pos.toLong());
    }

    public static void setDim(ItemStack stack, int dim)
    {
        ItemNBTHelper.setInt(stack, DIM, dim);
    }

    public static void setUses(ItemStack stack, int uses)
    {
        ItemNBTHelper.setInt(stack, USES, uses);
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

    public static int getUses(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, USES, TotemOfReturning.numberOfUses);
    }
}