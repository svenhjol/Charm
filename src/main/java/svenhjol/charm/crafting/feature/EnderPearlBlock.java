package svenhjol.charm.crafting.feature;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.crafting.ai.AIFormEndermite;
import svenhjol.charm.crafting.block.BlockEnderPearl;
import svenhjol.meson.Feature;
import svenhjol.meson.registry.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;
import svenhjol.meson.helper.WorldHelper;

public class EnderPearlBlock extends Feature
{
    public static BlockEnderPearl block;
    public static float hardness;
    public static int range;
    public static boolean showParticles;
    public static boolean teleportStabilize;
    public static double endermiteChance;

    @Override
    public String getDescription()
    {
        return "A storage block for ender pearls. Eating a chorus fruit will teleport you to a nearby ender pearl block.\n" +
                "If a silverfish burrows into an ender pearl block, it will become an endermite.";
    }

    @Override
    public void configure()
    {
        super.configure();

        endermiteChance = propDouble(
                "Silverfish burrowing",
                "Chance (out of 1.0) of a silverfish burrowing into an Ender Pearl Block, creating an Endermite.",
                0.8D
        );

        teleportStabilize = propBoolean(
                "Teleport stabilization",
                "If true, eating a Chorus Fruit while in range of an Ender Pearl Block will teleport you to it.",
                true
        );

        // internal
        hardness = 2.0f;
        range = 8;
        showParticles = true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        block = new BlockEnderPearl();

        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(block, 1),
                "EEE", "EEE", "EEE",
                'E', Items.ENDER_PEARL
        );
        RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(Items.ENDER_PEARL, 9),
                ProxyRegistry.newStack(block));
    }

    public static boolean onChorusFruitEaten(World world, EntityLivingBase entity)
    {
        boolean didTeleport = false;

        if (!world.isRemote && entity != null && teleportStabilize) {

            // find the blocks around the entity
            double distance = 0;
            BlockPos selectedPos = null;
            BlockPos entityPos = entity.getPosition();

            Iterable<BlockPos> positions = BlockPos.getAllInBox(entityPos.add(-range, -range, -range), entityPos.add(range, range, range));
            for (BlockPos pos : positions) {
                if (world.getBlockState(pos).getBlock() == block && !pos.up(1).equals(entityPos)) {

                    // should be able to stand on it
                    if (world.getBlockState(pos.up(1)).getMaterial() == Material.AIR
                        && world.getBlockState(pos.up(2)).getMaterial() == Material.AIR)
                    {
                        double d = WorldHelper.getDistanceSq(entityPos, pos.up(1));
                        if (distance == 0 || d < distance) {
                            distance = d;
                            selectedPos = pos.up(1); // the air block above
                        }
                    }
                }
            }
            if (selectedPos != null) {
                double x = selectedPos.getX() + 0.5D;
                double y = selectedPos.getY();
                double z = selectedPos.getZ() + 0.5D;
                if (entity.attemptTeleport(x, y, z)) {
                    world.playSound(null, x, y, z, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    didTeleport = true;
                }
            }

            if (didTeleport && entity instanceof EntityPlayer) {
                ((EntityPlayer)entity).getCooldownTracker().setCooldown(Items.CHORUS_FRUIT, 20);
            }
        }

        return didTeleport;
    }

    @SubscribeEvent
    public void onEnterChunk(EntityEvent.EnteringChunk event)
    {
        if (event.getEntity() instanceof EntitySilverfish) {
            EntitySilverfish silverfish = (EntitySilverfish) event.getEntity();
            for (EntityAITasks.EntityAITaskEntry task : silverfish.tasks.taskEntries) {
                if (task.action instanceof AIFormEndermite) return;
            }
            silverfish.tasks.addTask(2, new AIFormEndermite(silverfish));
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
