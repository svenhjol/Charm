package svenhjol.charm.world.decorator.outer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import svenhjol.charm.world.feature.VillageDecorations;
import svenhjol.meson.decorator.MesonOuterDecorator;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.WorldHelper;

import java.util.List;
import java.util.Random;

public class Mobs extends MesonOuterDecorator
{
    public Mobs(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        Biome biome = world.getBiome(pos);

        int max = 1;
        for (int i = 0; i < max; i++) {
            if (rand.nextFloat() <= VillageDecorations.mobsWeight) return;

            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;

            BlockPos posForMob = world.getHeight(pos.add(xx, 0, zz));
            boolean airAbove = world.getBlockState(posForMob) == Blocks.AIR.getDefaultState();
            boolean grassBelow = world.getBlockState(posForMob.offset(EnumFacing.DOWN)) == Blocks.GRASS.getDefaultState();
            if (!airAbove && !grassBelow && !EntityHelper.canMobsSpawnInPos(world, posForMob)) continue;


            if (VillageDecorations.villageInfested.contains(WorldHelper.getChunkPos(pos))) {
                String horseType;

                if (rand.nextFloat() < 0.5) {
                    horseType = "minecraft:zombie_horse";
                } else {
                    horseType = "minecraft:skeleton_horse";
                }

                EntityHelper.spawnEntity(new ResourceLocation(horseType), world, posForMob);
                continue;
            }


            int mobType = rand.nextInt(4);

            if (biome.isSnowyBiome() && rand.nextFloat() <= VillageDecorations.golemsWeight) {
                // Jon Snow
                EntitySnowman snowGolem = new EntitySnowman(world);
                snowGolem.setPosition(posForMob.getX(), posForMob.getY(), posForMob.getZ());
                world.spawnEntity(snowGolem);
            }

            if (mobType == 0) {
                // Cat
                Entity entity = EntityHelper.spawnEntity(new ResourceLocation("minecraft:ocelot"), world, posForMob);
                if (entity != null) {
                    EntityOcelot cat = (EntityOcelot) entity;
                    cat.setTameSkin(rand.nextInt(4));
                    cat.setSitting(false);
                    if (rand.nextFloat() < 0.2f) {
                        cat.setTamed(true);
                        cat.setOwnerId(cat.getUniqueID());
                    }
                }
            } else if (mobType == 1) {
                // Wolf
                Entity entity = EntityHelper.spawnEntity(new ResourceLocation("minecraft:wolf"), world, posForMob);
                if (entity != null) {
                    EntityWolf wolf = (EntityWolf) entity;
                    wolf.setSitting(false);
                    if (rand.nextFloat() < 0.2f) {
                        wolf.setTamed(true);
                        wolf.setOwnerId(wolf.getUniqueID());
                    }
                }
            } else if (mobType == 2 && rand.nextFloat() <= VillageDecorations.golemsWeight) {
                // Iron Golem
                EntityIronGolem golem = new EntityIronGolem(world);
                golem.setPosition(posForMob.getX(), posForMob.getY(), posForMob.getZ());
                world.spawnEntity(golem);
            } else if (mobType == 3) {
                // Hest
                Entity entity = EntityHelper.spawnEntity(new ResourceLocation("minecraft:horse"), world, posForMob);
                if (entity instanceof EntityHorse) {
                    EntityHorse horse = (EntityHorse) entity;
                    horse.setHorseVariant(rand.nextInt(7));
                }
            }
        }
    }
}
