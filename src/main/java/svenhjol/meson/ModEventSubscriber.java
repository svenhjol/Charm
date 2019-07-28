package svenhjol.meson;

//@Mod.EventBusSubscriber(bus = MOD)
public class ModEventSubscriber
{
//    @SubscribeEvent
//    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
//    {
//        final IForgeRegistry<Block> registry = event.getRegistry();
//
//        // register blocks
//        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
//            registry.register((Block)block);
//        }
//
//        Meson.log("Block registration done");
//    }
//
//    @SubscribeEvent
//    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
//    {
//        final IForgeRegistry<Item> registry = event.getRegistry();
//
//        // register items
//        for (IMesonItem item : RegistrationHandler.ITEMS) {
//            registry.register((Item)item);
//        }
//
//        // register block items
//        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
//            BlockItem blockItem = block.getBlockItem();
//            registry.register(blockItem);
//        }
//
//        Meson.log("Item registration done");
//    }
//
//    @SubscribeEvent
//    public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
//    {
//        final IForgeRegistry<Effect> registry = event.getRegistry();
//
//        // register effects
//        for (IMesonEffect effect : RegistrationHandler.EFFECTS) {
//            registry.register((Effect)effect);
//        }
//
//        Meson.log("Effect registration done");
//    }
//
//    @SubscribeEvent
//    public static void onRegisterPotions(final RegistryEvent.Register<Potion> event)
//    {
//        final IForgeRegistry<Potion> registry = event.getRegistry();
//
//        // register potions
//        for (IMesonPotion p : RegistrationHandler.POTIONS) {
//            Potion potion = (Potion)p;
//            registry.register(potion);
//
//            // register the recipe
//            ((IMesonPotion)potion).registerRecipe();
//        }
//
//        Meson.log("Potion registration done");
//    }
}
