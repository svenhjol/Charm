package svenhjol.charm.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.PillagerSpawner;
import net.minecraft.world.gen.Spawner;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.item.CharmItem;
import svenhjol.charm.mixin.accessor.PillagerSpawnerAccessor;
import svenhjol.charm.mixin.accessor.ServerWorldAccessor;
import svenhjol.charm.module.RaidHorns;

import java.util.List;
import java.util.Random;

public class RaidHornItem extends CharmItem {
    public RaidHornItem(CharmModule module) {
        super(module, "raid_horn", new Item.Settings()
            .maxCount(1)
            .maxDamage(16)
            .group(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack horn = user.getStackInHand(hand);

        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), CharmSounds.RAID_HORN, SoundCategory.PLAYERS, (float)RaidHorns.volume, 1.0F);
            user.setCurrentHand(hand);
        }

        return TypedActionResult.consume(horn);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world.isClient)
            return;

        if (getPullProgress(getMaxUseTime(stack) - remainingUseTicks) < 1.0F)
            return;

        BlockPos pos = user.getBlockPos();
        ServerWorld serverWorld = (ServerWorld)world;
        if (serverWorld.hasRaidAt(pos)) {
            Raid raid = serverWorld.getRaidAt(pos);
            if (raid != null)
                raid.invalidate();
        } else {
            if (user instanceof PlayerEntity)
                trySpawnPillagers(serverWorld, (PlayerEntity)user);
        }

        if (user instanceof PlayerEntity)
            ((PlayerEntity)user).getItemCooldownManager().set(this, 100);


        stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private void trySpawnPillagers(ServerWorld world, PlayerEntity player) {
        PillagerSpawner pillagerSpawner = null;
        List<Spawner> spawners = ((ServerWorldAccessor)world).getSpawners();
        for (Spawner spawner : spawners) {
            if (spawner instanceof PillagerSpawner) {
                pillagerSpawner = (PillagerSpawner)spawner;
                break;
            }
        }

        if (pillagerSpawner == null)
            return;

        Random random = world.getRandom();

        // copypasta from PillagerSpawner
        int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        BlockPos.Mutable mutable = player.getBlockPos().mutableCopy().move(j, 0, k);
        if (!world.isRegionLoaded(mutable.getX() - 10, mutable.getY() - 10, mutable.getZ() - 10, mutable.getX() + 10, mutable.getY() + 10, mutable.getZ() + 10)) {
            return;
        } else {
            Biome biome = world.getBiome(mutable);
            Biome.Category category = biome.getCategory();
            if (category == Biome.Category.MUSHROOM) {
                return;
            } else {
                int m = 0;
                int n = (int)Math.ceil((double)world.getLocalDifficulty(mutable).getLocalDifficulty()) + 1;

                for(int o = 0; o < n; ++o) {
                    ++m;
                    mutable.setY(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                    if (o == 0) {
                        if (!((PillagerSpawnerAccessor)pillagerSpawner).invokeSpawnPillager(world, mutable, random, true)) {
                            break;
                        }
                    } else {
                        ((PillagerSpawnerAccessor)pillagerSpawner).invokeSpawnPillager(world, mutable, random, false);
                    }

                    mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                    mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                }

                // must reset the global pillager spawner timer after spawning these in
                ((PillagerSpawnerAccessor)pillagerSpawner).setTicksUntilNextSpawn(12000);
                return;
            }
        }
    }
}
