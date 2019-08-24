package svenhjol.charm.building.module;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.building.block.EnderPearlBlock;
import svenhjol.charm.building.goal.FormEndermiteGoal;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BUILDING, hasSubscriptions = true)
public class BlockOfEnderPearls extends MesonModule
{
    public static EnderPearlBlock block;
    public static float hardness = 2.0F;
    public static int range = 8;
    @Config
    public static boolean teleportStabilize = true;

    @Override
    public void init()
    {
        block = new EnderPearlBlock(this);
    }

    /**
     * When player eats Chorus Fruit in range of Ender Pearl Block, get the
     * coordinates of the closest portal block and teleport them to it.
     * @param event LivingEntityUseItemEvent.Start
     */
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Start event)
    {
        boolean didTeleport = false;

        if (teleportStabilize
            && event.getEntityLiving() instanceof PlayerEntity
            && event.getItem().getItem() == Items.CHORUS_FRUIT
            && !event.getEntityLiving().getEntityWorld().isRemote
        ) {
            World world = event.getEntityLiving().world;
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            Map<Double, BlockPos> positions = new HashMap<>();
            BlockPos playerPos = player.getPosition();
            BlockPos targetPos = null;

            // find the blocks around the player
            BlockPos.getAllInBox(playerPos.add(-range, -range, -range), playerPos.add(range, range, range)).forEach(pos -> {
                if (world.getBlockState(pos).getBlock() == block && !pos.up(1).equals(playerPos)) {

                    // should be able to stand on it
                    if (world.getBlockState(pos.up(1)).getMaterial() == Material.AIR && world.getBlockState(pos.up(2)).getMaterial() == Material.AIR)
                    {
                        positions.put(WorldHelper.getDistanceSq(playerPos, pos.up(1)), pos.up(1));
                    }
                }
            });

            // get the closest position by finding the smallest distance
            if (!positions.isEmpty()) {
                targetPos = positions.get(Collections.min(positions.keySet()));
            }

            if (targetPos != null) {
                double x = targetPos.getX() + 0.5D;
                double y = targetPos.getY();
                double z = targetPos.getZ() + 0.5D;
                if (player.attemptTeleport(x, y, z, true)) {
                    didTeleport = true;

                    // play sound at original location and new location
                    world.playSound(null, x, y, z, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                }
            }

            if (didTeleport) {
                player.getCooldownTracker().setCooldown(Items.CHORUS_FRUIT, 20);
                event.getItem().shrink(1);
                event.setCanceled(true);
            }
        }
    }

    /**
     * When silversish spawns, add an AI goal to it so that it has the
     * possibility to form an endermite when close to an Ender Pearl Block.
     * @param event EntityEvent.EnteringChunk
     */
    @SubscribeEvent
    public void onEnterChunk(EntityEvent.EnteringChunk event)
    {
        if (event.getEntity() instanceof SilverfishEntity) {
            SilverfishEntity silverfish = (SilverfishEntity)event.getEntity();

            boolean hasGoal = silverfish.goalSelector.getRunningGoals().anyMatch(g -> g.getGoal() instanceof FormEndermiteGoal);

            if (!hasGoal) {
                silverfish.goalSelector.addGoal(2, new FormEndermiteGoal(silverfish));
            }
        }
    }
}
