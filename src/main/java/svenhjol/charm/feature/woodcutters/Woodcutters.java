package svenhjol.charm.feature.woodcutters;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, priority = 1, description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends CharmFeature {
    public static final String BLOCK_ID = "woodcutter";
    public static Supplier<WoodcutterBlock> BLOCK;
    public static Supplier<WoodcutterBlock.BlockItem> BLOCK_ITEM;
    public static Supplier<MenuType<WoodcutterMenu>> MENU;
    public static Supplier<PoiType> POI_TYPE;
    public static Supplier<SoundEvent> USE_SOUND;
    public static Supplier<RecipeBookType> RECIPE_BOOK_TYPE;

    @Override
    public void preRegister() {
        Charm.instance().registry().recipeBookTypeEnum("woodcutter");
    }

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        BLOCK = registry.block(BLOCK_ID,
            () -> new WoodcutterBlock(this));
        BLOCK_ITEM = registry.item(BLOCK_ID,
            () -> new WoodcutterBlock.BlockItem(this, BLOCK));

        POI_TYPE = registry.pointOfInterestType(BLOCK_ID,
            () -> new PoiType(ImmutableSet.copyOf(BLOCK.get().getStateDefinition().getPossibleStates()), 1, 1));

        RECIPE_BOOK_TYPE = registry.recipeBookType(BLOCK_ID);
        MENU = registry.menuType(BLOCK_ID,
            () -> new MenuType<>(WoodcutterMenu::new, FeatureFlags.VANILLA_SET));

        USE_SOUND = registry.soundEvent("woodcutter_use");
    }
}
