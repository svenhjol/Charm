package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.meson.handler.RegistrationHandler;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonEffect;
import svenhjol.meson.iface.IMesonItem;
import svenhjol.meson.iface.IMesonPotion;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD)
public class ModEventSubscriber
{
    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
    {
        final IForgeRegistry<Block> registry = event.getRegistry();

        // register blocks
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            registry.register(((Block)block).setRegistryName(block.getModId(), block.getBaseName()));
        }

        Meson.log("Block registration done");
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();

        // register items
        for (IMesonItem item : RegistrationHandler.ITEMS) {
            registry.register(((Item)item).setRegistryName(item.getModId(), item.getBaseName()));
        }

        // register block items
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            registry.register(block.getBlockItem().setRegistryName(block.getModId(), block.getBaseName()));
        }

        Meson.log("Item registration done");
    }

    @SubscribeEvent
    public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
    {
        final IForgeRegistry<Effect> registry = event.getRegistry();

        // register effects
        for (IMesonEffect effect : RegistrationHandler.EFFECTS) {
            registry.register(((Effect)effect).setRegistryName(effect.getModId(), effect.getBaseName()));
        }

        Meson.log("Effect registration done");
    }

    @SubscribeEvent
    public static void onRegisterPotions(final RegistryEvent.Register<Potion> event)
    {
        final IForgeRegistry<Potion> registry = event.getRegistry();

        // register potions
        for (IMesonPotion p : RegistrationHandler.POTIONS) {
            Potion potion = ((Potion)p).setRegistryName(p.getModId(), p.getBaseName());
            registry.register(potion);

            // register the recipe
            ItemStack out = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion);
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(p.getBase(), p.getReagant(), out));
        }

        Meson.log("Potion registration done");
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event)
    {
        Meson.log("Common setup done");
    }
}
