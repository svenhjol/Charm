package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
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

public class EnchantmentHarmingCurse extends MesonEnchantment implements ICurse
{
    private int amount;
    private double chance;

    public EnchantmentHarmingCurse()
    {
        super("harming_curse", Rarity.VERY_RARE, EnumEnchantmentType.ALL, EntityEquipmentSlot.MAINHAND);
        this.amount = ExtraCurses.harmingDamageAmount;
        this.chance = ExtraCurses.harmingDamageChance;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void onBreak(EntityPlayer player, BlockEvent.BreakEvent event)
    {
        if (this.isHeldItemEnchanted(player, this)) {
            this.tryDamage(player);
        }
    }

    @Override
    public void onAttack(EntityPlayer player, AttackEntityEvent event)
    {
        if (this.isHeldItemEnchanted(player, this)) {
            this.tryDamage(player);
        }
    }

    @Override
    public void onDamage(EntityPlayer player, LivingDamageEvent event)
    {
        for (ItemStack item : player.getEquipmentAndArmor()) {
            if (EnchantmentHelper.hasEnchantment(this, item)) {
                this.tryDamage(player);
            }
        }
    }

    @Override
    public void onInteract(EntityPlayer player, PlayerInteractEvent.RightClickBlock event)
    {
        if (this.isHeldItemEnchanted(player, this)) {
            this.tryDamage(player);
        }
    }

    @Override
    public void onItemUseStop(EntityPlayer player, LivingEntityUseItemEvent.Stop event)
    {
        if (this.isHeldItemEnchanted(player, this)) {
            this.tryDamage(player);
        }
    }

    private void tryDamage(EntityPlayer player)
    {
        if (player.world.rand.nextFloat() < this.chance) {
            player.attackEntityFrom(DamageSource.MAGIC, amount);
        }
    }
}
