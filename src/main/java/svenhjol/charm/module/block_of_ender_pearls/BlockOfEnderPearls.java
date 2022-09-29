package svenhjol.charm.module.block_of_ender_pearls;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.AddEntityCallback;
import svenhjol.charm.lib.Advancements;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, description = "Ender pearl storage. A silverfish will be converted to an endermite upon touching an ender pearl block.\n" +
    "Eating a chorus fruit will teleport you to the nearest ender pearl block.")
public class BlockOfEnderPearls extends CharmModule {
    public static EnderPearlBlock ENDER_PEARL_BLOCK;

    public static final ResourceLocation TRIGGER_CONVERTED_SILVERFISH = new ResourceLocation(Charm.MOD_ID, "converted_silverfish");
    public static final ResourceLocation TRIGGER_TELEPORTED_TO_ENDER_PEARL_BLOCK = new ResourceLocation(Charm.MOD_ID, "teleported_to_ender_pearl_block");

    @Config(name = "Chorus teleport range", description = "A chorus fruit will teleport you to an ender pearl block within this range (in blocks).")
    public static int teleportRange = 16;

    @Config(name = "Chorus fruit teleport", description = "If true, eating a chorus fruit while in range of an ender pearl block will teleport you to it.")
    public static boolean chorusTeleport = true;

    @Config(name = "Convert silverfish to endermite", description = "If true, a silverfish has a chance to be converted into an endermite when it is next to an ender pearl block.")
    public static boolean convertSilverfish = true;

    @Override
    public void register() {
        ENDER_PEARL_BLOCK = new EnderPearlBlock(this);

        // conditionally disable advancements
        List<String> disable = new ArrayList<>();
        if (!chorusTeleport) disable.add("teleport_to_ender_pearl_block");
        if (!convertSilverfish) disable.add("convert_silverfish");
        disable.forEach(a -> Advancements.removeAdvancement(new ResourceLocation(Charm.MOD_ID, "block_of_ender_pearls/" + a)));
    }

    @Override
    public void runWhenEnabled() {
        AddEntityCallback.EVENT.register(this::addGoalToSilverfish);
        ExtraWanderingTrades.registerRareItem(ENDER_PEARL_BLOCK, 1, 16);
    }

    public static boolean tryChorusTeleport(LivingEntity entity, ItemStack stack) {
        if (!Charm.LOADER.isEnabled(BlockOfEnderPearls.class) || !chorusTeleport) return false;
        if (entity.level.isClientSide) return false;

        BlockPos pos = entity.blockPosition();
        ServerLevel level = (ServerLevel)entity.level;
        Map<Double, BlockPos> candidates = new HashMap<>();

        // find blocks around player
        BlockPos.betweenClosedStream(
            pos.offset(-teleportRange, -teleportRange, -teleportRange),
            pos.offset(teleportRange, teleportRange, teleportRange)
        ).forEach(p -> {
            BlockPos above = p.above(1);
            BlockState state = level.getBlockState(p);

            if (state.getBlock() != ENDER_PEARL_BLOCK) {
                return; // must be an ender pearl block
            }

            if (above.equals(pos)) {
                return; // can't teleport the player back to their current spot
            }

            BlockState state1 = level.getBlockState(p.above(1));
            BlockState state2 = level.getBlockState(p.above(2));
            if (state1.getMaterial() != Material.AIR || state2.getMaterial() != Material.AIR) {
                return; // don't suffocate the player
            }

            double dist = WorldHelper.getDistanceSquared(pos, above);
            candidates.put(dist, above);
        });

        if (candidates.isEmpty()) return false;
        BlockPos target = candidates.get(Collections.min(candidates.keySet()));
        if (target == null) return false;

        double x = target.getX() + 0.5D;
        double y = target.getY();
        double z = target.getZ() + 0.5D;

        // final parameter is whether to display particle effects after teleport
        boolean didTeleport = entity.randomTeleport(x, y, z, true);
        if (!didTeleport) return false;

        SoundEvent teleportSound = SoundEvents.CHORUS_FRUIT_TELEPORT;
        level.playSound(null, x, y, z, teleportSound, SoundSource.PLAYERS, 1.0F, 1.0F); // at old location
        entity.playSound(teleportSound, 1.0F, 1.0F); // at new location

        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(Items.CHORUS_FRUIT, 20);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                triggerTeleported((ServerPlayer)player);
            }
        }

        return true;
    }

    private InteractionResult addGoalToSilverfish(Entity entity) {
        if (!convertSilverfish)
            return InteractionResult.PASS;

        if (!(entity instanceof Silverfish silverfish))
            return InteractionResult.PASS; // must be a silverfish to process it

        if (silverfish.goalSelector.getRunningGoals().noneMatch(g -> g.getGoal() instanceof FormEndermiteGoal)) {
            silverfish.goalSelector.addGoal(2, new FormEndermiteGoal(silverfish));
        }

        return InteractionResult.SUCCESS;
    }

    public static void triggerConvertedSilverfishForNearbyPlayers(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos).forEach(player
            -> Advancements.triggerActionPerformed((ServerPlayer)player, TRIGGER_CONVERTED_SILVERFISH));
    }

    public static void triggerTeleported(ServerPlayer playerEntity) {
        Advancements.triggerActionPerformed(playerEntity, TRIGGER_TELEPORTED_TO_ENDER_PEARL_BLOCK);
    }
}