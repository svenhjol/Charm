package svenhjol.charm.brewing.feature;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
    public static Map<String, BlockFlavoredCake> cakes2 = new HashMap<>();
    public static Map<PotionType, BlockFlavoredCake> cakeTypes = new HashMap<>();
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
                "night_vision",
                "quark:danger_sight",
                "quark:haste",
                "quark:resistance"
            }
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        for (String name : validPotions) {
            String baseName;

            if (name.contains(":")) {
                String[] split = name.split(":");
                baseName = split[1];
            } else {
                baseName = name;
            }

            BlockFlavoredCake cake = new BlockFlavoredCake(baseName);
            cakes2.put(name, cake);

            // add flavored cakes to the composter inputs
            if (Charm.hasFeature(Composter.class)) {
                Composter.inputs.put(Objects.requireNonNull(cake.getRegistryName()).toString(), 1.0f);
            }
        }

        // dispensers can imbue cakes
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.POTIONITEM, new DispenseHandlerImbue());

        NetworkHandler.register(MessageCakeImbue.class, Side.CLIENT);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        for (String name : cakes2.keySet()) {
            String longName;
            String strongName;
            PotionType type = null;
            BlockFlavoredCake cake = cakes2.get(name);
            int duration = 0;

            if (name.contains(":")) {
                String[] split = name.split(":");
                String modName = split[0];
                longName = modName + ":long_" + split[1];
                strongName = modName + ":strong_" + split[1];
            } else {
                longName = "long_" + name;
                strongName = "strong_" + name;
            }

            String[] names = new String[] {longName, strongName, name};
            for (String n : names) {
                type = PotionType.getPotionTypeForName(n);
                if (type == null || type.getEffects().isEmpty()) continue;
                break;
            }

            if (type == null || type.getEffects().isEmpty()) {
                Meson.log("Cannot find PotionType for " + name + ", skipping");
                continue;
            }

            ItemStack potionItem = ItemHelper.getPotionBottle(1, type);
            List<PotionEffect> effects = PotionUtils.getEffectsFromStack(potionItem);

            for (PotionEffect effect : effects) {
                duration = Math.max(duration, effect.getDuration());
            }
            if (duration == 0) duration = 10;

            cake.setPotionItem(potionItem, duration);
            cakeTypes.put(type, cake);
        }
    }

    @SubscribeEvent
    public void onUse(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack held = event.getEntityPlayer().getHeldItem(event.getHand());
        boolean result = imbue(event.getWorld(), event.getPos(), held);
        if (result) {
            event.setCanceled(true);
        }
    }

    private boolean imbue(World world, BlockPos pos, ItemStack item)
    {
        if (world.getBlockState(pos).getBlock() == Blocks.CAKE) {
            PotionType potionItem = PotionUtils.getPotionFromItem(item);

            if (FlavoredCake.cakeTypes.containsKey(potionItem)) {
                IBlockState current = world.getBlockState(pos);
                IBlockState imbued = FlavoredCake.cakeTypes.get(potionItem).getDefaultState().withProperty(BlockFlavoredCake.BITES, current.getValue(BlockFlavoredCake.BITES));
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
    public static void cakeImbued(BlockPos pos)
    {
        World world = Minecraft.getMinecraft().world;
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
