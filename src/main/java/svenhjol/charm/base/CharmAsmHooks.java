package svenhjol.charm.base;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker.MusicType;
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
import svenhjol.charm.tweaks.client.AmbientMusicClient;
import svenhjol.charm.tweaks.module.*;
import svenhjol.meson.event.ComposterEvent;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class CharmAsmHooks
{
    public static boolean checkBrewingStandStack(ItemStack stack)
    {
        return Charm.hasModule(StackablePotions.class);
    }

    public static int getMinimumRepairCost()
    {
        return Charm.hasModule(NoAnvilMinimumXp.class) ? -1 : 0;
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack stack)
    {
        if (!Charm.hasModule(LeatherArmorInvisibility.class)) return false;
        return LeatherArmorInvisibility.isArmorInvisible(entity, stack);
    }

    public static void mobsInBeaconRange(World world, int levels, BlockPos pos, Effect primaryEffect, Effect secondaryEffect)
    {
        if (!Charm.hasModule(MobsAffectedByBeacon.class)) return;
        MobsAffectedByBeacon.mobsInBeaconRange(world, levels, pos, primaryEffect, secondaryEffect);
    }

    public static boolean removePotionGlint()
    {
        return Charm.hasModule(RemovePotionGlint.class);
    }

    public static void itemDamaged(ItemStack stack, int amount, @Nullable ServerPlayerEntity player)
    {
        if (!Charm.hasModule(Salvage.class)) return;
        Salvage.itemDamaged(stack, amount, player);
    }

    public static boolean isSkyLightMax(IWorld world, BlockPos pos)
    {
        if (!Charm.hasModule(HuskImprovements.class) || !HuskImprovements.spawnAnywhere) return world.isSkyLightMax(pos);
        return HuskImprovements.isSkyLightMax(world, pos);
    }

    public static void composterOutput(World world, BlockPos pos, PlayerEntity player)
    {
        MinecraftForge.EVENT_BUS.post(new ComposterEvent.Output(world, pos, player));
    }

    public static boolean handleMusicTick(ISound currentMusic)
    {
        if (!Charm.hasModule(AmbientMusicImprovements.class)) return false;
        return AmbientMusicClient.handleTick(currentMusic);
    }

    public static boolean handleMusicStop()
    {
        if (!Charm.hasModule(AmbientMusicImprovements.class)) return false;
        return AmbientMusicClient.handleStop();
    }

    public static boolean handleMusicPlaying(MusicType type)
    {
        if (!Charm.hasModule(AmbientMusicImprovements.class)) return false;
        return AmbientMusicClient.handlePlaying(type);
    }
}