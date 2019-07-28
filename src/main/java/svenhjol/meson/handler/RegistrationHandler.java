package svenhjol.meson.handler;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonEffect;
import svenhjol.meson.iface.IMesonItem;
import svenhjol.meson.iface.IMesonPotion;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD)
public class RegistrationHandler
{
    public static List<IMesonBlock> BLOCKS = new ArrayList<>();
    public static List<IMesonItem> ITEMS = new ArrayList<>();
    public static List<IMesonPotion> POTIONS = new ArrayList<>();
    public static List<IMesonEffect> EFFECTS = new ArrayList<>();

    public static void addBlock(IMesonBlock block)
    {
        ((Block)block).setRegistryName(new ResourceLocation(block.getModId(), block.getBaseName()));
        BLOCKS.add(block);
    }

    public static void addEffect(IMesonEffect effect)
    {
        ((Effect)effect).setRegistryName(new ResourceLocation(effect.getModId(), effect.getBaseName()));
        EFFECTS.add(effect);
    }

    public static void addItem(IMesonItem item)
    {
        ((Item)item).setRegistryName(new ResourceLocation(item.getModId(), item.getBaseName()));
        ITEMS.add(item);
    }

    public static void addPotion(IMesonPotion potion)
    {
        ((Potion)potion).setRegistryName(new ResourceLocation(potion.getModId(), potion.getBaseName()));
        POTIONS.add(potion);
    }

    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
    {
        final IForgeRegistry<Block> registry = event.getRegistry();

        // register blocks
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            registry.register((Block)block);
        }

        Meson.log("Block registration done");
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();

        // register items
        for (IMesonItem item : RegistrationHandler.ITEMS) {
            registry.register((Item)item);
        }

        // register block items
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            BlockItem blockItem = block.getBlockItem();
            registry.register(blockItem);
        }

        Meson.log("Item registration done");
    }

    @SubscribeEvent
    public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
    {
        final IForgeRegistry<Effect> registry = event.getRegistry();

        // register effects
        for (IMesonEffect effect : RegistrationHandler.EFFECTS) {
            registry.register((Effect)effect);
        }

        Meson.log("Effect registration done");
    }

    @SubscribeEvent
    public static void onRegisterPotions(final RegistryEvent.Register<Potion> event)
    {
        final IForgeRegistry<Potion> registry = event.getRegistry();

        // register potions
        for (IMesonPotion p : RegistrationHandler.POTIONS) {
            Potion potion = (Potion)p;
            registry.register(potion);

            // register the recipe
            ((IMesonPotion)potion).registerRecipe();
        }

        Meson.log("Potion registration done");
    }
}
