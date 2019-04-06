package svenhjol.charm.world.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.meson.IMesonItem;
import svenhjol.charm.world.entity.EntityChargedEmerald;

public class ItemChargedEmerald extends Item implements IMesonItem
{
    public ItemChargedEmerald()
    {
        register("charged_emerald");
        setMaxStackSize(16);
        setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        if (!worldIn.isRemote) {
            EntityChargedEmerald emerald = new EntityChargedEmerald(worldIn, playerIn);
            emerald.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f);
            worldIn.spawnEntity(emerald);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}