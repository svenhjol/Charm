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
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.helper.*;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, description = "Ender pearl storage. Eating a chorus fruit will teleport you to the nearest ender pearl block.")
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
        disable.forEach(a -> AdvancementHelper.removeAdvancement(new ResourceLocation(Charm.MOD_ID, "block_of_ender_pearls/" + a)));
    }

    @Override
    public void runWhenEnabled() {
        AddEntityCallback.EVENT.register(this::addGoalToSilverfish);
    }

    public static boolean tryChorusTeleport(LivingEntity entity, ItemStack stack) {
        if (!Charm.LOADER.isEnabled(BlockOfEnderPearls.class) || !chorusTeleport)
            return false;

        if (!(entity instanceof Player))
            return false; // don't want non-players teleporting!

        if (entity.level.isClientSide)
            return false; // must be on server side

        ServerPlayer player = (ServerPlayer)entity;
        BlockPos playerPos = player.blockPosition();
        Level world = player.level;
        Map<Double, BlockPos> foundPositions = new HashMap<>();

        // find blocks around player
        BlockPos.betweenClosedStream(
            playerPos.offset(-teleportRange, -teleportRange, -teleportRange),
            playerPos.offset(teleportRange, teleportRange, teleportRange)
        ).forEach(blockPos -> {
            BlockPos abovePos = blockPos.above(1);
            BlockState stateAtPos = world.getBlockState(blockPos);

            if (stateAtPos.getBlock() != ENDER_PEARL_BLOCK)
                return; // must be an ender pearl block

            if (abovePos.equals(playerPos))
                return; // can't teleport the player back to their current spot

            BlockState state1 = world.getBlockState(blockPos.above(1));
            BlockState state2 = world.getBlockState(blockPos.above(2));
            if (state1.getMaterial() != Material.AIR || state2.getMaterial() != Material.AIR)
                return; // don't suffocate the player

            double dist = WorldHelper.getDistanceSquared(playerPos, abovePos);
            foundPositions.put(dist, abovePos);
        });

        if (foundPositions.isEmpty())
            return false; // didn't find any ender pearl block locations

        BlockPos targetPos = foundPositions.get(Collections.min(foundPositions.keySet()));
        if (targetPos == null)
            return false; // didn't find the closest ender pearl?

        double x = targetPos.getX() + 0.5D;
        double y = targetPos.getY();
        double z = targetPos.getZ() + 0.5D;

        // final parameter is whether to display particle effects after teleport
        boolean didTeleport = player.randomTeleport(x, y, z, true);
        if (!didTeleport)
            return false; // I guess the player didn't make it?

        SoundEvent teleportSound = SoundEvents.CHORUS_FRUIT_TELEPORT;
        world.playSound(null, x, y, z, teleportSound, SoundSource.PLAYERS, 1.0F, 1.0F); // at old location
        player.playSound(teleportSound, 1.0F, 1.0F); // at new location

        player.getCooldowns().addCooldown(Items.CHORUS_FRUIT, 20);
        if (!player.isCreative())
            stack.shrink(1);

        triggerTeleported(player);

        return true;
    }

    private InteractionResult addGoalToSilverfish(Entity entity) {
        if (!convertSilverfish)
            return InteractionResult.PASS;

        if (!(entity instanceof Silverfish silverfish))
            return InteractionResult.PASS; // must be a silverfish to process it

        GoalSelector goalSelector = MobHelper.getGoalSelector(silverfish);

        if (goalSelector.getRunningGoals().noneMatch(g -> g.getGoal() instanceof FormEndermiteGoal))
            goalSelector.addGoal(2, new FormEndermiteGoal(silverfish));

        return InteractionResult.SUCCESS;
    }

    public static void triggerConvertedSilverfishForNearbyPlayers(ServerLevel world, BlockPos pos) {
        PlayerHelper.getPlayersInRange(world, pos).forEach(player
            -> CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer)player, TRIGGER_CONVERTED_SILVERFISH));
    }

    public static void triggerTeleported(ServerPlayer playerEntity) {
        CharmAdvancements.ACTION_PERFORMED.trigger(playerEntity, TRIGGER_TELEPORTED_TO_ENDER_PEARL_BLOCK);
    }
}
