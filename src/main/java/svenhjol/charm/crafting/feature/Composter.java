package svenhjol.charm.crafting.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.crafting.block.BlockComposter;
import svenhjol.charm.crafting.message.MessageComposterAddLevel;
import svenhjol.meson.Feature;
import svenhjol.meson.NetworkHandler;

import java.util.*;

public class Composter extends Feature
{
    public static BlockComposter block;
    public static Map<String, Float> inputs = new HashMap<>();
    public static List<String> outputs = new ArrayList<>();
    public static int maxOutput;

    @Override
    public void setupConfig()
    {
        String[] items;

        items = propStringList(
                "Items with 30% chance",
                "These items have a 30% chance of adding a level of compost.",
                new String[] {
                        "minecraft:beetroot_seeds",
                        "minecraft:grass",
                        "minecraft:leaves",
                        "minecraft:melon_seeds",
                        "minecraft:pumpkin_seeds",
                        "minecraft:sapling",
                        "minecraft:wheat_seeds"
                }
        );
        for (String item : items) inputs.put(item, 0.3f);

        items = propStringList(
                "Items with 50% chance",
                "These items have a 50% chance of adding a level of compost.",
                new String[] {
                        "minecraft:cactus",
                        "minecraft:melon",
                        "minecraft:reeds",
                        "minecraft:double_plant",
                        "minecraft:tallgrass",
                }
        );
        for (String item : items) inputs.put(item, 0.5f);

        items = propStringList(
                "Items with 65% chance",
                "These items have a 65% chance of adding a level of compost.",
                new String[] {
                        "minecraft:apple",
                        "minecraft:beetroot",
                        "minecraft:carrot",
                        "minecraft:dye[3]",
                        "minecraft:tallgrass[2]",
                        "minecraft:yellow_flower",
                        "minecraft:red_flower",
                        "minecraft:red_mushroom",
                        "minecraft:brown_mushroom",
                        "minecraft:potato",
                        "minecraft:poisonous_potato",
                        "minecraft:pumpkin",
                        "minecraft:wheat"
                }
        );
        for (String item : items) inputs.put(item, 0.65f);

        items = propStringList(
                "Items with 85% chance",
                "These items have a 85% chance of adding a level of compost.",
                new String[] {
                        "minecraft:baked_potato",
                        "minecraft:bread",
                        "minecraft:cookie",
                        "minecraft:hay_block",
                        "minecraft:red_mushroom_block",
                        "minecraft:brown_mushroom_block"
                }
        );
        for (String item : items) inputs.put(item, 0.85f);

        items = propStringList(
                "Items with 100% chance",
                "These items have a 100% chance of adding a level of compost.",
                new String[] {
                        "minecraft:cake",
                        "minecraft:pumpkin_pie"
                }
        );
        for (String item : items) inputs.put(item, 1.0f);

        items = propStringList(
                "Output from composter",
                "Items that may be produced by the composter when it is full.",
                new String[] {
                        "minecraft:dye[15]",
                }
        );
        outputs.addAll(Arrays.asList(items));

        maxOutput = propInt(
                "Maximum number of output items",
                "Sets the maximum stack size of the composter output.",
                3
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockComposter();
        NetworkHandler.register(MessageComposterAddLevel.class, Side.CLIENT);

        /* @todo recipe */
    }

    @SideOnly(Side.CLIENT)
    public static void spawnComposterParticles(BlockPos pos, int level)
    {
        WorldClient world = Minecraft.getMinecraft().world;
        for (int i = 0; i < level + 2; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double dx = (float)pos.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            double dy = (float)pos.getY() + 1.05f;
            double dz = (float)pos.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, dx, dy, dz, d0, d1, d2);
        }
    }
}
