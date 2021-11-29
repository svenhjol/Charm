package svenhjol.charm.module.raid_horns;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.loader.CharmModule;

import java.util.List;
import java.util.Random;

public class RaidHornItem extends CharmItem {
    public RaidHornItem(CharmModule module) {
        super(module, "raid_horn", new Properties()
            .stacksTo(1)
            .durability(4)
            .tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack horn = user.getItemInHand(hand);

        if (!world.isClientSide) {
            world.playSound(null, user.blockPosition(), CharmSounds.RAID_HORN, SoundSource.PLAYERS, (float) RaidHorns.volume, 1.0F);
            user.startUsingItem(hand);
        }

        return InteractionResultHolder.consume(horn);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks) {
        if (level.isClientSide) return;
        if (getPullProgress(getUseDuration(stack) - remainingUseTicks) < 1.0F) return;

        BlockPos pos = user.blockPosition();
        ServerLevel serverLevel = (ServerLevel)level;
        if (!(user instanceof Player)) return;

        ServerPlayer player = (ServerPlayer) user;

        if (serverLevel.isRaided(pos)) {
            Raid raid = serverLevel.getRaidAt(pos);
            if (raid != null) {
                raid.stop();
                RaidHorns.triggerCalledOff(player);
            }
        } else {
            boolean result = trySpawnPillagers(serverLevel, (Player) user);
            if (result) {
                RaidHorns.triggerSummoned(player);
            }
        }

        player.getCooldowns().addCooldown(this, 100);
        stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
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

    private boolean trySpawnPillagers(ServerLevel level, Player player) {
        PatrolSpawner patrolSpawner = null;
        List<CustomSpawner> spawners = level.customSpawners;
        for (CustomSpawner spawner : spawners) {
            if (spawner instanceof PatrolSpawner) {
                patrolSpawner = (PatrolSpawner)spawner;
                break;
            }
        }

        if (patrolSpawner == null) return false;

        Random random = level.getRandom();

        // copypasta from PillagerSpawner
        int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        BlockPos.MutableBlockPos mutable = player.blockPosition().mutable().move(j, 0, k);
        if (!level.hasChunksAt(mutable.getX() - 10, mutable.getY() - 10, mutable.getZ() - 10, mutable.getX() + 10, mutable.getY() + 10, mutable.getZ() + 10)) {
            return false;
        } else {
            Biome biome = level.getBiome(mutable);
            Biome.BiomeCategory category = biome.getBiomeCategory();
            if (category == Biome.BiomeCategory.MUSHROOM) {
                return false;
            } else {
                int m = 0;
                int n = (int)Math.ceil((double)level.getCurrentDifficultyAt(mutable).getEffectiveDifficulty()) + 1;

                for(int o = 0; o < n; ++o) {
                    ++m;
                    mutable.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                    if (o == 0) {
                        if (!patrolSpawner.spawnPatrolMember(level, mutable, random, true)) {
                            break;
                        }
                    } else {
                        patrolSpawner.spawnPatrolMember(level, mutable, random, false);
                    }

                    mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                    mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                }

                // must reset the global pillager spawner timer after spawning these in
                patrolSpawner.nextTick = 12000;
                return true;
            }
        }
    }
}
