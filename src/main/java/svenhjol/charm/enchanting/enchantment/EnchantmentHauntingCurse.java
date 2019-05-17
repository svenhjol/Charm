package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonEnchantment.*;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.charm.enchanting.feature.ExtraCurses;

public class EnchantmentHauntingCurse extends MesonEnchantment implements ICurse
{
    private double spawnChance;
    private int spawnRange;
    private String[] spawnableMobs;

    public EnchantmentHauntingCurse()
    {
        super("haunting_curse", Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, EntityEquipmentSlot.MAINHAND);
        this.spawnChance = ExtraCurses.hauntingSpawnChance;
        this.spawnRange = ExtraCurses.hauntingSpawnRange;
        this.spawnableMobs = ExtraCurses.hauntingMobs;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void onBreak(EntityPlayer player, BlockEvent.BreakEvent event)
    {
        this.trySpawn(player);
    }

    @Override
    public void onInteract(EntityPlayer player, PlayerInteractEvent.RightClickBlock event)
    {
        this.trySpawn(player);
    }

    @Override
    public void onAttack(EntityPlayer player, AttackEntityEvent event)
    {
        this.trySpawn(player);
    }

    private void trySpawn(EntityPlayer player)
    {
        if (this.isHeldItemEnchanted(player, this)
            && player.world.rand.nextFloat() < spawnChance
        ) {
            String mob = spawnableMobs[player.world.rand.nextInt(spawnableMobs.length)];
            EntityHelper.spawnEntityNearPlayer(player, spawnRange, new ResourceLocation(mob));
        }
    }
}
