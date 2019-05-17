package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonEnchantment.*;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.charm.enchanting.feature.ExtraCurses;

import java.util.Random;

public class EnchantmentRustingCurse extends MesonEnchantment implements ICurse
{
    private int rustDamage;

    public EnchantmentRustingCurse()
    {
        super("rusting_curse", Rarity.VERY_RARE, EnumEnchantmentType.ALL, EntityEquipmentSlot.MAINHAND);
        rustDamage = ExtraCurses.rustingDamage;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void onBreak(EntityPlayer player, BlockEvent.BreakEvent event)
    {
        if (this.isHeldItemEnchanted(player, this, EnumHand.MAIN_HAND)) {
            this.damageItem(player, player.getHeldItemMainhand());
        }
        if (this.isHeldItemEnchanted(player, this, EnumHand.OFF_HAND)) {
            this.damageItem(player, player.getHeldItemOffhand());
        }
    }

    @Override
    public void onInteract(EntityPlayer player, PlayerInteractEvent.RightClickBlock event)
    {
        if (this.isHeldItemEnchanted(player, this, EnumHand.MAIN_HAND)) {
            this.damageItem(player, player.getHeldItemMainhand());
        }
        if (this.isHeldItemEnchanted(player, this, EnumHand.OFF_HAND)) {
            this.damageItem(player, player.getHeldItemOffhand());
        }
    }

    @Override
    public void onAttack(EntityPlayer player, AttackEntityEvent event)
    {
        if (this.isHeldItemEnchanted(player, this, EnumHand.MAIN_HAND)) {
            this.damageItem(player, player.getHeldItemMainhand());
        }
        if (this.isHeldItemEnchanted(player, this, EnumHand.OFF_HAND)) {
            this.damageItem(player, player.getHeldItemOffhand());
        }
    }

    @Override
    public void onItemUseStop(EntityPlayer player, LivingEntityUseItemEvent.Stop event)
    {
        if (this.isHeldItemEnchanted(player, this, EnumHand.MAIN_HAND)) {
            this.damageItem(player, player.getHeldItemMainhand());
        }
    }

    @Override
    public void onDamage(EntityPlayer player, LivingDamageEvent event)
    {
        for (ItemStack item : player.getEquipmentAndArmor()) {
            if (EnchantmentHelper.hasEnchantment(this, item)) {
                this.damageItem(player, item);
            }
        }
    }

    private void damageItem(EntityPlayer player, ItemStack item)
    {
        item.attemptDamageItem(new Random().nextInt(rustDamage), player.world.rand, (EntityPlayerMP) player);
    }
}
