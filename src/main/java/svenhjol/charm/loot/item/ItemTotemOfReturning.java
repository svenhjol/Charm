package svenhjol.charm.loot.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.loot.entry.TotemOfReturningEntry;
import svenhjol.charm.loot.feature.TotemOfReturning;
import svenhjol.meson.IMesonItem;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.SoundHelper;

import javax.annotation.Nonnull;

public class ItemTotemOfReturning extends Item implements IMesonItem
{
    public ItemTotemOfReturning()
    {
        register("totem_of_returning");
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        TotemOfReturningEntry entry = getEntry(stack);
        return entry != null && entry.pos != null;
    }

    public TotemOfReturningEntry getEntry(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null) {
            return TotemOfReturningEntry.read(tag);
        }

        return null;
    }

    public void setEntry(ItemStack stack, int uses)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
        }

        TotemOfReturningEntry.write(tag, uses);
        stack.setTagCompound(tag);
    }

    public void setEntry(ItemStack stack, BlockPos pos, int dimension, int uses)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
        }

        TotemOfReturningEntry.write(tag, pos, dimension, uses);

        stack.setTagCompound(tag);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos; // the position to teleport to
        int dimension; // the dimension to teleport to
        int uses; // default number of times the totem can be used

        TotemOfReturningEntry entry = getEntry(stack);

        // if shift is held, or entry isn't set, then bind this totem to current location and exit
        if (entry == null || entry.pos == null || player.isSneaking()) {
            uses = entry != null ? entry.uses : TotemOfReturning.numberOfUses;
            setEntry(stack, player.getPosition(), player.dimension, uses);
            SoundHelper.playSoundAtPos(world, player.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.8f);
            return super.onItemRightClick(world, player, hand);
        }

        // get the position the totem is bound to
        pos = entry.pos;
        dimension = entry.dimension;
        uses = entry.uses;

        PlayerHelper.teleportPlayer(player, pos, dimension);

        if (uses > 1) {
            setEntry(stack, --uses);
            SoundHelper.playSoundAtPos(world, pos, SoundEvents.ENTITY_SHULKER_TELEPORT, 0.8f, 1.0f);
        } else {
            stack.shrink(1);
            SoundHelper.playSoundAtPos(world, pos, SoundEvents.ITEM_TOTEM_USE, 0.8f, 1.0f);
        }

        return super.onItemRightClick(world, player, hand);
    }
}