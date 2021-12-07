package svenhjol.charm.module.kilns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

@CommonModule(mod = Charm.MOD_ID, description = "A functional block that speeds up cooking of clay, glass, bricks and terracotta.")
public class Kilns extends CharmModule {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Charm.MOD_ID, "firing");
    public static final ResourceLocation BLOCK_ID = new ResourceLocation(Charm.MOD_ID, "kiln");
    public static final ResourceLocation TRIGGER_FIRED_ITEM = new ResourceLocation(Charm.MOD_ID, "fired_item");

    public static KilnBlock KILN;
    public static BlockEntityType<KilnBlockEntity> BLOCK_ENTITY;
    public static RecipeType<FiringRecipe> RECIPE_TYPE;
    public static SimpleCookingSerializer<FiringRecipe> RECIPE_SERIALIZER;
    public static MenuType<KilnScreenHandler> SCREEN_HANDLER;

    public static final SoundEvent KILN_BAKE = CharmSounds.createSound("kiln_bake");

    public static RecipeBookType RECIPE_BOOK_TYPE;

    @Override
    public void register() {
        KILN = new KilnBlock(this);
        RECIPE_TYPE = CommonRegistry.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = CommonRegistry.recipeSerializer(RECIPE_ID.toString(), new SimpleCookingSerializer<>(FiringRecipe::new, 100));
        BLOCK_ENTITY = CommonRegistry.blockEntity(BLOCK_ID, KilnBlockEntity::new, KILN);
        SCREEN_HANDLER = CommonRegistry.menu(BLOCK_ID, KilnScreenHandler::new);
    }

    @Override
    public void runWhenEnabled() {
        RECIPE_BOOK_TYPE = CommonRegistry.recipeBookType("kiln");
    }

    public static void triggerFiredItem(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_FIRED_ITEM);
    }
}
