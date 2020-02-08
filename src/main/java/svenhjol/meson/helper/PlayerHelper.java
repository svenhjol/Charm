package svenhjol.meson.helper;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PlayerHelper
{
    /**
     * Tries to add item stack to player, drops if not possible.
     *
     * @param player The player
     * @param stack  The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(PlayerEntity player, ItemStack stack)
    {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
            return false;
        }
        return true;
    }

    public static void damageHeldItem(PlayerEntity player, Hand hand, ItemStack stack, int damage)
    {
        stack.damageItem(damage, player, (p) -> {
            player.sendBreakAnimation(hand);
        });
    }

    public static ImmutableList<NonNullList<ItemStack>> getInventories(PlayerEntity player)
    {
        PlayerInventory inventory = player.inventory;
        return ImmutableList.of(inventory.mainInventory, inventory.armorInventory, inventory.offHandInventory);
    }

    public static void doLightningNearPlayer(PlayerEntity player)
    {
        int dist = 24;
        World world = player.world;
        Random rand = world.rand;

        if (!world.isSkyLightMax(player.getPosition())) return;

        BlockPos pos = player.getPosition().add(-(dist/2) + rand.nextInt(dist), 0, -(dist/2) + rand.nextInt(dist));
        ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, false));
    }

    public static void setHeldItem(PlayerEntity player, Hand hand, ItemStack item)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() == 1) {
            player.setHeldItem(hand, item);
        } else {
            stack.shrink(1);
            if (stack.getCount() == 0) {
                player.setHeldItem(hand, item);
            } else {
                addOrDropStack(player, item);
            }
        }
    }

    public static boolean spawnEntityNearPlayer(PlayerEntity player, MobEntity mob, BiConsumer<MobEntity, BlockPos> onSpawn)
    {
        boolean spawned = false;
        int range = 8;
        int tries = 8;
        BlockPos pp = player.getPosition();
        World world = player.world;
        Random rand = world.rand;
        List<BlockPos> valid = new ArrayList<>();

        for (int y = pp.getY() + range*2; y > 0 ; y--) {
            for (int i = range; i > 1; i--) {
                for (int c = 1; c < tries; c++) {
                    BlockPos p = new BlockPos(pp.getX() + rand.nextInt(i), y, pp.getZ() + rand.nextInt(i));
                    BlockPos floor = p.down();
                    BlockPos above = p.up();

                    boolean areaIsValid = (WorldHelper.isSolidishBlock(world, floor))
                        && (WorldHelper.isAirBlock(world, p))
                        && (WorldHelper.isAirBlock(world, p));

                    if (areaIsValid) valid.add(above);
                    if (valid.size() > 2) break;
                }
            }
        }

        if (valid.isEmpty()) return false;

        BlockPos spawnPos = valid.get(rand.nextInt(valid.size()));
        mob.moveToBlockPosAndAngles(spawnPos, 0.0F, 0.0F);
        mob.onInitialSpawn(world, world.getDifficultyForLocation(spawnPos), SpawnReason.TRIGGERED, null, null);
        world.addEntity(mob);
        onSpawn.accept(mob, spawnPos);
        return true;
    }

    /**
     * Basic way to teleport a player to co-ordinate in a dimension.
     * If the player is not in the specified dimension they will be transferred first.
     */
    public static void teleport(PlayerEntity player, BlockPos pos, int dim)
    {
        teleport(player, pos, dim, p -> {});
    }

    public static void teleport(PlayerEntity player, BlockPos pos, int dim, Consumer<PlayerEntity> onTeleport)
    {
        World world = player.world;
        if (world.isRemote) return;

        changeDimension(player, dim);

       // ((ServerPlayerEntity)player).teleport((ServerWorld)world, pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
        player.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);

        BlockPos playerPos = player.getPosition();

        while (world.isAirBlock(playerPos.down()) && playerPos.getY() > 0) {
            playerPos = playerPos.down();
        }

        while (world.getBlockState(playerPos).isSolid() || world.getBlockState(playerPos.up()).isSolid() && playerPos.getY() < 256) {
            playerPos = playerPos.up();
        }

        player.setPositionAndUpdate(playerPos.getX(), playerPos.getY(), playerPos.getZ());

        onTeleport.accept(player);
    }

    public static void teleportSurface(PlayerEntity player, BlockPos pos, int dim)
    {
        teleportSurface(player, pos, dim, p -> {});
    }

    public static void teleportSurface(PlayerEntity player, BlockPos pos, int dim, Consumer<PlayerEntity> onTeleport)
    {
        World world = player.world;
        if (world.isRemote) return;

        teleport(player, pos, dim, (p) -> {
            for (int y = p.world.getHeight(); y > 0; y--) {
                BlockPos pp = new BlockPos(p.getPosition().getX(), y, p.getPosition().getZ());
                if (p.world.isAirBlock(pp)
                    && !p.world.isAirBlock(pp.down())
                ) {
                    p.setPositionAndUpdate(pp.getX(), y, pp.getZ());
                    onTeleport.accept(p);
                    break;
                }
            }
        });
    }

    public static void changeDimension(PlayerEntity player, int dim)
    {
        if (player.world.isRemote) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        DimensionType destination = DimensionType.getById(dim);
        if (destination == null) return;
        if (destination.getId() == player.dimension.getId()) return;

        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, destination)) return;

        DimensionType dimensiontype = serverPlayer.dimension;
        ServerWorld serverworld = serverPlayer.server.func_71218_a(dimensiontype);
        serverPlayer.dimension = destination;
        ServerWorld serverworld1 = serverPlayer.server.func_71218_a(destination);
        WorldInfo worldinfo = serverPlayer.world.getWorldInfo();
        net.minecraftforge.fml.network.NetworkHooks.sendDimensionDataPacket(serverPlayer.connection.netManager, serverPlayer);

        serverPlayer.connection.sendPacket(new SRespawnPacket(destination, worldinfo.getGenerator(), serverPlayer.interactionManager.getGameType()));
        serverPlayer.connection.sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));

        PlayerList playerlist = serverPlayer.server.getPlayerList();
        playerlist.updatePermissionLevel(serverPlayer);
        serverworld.removeEntity(serverPlayer, true); //Forge: the player entity is moved to the new world, NOT cloned. So keep the data alive with no matching invalidate call.
        serverPlayer.revive();

        double d0 = serverPlayer.posX;
        double d1 = serverPlayer.posY;
        double d2 = serverPlayer.posZ;
        float f = serverPlayer.rotationPitch;
        float f1 = serverPlayer.rotationYaw;
        float f2 = f1;

        serverworld.getProfiler().startSection("moving");
        double moveFactor = serverworld.getDimension().getMovementFactor() / serverworld1.getDimension().getMovementFactor();
        d0 *= moveFactor;
        d2 *= moveFactor;
        serverPlayer.setLocationAndAngles(d0, d1, d2, f1, f);
        serverworld.getProfiler().endSection();

        serverworld.getProfiler().startSection("placing");
        double d7 = Math.min(-2.9999872E7D, serverworld1.getWorldBorder().minX() + 16.0D);
        double d4 = Math.min(-2.9999872E7D, serverworld1.getWorldBorder().minZ() + 16.0D);
        double d5 = Math.min(2.9999872E7D, serverworld1.getWorldBorder().maxX() - 16.0D);
        double d6 = Math.min(2.9999872E7D, serverworld1.getWorldBorder().maxZ() - 16.0D);
        d0 = MathHelper.clamp(d0, d7, d5);
        d2 = MathHelper.clamp(d2, d4, d6);
        serverPlayer.setLocationAndAngles(d0, d1, d2, f1, f);
        serverworld.getProfiler().endSection();

        serverPlayer.setWorld(serverworld1);
        serverworld1.func_217447_b(serverPlayer);
        serverPlayer.connection.setPlayerLocation(serverPlayer.posX, serverPlayer.posY, serverPlayer.posZ, f1, f);
        serverPlayer.interactionManager.func_73080_a(serverworld1);
        serverPlayer.connection.sendPacket(new SPlayerAbilitiesPacket(serverPlayer.abilities));
        playerlist.func_72354_b(serverPlayer, serverworld1);
        playerlist.sendInventory(serverPlayer);

        for(EffectInstance effectinstance : serverPlayer.getActivePotionEffects()) {
            serverPlayer.connection.sendPacket(new SPlayEntityEffectPacket(serverPlayer.getEntityId(), effectinstance));
        }

        serverPlayer.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
        net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerChangedDimensionEvent(serverPlayer, dimensiontype, destination);
    }
}
