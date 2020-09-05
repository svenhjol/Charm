package svenhjol.charm.module;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.block.WoodcutterBlock;
import svenhjol.charm.gui.WoodcutterScreen;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.charm.recipe.WoodcuttingRecipe;
import svenhjol.charm.screenhandler.WoodcutterScreenHandler;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.VillagerHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.Random;

@Module(description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public class Lumberjacks extends MesonModule {
    public static Identifier RECIPE_ID = new Identifier("woodcutting");
    public static Identifier BLOCK_ID = new Identifier(Charm.MOD_ID, "woodcutter");
    public static Identifier VILLAGER_ID = new Identifier(Charm.MOD_ID, "lumberjack");

    public static VillagerProfession LUMBERJACK;
    public static WoodcutterBlock WOODCUTTER;
    public static PointOfInterestType POIT;
    public static ScreenHandlerType<WoodcutterScreenHandler> SCREEN_HANDLER;
    public static RecipeType<WoodcuttingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<WoodcuttingRecipe> RECIPE_SERIALIZER;

    @Override
    public void init() {
        WOODCUTTER = new WoodcutterBlock(this);

        RECIPE_TYPE = RecipeType.register(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RecipeSerializer.register(RECIPE_ID.toString(), new WoodcuttingRecipe.Serializer(WoodcuttingRecipe::new));

        SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, BLOCK_ID, new ScreenHandlerType<>(WoodcutterScreenHandler::new));

        // TODO dedicated sounds for woodcutter and jobsite
        POIT = VillagerHelper.addPointOfInterestType(BLOCK_ID, WOODCUTTER, 1);
        LUMBERJACK = VillagerHelper.addProfession(VILLAGER_ID, POIT, SoundEvents.ENTITY_VILLAGER_WORK_MASON);
        VillagerHelper.addTrade(LUMBERJACK, 1, new EmeraldsForWoodenPlanks());
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(WOODCUTTER, RenderLayer.getCutout());
        ScreenRegistry.register(SCREEN_HANDLER, WoodcutterScreen::new);
    }

    public static class EmeraldsForWoodenPlanks implements TradeOffers.Factory {
        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            int count = random.nextInt(2) + 1;
            ItemStack in1 = new ItemStack(Items.OAK_PLANKS, count * 6);
            ItemStack out = new ItemStack(Items.EMERALD, count);
            return new TradeOffer(in1, out, 10, 1, 0.2F);
        }
    }
}
