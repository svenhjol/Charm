package svenhjol.charm.brewing.feature;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.block.BlockFlavoredCake;
import svenhjol.charm.brewing.message.MessageCakeImbue;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.SoundHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlavoredCake extends Feature
{
    public static Map<PotionType, BlockFlavoredCake> cakes = new HashMap<>();
    public static String[] validPotions; // potions that can be made into cakes
    public static double multiplier; // in seconds

    @Override
    public String getDescription()
    {
        return "Right-click a Long Potion on a cake make a Flavored Cake that gives a potion effect after eating each slice.";
    }

    @Override
    public void setupConfig()
    {
        multiplier = propDouble(
            "Effect duration multiplier",
            "Duration multiplier of the potion effect when eating a single slice of cake.",
            0.5
        );
        validPotions = propStringList(
            "Effect potion types",
            "List of Long Potions that can be used to make a cake.",
            new String[] {
                "swiftness",
                "strength",
                "leaping",
                "regeneration",
                "fire_resistance",
                "water_breathing",
                "invisibility",
                "night_vision"
            }
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        for (String name : validPotions) {

            // try for long potion, on failure try normal
            PotionType type = PotionType.getPotionTypeForName("long_" + name);
            if (type == null) {
                type = PotionType.getPotionTypeForName(name);
                if (type == null) {
                    Meson.runtimeException("Could not load potion " + name + " when registering cake");
                }
            }

            ItemStack potionItem = ItemHelper.getPotionBottle(1, type);

            // get duration from potion to be applied
            List<PotionEffect> effects = PotionUtils.getEffectsFromStack(potionItem);
            int duration = 0;
            for (PotionEffect effect : effects) {
                duration = Math.max(duration, effect.getDuration());
            }
            if (duration == 0) {
                duration = 10;
            }

            BlockFlavoredCake cake = new BlockFlavoredCake(name, potionItem, (int)(duration * multiplier));
            cakes.put(type, cake);

            // add flavored cakes to the composter inputs
            if (Charm.hasFeature(Composter.class)) {
                Composter.inputs.put(Objects.requireNonNull(cake.getRegistryName()).toString(), 1.0f);
            }
        }

        // dispensers can imbue cakes
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.POTIONITEM, new DispenseHandlerImbue());

        NetworkHandler.register(MessageCakeImbue.class, Side.CLIENT);
    }

    @SubscribeEvent
    public void onUse(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack held = event.getEntityPlayer().getHeldItem(event.getHand());
        imbue(event.getWorld(), event.getPos(), held);
    }

    private boolean imbue(World world, BlockPos pos, ItemStack item)
    {
        if (world.getBlockState(pos).getBlock() == Blocks.CAKE) {
            PotionType potionItem = PotionUtils.getPotionFromItem(item);

            if (FlavoredCake.cakes.containsKey(potionItem)) {
                IBlockState current = world.getBlockState(pos);
                IBlockState imbued = FlavoredCake.cakes.get(potionItem).getDefaultState().withProperty(BlockFlavoredCake.BITES, current.getValue(BlockFlavoredCake.BITES));
                world.setBlockState(pos, imbued, 2);
                item.shrink(1);

                // let clients know the cake has been imbued
                if (!world.isRemote) {
                    NetworkHandler.INSTANCE.sendToAll(new MessageCakeImbue(pos));
                }
                return true;
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static void cakeImbued(World world, BlockPos pos)
    {
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.ITEM_BOTTLE_EMPTY, 0.5f, 1.1f);
        for (int i = 0; i < 8; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double dx = (float)pos.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            double dy = (float)pos.getY() + 0.65f;
            double dz = (float)pos.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            world.spawnParticle(EnumParticleTypes.SPELL_WITCH, dx, dy, dz, d0, d1, d2);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }

    public class DispenseHandlerImbue extends BehaviorDefaultDispenseItem
    {
        @Override
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            World world = source.getWorld();
            BlockPos pos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
            boolean result = imbue(world, pos, stack);
            if (!result) {
                return super.dispenseStack(source, stack);
            }

            return stack;
        }
    }
}
