package svenhjol.charm.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import svenhjol.charm.Charm;
import svenhjol.charm.enchanting.module.Salvage;
import svenhjol.charm.tweaks.module.*;
import svenhjol.meson.event.ComposterEvent;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class CharmAsmHooks
{
    public static boolean checkBrewingStandStack(ItemStack stack)
    {
        return Charm.loader.hasModule(StackablePotions.class);
    }

    public static int getMinimumRepairCost()
    {
        return Charm.loader.hasModule(NoAnvilMinimumXp.class) ? -1 : 0;
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack stack)
    {
        if (!Charm.loader.hasModule(LeatherArmorInvisibility.class)) return false;
        return LeatherArmorInvisibility.isArmorInvisible(entity, stack);
    }

    public static void mobsInBeaconRange(World world, int levels, BlockPos pos, Effect primaryEffect, Effect secondaryEffect)
    {
        if (!Charm.loader.hasModule(MobsAffectedByBeacon.class)) return;
        MobsAffectedByBeacon.mobsInBeaconRange(world, levels, pos, primaryEffect, secondaryEffect);
    }

    public static boolean removePotionGlint()
    {
        return Charm.loader.hasModule(RemovePotionGlint.class);
    }

    public static void itemDamaged(ItemStack stack, int amount, @Nullable ServerPlayerEntity player)
    {
        if (!Charm.loader.hasModule(Salvage.class)) return;
        Salvage.itemDamaged(stack, amount, player);
    }

    public static boolean isSkyLightMax(IWorld world, BlockPos pos)
    {
        if (!Charm.loader.hasModule(HuskImprovements.class) || !HuskImprovements.spawnAnywhere) return world.isSkyLightMax(pos);
        return HuskImprovements.isSkyLightMax(world, pos);
    }

    public static void composterOutput(World world, BlockPos pos, PlayerEntity player)
    {
        MinecraftForge.EVENT_BUS.post(new ComposterEvent.Output(world, pos, player));
    }
}