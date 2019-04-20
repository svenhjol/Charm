package svenhjol.meson.helper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityHelper
{
    /**
     * From Quark's DepthMobs.java
     * @link {https://github.com/Vazkii/Quark/blob/master/src/main/java/vazkii/quark/world/feature/DepthMobs.java}
     */
    public static Biome[] getBiomesWithMob(Class<? extends Entity> clazz)
    {
        List<Biome> biomes = new ArrayList<>();
        for (Biome b : Biome.REGISTRY) {
            List<Biome.SpawnListEntry> spawnList = b.getSpawnableList(EnumCreatureType.MONSTER);
            for (Biome.SpawnListEntry e : spawnList)
                if (e.entityClass == clazz) {
                    biomes.add(b);
                    break;
                }
        }

        return biomes.toArray(new Biome[0]);
    }

    /**
     * From BetterWithMods PotionEventHandler.java
     * @link {https://github.com/BetterWithMods/BetterWithMods/blob/bf630aa1fade156ce8fae0d769ad745a4161b0ba/src/main/java/betterwithmods/event/PotionEventHandler.java}
     */
    public static boolean canMobsSpawnInPos(World world, BlockPos pos)
    {
        if (world.isSideSolid(pos.down(), EnumFacing.UP) && !world.isBlockNormalCube(pos, false)
                && !world.isBlockNormalCube(pos.up(), false) && !world.getBlockState(pos).getMaterial().isLiquid()) {
            IBlockState state = world.getBlockState(pos);

            int lightLevel = world.getLightFor(EnumSkyBlock.BLOCK, pos);
            return lightLevel < 8 && (world.isAirBlock(pos) || state.getCollisionBoundingBox(world, pos) == null);
        }

        return false;
    }

    public static boolean itemHasEntityTag(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        return (tag != null && tag.hasKey("EntityTag") && tag.getCompoundTag("EntityTag").hasKey("id"));
    }

    public static void spawnEntityFromItem(ItemStack stack, World world, BlockPos pos)
    {
        if (world.isRemote) return;
        if (!itemHasEntityTag(stack)) return;

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return;

        String s = tag.getCompoundTag("EntityTag").getString("id");
        ResourceLocation id = new ResourceLocation(s);

        if (EntityList.ENTITY_EGGS.containsKey(id)) {
            Entity entityFromId = EntityList.createEntityByIDFromName(id, world);
            Entity entity = spawnEntity(entityFromId, world, pos);
            if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                entity.setCustomNameTag(stack.getDisplayName());
            }
        }
    }

    public static void spawnEntityItem(World world, BlockPos pos, ItemStack stack)
    {
        EntityItem item = new EntityItem(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, stack);
        world.spawnEntity(item);
    }

    public static void spawnEntityNearPlayer(EntityPlayer player, int distance, ResourceLocation entityName)
    {
        World world = player.world;
        Entity entity = EntityList.createEntityByIDFromName(entityName, world);

        if (entity instanceof EntityLiving) {
            ArrayList<BlockPos> shuffled = new ArrayList<>();

            Iterable<BlockPos> positions = BlockPos.getAllInBox(player.getPosition().add(-distance, -distance, -distance), player.getPosition().add(distance, distance, distance));
            positions.forEach(shuffled::add);

            Collections.shuffle(shuffled);

            for (BlockPos p : shuffled) {
                if (canMobsSpawnInPos(world, p)) {
                    spawnEntity(entity, world, new BlockPos(p.getX() + 0.5f, p.getY(), p.getZ() + 0.5f));
                    break;
                }
            }
        }
    }

    @Nullable
    public static Entity spawnEntity(Entity entity, World world, BlockPos pos)
    {
        if (entity == null) return null;

        entity.setLocationAndAngles(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);

        if (entity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) entity;
            living.rotationYawHead = living.rotationYaw;
            living.renderYawOffset = living.rotationYaw;
            living.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(living)), null);
            world.spawnEntity(living);
            living.playLivingSound();
            return living;
        } else {
            world.spawnEntity(entity);
            return entity;
        }
    }

    public static Entity spawnEntity(ResourceLocation name, World world, BlockPos pos)
    {
        Entity entity = EntityList.createEntityByIDFromName(name, world);
        if (entity == null) return null;

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
        return spawnEntity(entity, world, new BlockPos(x, y, z));
    }

    /**
     * From CoFH EntityHelper::transferEntityToWorld
     * @link {https://github.com/CoFH/CoFHCore/blob/1.12/src/main/java/cofh/core/util/helpers/EntityHelper.java}
     */
    public static void transferEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld)
    {
        WorldProvider pOld = oldWorld.provider;
        WorldProvider pNew = newWorld.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double x = entity.posX * moveFactor;
        double z = entity.posZ * moveFactor;

        oldWorld.profiler.startSection("placing");
        x = MathHelper.clamp(x, -29999872, 29999872);
        z = MathHelper.clamp(z, -29999872, 29999872);

        if (entity.isEntityAlive()) {
            entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
            newWorld.spawnEntity(entity);
            newWorld.updateEntityWithOptionalForce(entity, false);
        }
        oldWorld.profiler.endSection();
        entity.setWorld(newWorld);
    }
}