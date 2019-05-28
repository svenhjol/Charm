package svenhjol.charm.world.feature;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmEntityIDs;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.world.client.render.RenderChargedEmerald;
import svenhjol.charm.world.entity.EntityChargedEmerald;
import svenhjol.charm.world.item.ItemChargedEmerald;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.LootHelper;
import svenhjol.meson.helper.PlayerHelper;

public class ChargedEmeralds extends Feature
{
    public static ItemChargedEmerald emerald;

    public static int maxChargedInStrike; // maximum number of emerals that will be charged in one strike
    public static boolean addToLoot;

    @Override
    public String getDescription()
    {
        return "If you are holding an emerald and are struck by lightning, the emerald will become charged and you will take no damage.\n" +
            "Right-click a Charged Emerald to throw it, creating a lightning strike at the point of impact.";
    }

    @Override
    public void setupConfig()
    {
        maxChargedInStrike = propInt(
            "Maximum charged emeralds per strike",
            "The maximum number of emeralds that will be converted into Charged Emeralds when a stack of emeralds is held.",
            3
        );
        addToLoot = propBoolean(
            "Add to loot",
            "Add the charged emerald to dungeon loot.",
            true
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        emerald = new ItemChargedEmerald();
        String name = Charm.MOD_ID + ":charged_emerald";
        EntityRegistry.registerModEntity(new ResourceLocation(name), EntityChargedEmerald.class, name, CharmEntityIDs.CHARGED_EMERALD, Charm.instance, 64, 10, true);

        // fun times with dispensers!
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(emerald, new BehaviorProjectileDispense()
        {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
            return new EntityChargedEmerald(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event)
    {
        //noinspection unchecked,unchecked
        RenderingRegistry.registerEntityRenderingHandler(EntityChargedEmerald.class, RenderChargedEmerald.FACTORY);
    }

    @SubscribeEvent
    public void onStruckByLightning(EntityStruckByLightningEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer
            && !event.getEntity().world.isRemote
        ) {
            EntityLightningBolt bolt = event.getLightning();
            EntityPlayer player = (EntityPlayer) event.getEntity();

            EnumHand hand = EnumHand.MAIN_HAND;
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() != Items.EMERALD) {
                hand = EnumHand.OFF_HAND;
                stack = player.getHeldItem(hand);
                if (stack.getItem() != Items.EMERALD) return;
            }

            if (bolt.ticksExisted < 2) {
                int numCharged = Math.min(maxChargedInStrike, stack.getCount());
                ItemStack out = new ItemStack(emerald, numCharged);
                PlayerHelper.addOrDropStack(player, out);
                player.getHeldItem(hand).setCount(stack.getCount() - numCharged);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
        if (!addToLoot) return;
        int weight = 0;
        int quality = 0;
        LootFunction[] functions = new LootFunction[0];
        LootCondition[] conditions = new LootCondition[0];

        if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) { weight = 10; }
        if (event.getName().equals(CharmLootTables.TREASURE_UNCOMMON)) { weight = 10; }

        if (weight > 0) {
            LootHelper.addToLootTable(event.getTable(), emerald, weight, quality, functions, conditions);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
