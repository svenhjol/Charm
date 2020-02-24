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
import svenhjol.charm.enchanting.module.Salvage;
import svenhjol.charm.tweaks.client.AmbientMusicClient;
import svenhjol.charm.tweaks.module.*;
import svenhjol.meson.Meson;
import svenhjol.meson.event.ComposterEvent;
import svenhjol.meson.helper.WorldHelper;

import javax.annotation.Nullable;

public class CharmAsmHooks
{
    public static boolean checkBrewingStandStack(ItemStack stack)
    {
        return Meson.isModuleEnabled("charm:stackable_potions");
    }

    public static int getMinimumRepairCost()
    {
        return Meson.isModuleEnabled("charm:no_anvil_minimum_xp") ? -1 : 0;
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack stack)
    {
        if (!Meson.isModuleEnabled("charm:leather_armor_invisibility")) return false;
        return LeatherArmorInvisibility.isArmorInvisible(entity, stack);
    }

    public static void mobsInBeaconRange(World world, int levels, BlockPos pos, Effect primaryEffect, Effect secondaryEffect)
    {
        if (!Meson.isModuleEnabled("charm:mobs_affected_by_beacon")) return;
        MobsAffectedByBeacon.mobsInBeaconRange(world, levels, pos, primaryEffect, secondaryEffect);
    }

    public static boolean removePotionGlint()
    {
        return Meson.isModuleEnabled("charm:remove_potion_glint");
    }

    public static void itemDamaged(ItemStack stack, int amount, @Nullable ServerPlayerEntity player)
    {
        if (!Meson.isModuleEnabled("charm:salvage")) return;
        Salvage.itemDamaged(stack, amount, player);
    }

    public static boolean isSkyLightMax(IWorld world, BlockPos pos)
    {
        if (!Meson.isModuleEnabled("charm:husk_improvements") || !HuskImprovements.spawnAnywhere)
            return WorldHelper.canSeeSky(world, pos);

        return HuskImprovements.canSeeSky(world, pos);
    }

    public static void composterOutput(World world, BlockPos pos, PlayerEntity player)
    {
        MinecraftForge.EVENT_BUS.post(new ComposterEvent.Output(world, pos, player));
    }

    public static boolean handleMusicTick(ISound currentMusic)
    {
        if (!Meson.isModuleEnabled("charm:ambient_music_improvements")) return false;
        return AmbientMusicClient.handleTick(currentMusic);
    }

    public static boolean handleMusicStop()
    {
        if (!Meson.isModuleEnabled("charm:ambient_music_improvements")) return false;
        return AmbientMusicClient.handleStop();
    }

    public static boolean handleMusicPlaying(MusicType type)
    {
        if (!Meson.isModuleEnabled("charm:ambient_music_improvements")) return false;
        return AmbientMusicClient.handlePlaying(type);
    }
}