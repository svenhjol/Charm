package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import svenhjol.charm.Charm;
import svenhjol.charm.enchanting.feature.ExtraCurses;
import svenhjol.meson.iface.IMesonEnchantment.ICurse;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.helper.SoundHelper;

public class EnchantmentClumsinessCurse extends MesonEnchantment implements ICurse
{
    private double clumsiness;

    public EnchantmentClumsinessCurse()
    {
        super("clumsiness_curse", Rarity.VERY_RARE, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.MAINHAND);
        this.clumsiness = ExtraCurses.clumsinessMissChance;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void onBreakDrop(EntityPlayer player, BlockEvent.HarvestDropsEvent event)
    {
        if (this.isHeldItemEnchanted(player, this)
                && player.world.rand.nextFloat() < clumsiness
        ) {
            event.setDropChance(0.0f);
        }
    }

    @Override
    public void onAttack(EntityPlayer player, AttackEntityEvent event)
    {
        this.tryMiss(player, event);
    }

    @Override
    public void onInteract(EntityPlayer player, PlayerInteractEvent.RightClickBlock event)
    {
        this.tryMiss(player, event);
    }

    @Override
    public void onItemUseStop(EntityPlayer player, LivingEntityUseItemEvent.Stop event)
    {
        this.tryMiss(player, event);
    }

    private void tryMiss(EntityPlayer player, Event event)
    {
        if (this.isHeldItemEnchanted(player, this)
            && player.world.rand.nextFloat() < clumsiness
        ) {
            event.setCanceled(true);
            SoundHelper.playerSound(player, SoundEvents.ENTITY_ITEM_BREAK, 0.5f, 1.5f, 0.15f, null);
        }
    }
}
