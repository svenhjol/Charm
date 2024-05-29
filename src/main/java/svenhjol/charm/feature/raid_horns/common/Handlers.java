package svenhjol.charm.feature.raid_horns.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import svenhjol.charm.api.enums.ItemStackResult;
import svenhjol.charm.charmony.common.helper.EnchantmentsHelper;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.raid_horns.RaidHorns;

import java.util.function.Supplier;

public final class Handlers extends FeatureHolder<RaidHorns> {
    public final static double DROP_CHANCE = 0.05d;
    public final static double LOOTING_MULTIPLIER = 0.1d;
    public static final float RANGE = 256.0f; // Matches goat horns.
    public static final int DURATION = 140; // Matches goat horns.

    public Handlers(RaidHorns feature) {
        super(feature);
    }

    public InteractionResult entityDrop(LivingEntity entity, DamageSource source) {
        if (!entity.level().isClientSide()
            && entity instanceof PatrollingMonster patroller
            && source.getEntity() instanceof Player
            && entity.level().random.nextDouble() <= DROP_CHANCE + (EnchantmentsHelper.lootingLevel(source) * LOOTING_MULTIPLIER)
        ) {
            if (patroller.isPatrolLeader()) {
                var pos = patroller.blockPosition();
                var horn = new ItemStack(feature().registers.item.get());
                patroller.level().addFreshEntity(new ItemEntity(entity.getCommandSenderWorld(), pos.getX(), pos.getY(), pos.getZ(), horn));
            }
        }

        return InteractionResult.PASS;
    }

    public ItemStackResult useHorn(RaidHornItem item, Level level, Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        var range = RANGE / 16.0F;
        var pos = player.blockPosition();
        Supplier<SoundEvent> sound = null;

        if (!level.isClientSide()) {
            var serverLevel = (ServerLevel)level;
            var isRaided = serverLevel.isRaided(pos);
            log().debug("is raided: " + isRaided);

            if (isRaided) {
                // Try and call off the raid.
                var raid = serverLevel.getRaidAt(pos);
                if (raid != null) {
                    raid.stop();
                    sound = feature().registers.callOffRaidSound;
                    feature().advancements.calledOffRaid(player);
                }
            } else {
                // Try and summon pillagers.
                var result = trySpawnPillagers(serverLevel, pos);
                if (result) {
                    sound = feature().registers.callPatrolSound;
                }
            }

            if (sound == null) {
                sound = feature().registers.failSound;
            }

            player.startUsingItem(hand);
            level.playSound(null, player, sound.get(), SoundSource.RECORDS, range, 1.0F);
            player.getCooldowns().addCooldown(item, DURATION);

            if (!player.getAbilities().instabuild) {
                held.hurtAndBreak(1, player, Player.getSlotForHand(hand));
            }
        }

        return ItemStackResult.consume(held);
    }

    /**
     * @see PatrolSpawner#tick
     */
    @SuppressWarnings("deprecation")
    public boolean trySpawnPillagers(ServerLevel level, BlockPos pos) {
        PatrolSpawner patrolSpawner = null;
        var spawners = level.customSpawners;
        for (var s : spawners) {
            if (s instanceof PatrolSpawner) {
                patrolSpawner = (PatrolSpawner)s;
                break;
            }
        }

        if (patrolSpawner == null) {
            log().debug("patrolSpawner is null");
            return false;
        }

        var random = level.getRandom();

        // Some copypasta from PatrolSpawner#tick
        var j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        var k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        var mutable = pos.mutable().move(j, 0, k);
        if (!level.hasChunksAt(mutable.getX() - 10, mutable.getY() - 10, mutable.getZ() - 10, mutable.getX() + 10, mutable.getY() + 10, mutable.getZ() + 10)) {
            log().debug("not spawning, no chunks loaded for pos " + mutable);
            return false;
        } else {
            var holder = level.getBiome(mutable);
            if (holder.is(BiomeTags.WITHOUT_PATROL_SPAWNS)) {
                log().debug("not spawning, biome is in WITHOUT_PATROL_SPAWNS");
                return false;
            } else {
                var spawned = false;
                var n = (int)Math.ceil(level.getCurrentDifficultyAt(mutable).getEffectiveDifficulty()) + 1;
                for (int o = 0; o < n; ++o) {
                    mutable.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                    var result = patrolSpawner.spawnPatrolMember(level, mutable, random, o == 0);

                    if (result) {
                        log().debug("spawned patrol near " + mutable);
                        spawned = true;
                        mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                        mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                    } else {
                        log().debug("not spawning, spawnPatrolMember returned false");
                    }
                }

                // Reset the global pillager spawner timer after spawning these in.
                patrolSpawner.nextTick = 12000;
                return spawned;
            }
        }
    }
}
