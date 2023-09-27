package svenhjol.charm.feature.raid_horns;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.PatrolSpawnerAccessor;
import svenhjol.charm.mixin.accessor.ServerLevelAccessor;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.EntityKilledDropEvent;
import svenhjol.charmony_api.iface.IWandererTrade;
import svenhjol.charmony_api.iface.IWandererTradeProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Raid horns are sometimes dropped from raid leaders and can be used to call off raids or summon pillagers.")
public class RaidHorns extends CharmFeature implements IWandererTradeProvider {
    public static Supplier<RaidHornItem> item;
    public static Supplier<SoundEvent> callPatrolSound;
    public static Supplier<SoundEvent> callOffRaidSound;
    public static Supplier<SoundEvent> failSound;
    public static final double DROP_CHANCE = 0.05D;
    public static final double LOOT_BONUS = 0.1D;
    public static final int DURABILITY = 4;
    public static final int WANDERER_TRADE_COST = 30; // Number of emeralds a WT charges.

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        item = registry.item("raid_horn", () -> new RaidHornItem(this));
        callPatrolSound = registry.soundEvent("raid_horn_call_patrol");
        callOffRaidSound = registry.soundEvent("raid_horn_call_off_raid");
        failSound = registry.soundEvent("raid_horn_squeak");

        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        EntityKilledDropEvent.INSTANCE.handle(this::handleEntityDrop);
    }

    private InteractionResult handleEntityDrop(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.level().isClientSide()
            && entity instanceof PatrollingMonster patroller
            && source.getEntity() instanceof Player
            && entity.level().random.nextDouble() <= DROP_CHANCE + (LOOT_BONUS * lootingLevel)
        ) {
            if (patroller.isPatrolLeader()) {
                var pos = patroller.blockPosition();
                var horn = new ItemStack(item.get());
                patroller.level().addFreshEntity(new ItemEntity(entity.getCommandSenderWorld(), pos.getX(), pos.getY(), pos.getZ(), horn));
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * @see PatrolSpawner#tick
     */
    @SuppressWarnings("deprecation")
    static boolean trySpawnPillagers(ServerLevel level, BlockPos pos) {
        PatrolSpawner spawner = null;
        var spawners = ((ServerLevelAccessor)level).getCustomSpawners();
        for (var s : spawners) {
            if (s instanceof PatrolSpawner) {
                spawner = (PatrolSpawner)s;
                break;
            }
        }

        if (spawner == null) {
            return false;
        }

        var random = level.getRandom();

        // Some copypasta from PatrolSpawner#tick
        var j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        var k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        var mutable = pos.mutable().move(j, 0, k);
        if (!level.hasChunksAt(mutable.getX() - 10, mutable.getY() - 10, mutable.getZ() - 10, mutable.getX() + 10, mutable.getY() + 10, mutable.getZ() + 10)) {
            return false;
        } else {
            var holder = level.getBiome(mutable);
            if (holder.is(BiomeTags.WITHOUT_PATROL_SPAWNS)) {
                return false;
            } else {
                var spawned = false;
                var n = (int)Math.ceil(level.getCurrentDifficultyAt(mutable).getEffectiveDifficulty()) + 1;
                for (int o = 0; o < n; ++o) {
                    mutable.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                    var result = ((PatrolSpawnerAccessor)spawner).invokeSpawnPatrolMember(level, mutable, random, o == 0);

                    if (result) {
                        spawned = true;
                        mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                        mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                    }
                }

                // Reset the global pillager spawner timer after spawning these in.
                ((PatrolSpawnerAccessor)spawner).setNextTick(12000);
                return spawned;
            }
        }
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of();
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return item.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return WANDERER_TRADE_COST;
            }
        });
    }
}
