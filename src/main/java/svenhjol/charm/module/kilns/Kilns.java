package svenhjol.charm.module.kilns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.helper.DecorationHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.module.kilns.FiringRecipe;
import svenhjol.charm.module.kilns.KilnBlock;
import svenhjol.charm.module.kilns.KilnBlockEntity;
import svenhjol.charm.module.kilns.KilnScreenHandler;
import svenhjol.charm.module.kilns.KilnsClient;

@Module(mod = Charm.MOD_ID, client = KilnsClient.class, description = "A functional block that speeds up cooking of clay, glass, bricks and terracotta.")
public class Kilns extends CharmModule {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Charm.MOD_ID, "firing");
    public static final ResourceLocation BLOCK_ID = new ResourceLocation(Charm.MOD_ID, "kiln");
    public static final ResourceLocation TRIGGER_FIRED_ITEM = new ResourceLocation(Charm.MOD_ID, "fired_item");

    public static svenhjol.charm.module.kilns.KilnBlock KILN;
    public static BlockEntityType<svenhjol.charm.module.kilns.KilnBlockEntity> BLOCK_ENTITY;
    public static RecipeType<svenhjol.charm.module.kilns.FiringRecipe> RECIPE_TYPE;
    public static SimpleCookingSerializer<svenhjol.charm.module.kilns.FiringRecipe> RECIPE_SERIALIZER;
    public static MenuType<svenhjol.charm.module.kilns.KilnScreenHandler> SCREEN_HANDLER;

    @Override
    public void register() {
        KILN = new svenhjol.charm.module.kilns.KilnBlock(this);
        RECIPE_TYPE = RegistryHelper.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RegistryHelper.recipeSerializer(RECIPE_ID.toString(), new SimpleCookingSerializer<>(FiringRecipe::new, 100));
        BLOCK_ENTITY = RegistryHelper.blockEntity(BLOCK_ID, KilnBlockEntity::new, KILN);
        SCREEN_HANDLER = RegistryHelper.screenHandler(BLOCK_ID, KilnScreenHandler::new);
    }

    @Override
    public void init() {
        DecorationHelper.DECORATION_BLOCKS.add(KILN);
        DecorationHelper.STATE_CALLBACK.put(KILN, facing -> KILN.defaultBlockState().setValue(KilnBlock.FACING, facing));
    }

    public static void triggerFiredItem(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_FIRED_ITEM);
    }
}
