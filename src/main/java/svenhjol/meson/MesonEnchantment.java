package svenhjol.meson;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public abstract class MesonEnchantment extends Enchantment implements IMesonEnchantment
{
    public MesonEnchantment(String name, Rarity rarity, EnumEnchantmentType type, EntityEquipmentSlot... slots)
    {
        super(rarity, type, slots);
        this.register(name);
    }

    @Override
    public String getName()
    {
        return getModId() + "." + super.getName();
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        if (this instanceof ICurse) return 25;
        return super.getMinEnchantability(enchantmentLevel);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        if (this instanceof ICurse) return 50;
        return super.getMaxEnchantability(enchantmentLevel);
    }

    @Override
    public int getMaxLevel()
    {
        if (this instanceof ICurse) return 1;
        return super.getMaxLevel();
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        if (this instanceof ICurse) return true;
        return super.isTreasureEnchantment();
    }

    @Override
    public boolean isCurse()
    {
        if (this instanceof ICurse) return true;
        return super.isCurse();
    }

    public void onBreak(EntityPlayer player, BreakEvent event)
    {
        // no op
    }

    public void onBreakDrop(EntityPlayer player, HarvestDropsEvent event)
    {
        // no op
    }

    public void onAttack(EntityPlayer player, AttackEntityEvent event)
    {
        // no op
    }

    public void onDamage(EntityPlayer player, LivingDamageEvent event)
    {
        // no op
    }

    public void onInteract(EntityPlayer player, PlayerInteractEvent.RightClickBlock event)
    {
        // no op
    }

    public void onItemUseStop(EntityPlayer player, LivingEntityUseItemEvent.Stop event)
    {
        // no op
    }
}