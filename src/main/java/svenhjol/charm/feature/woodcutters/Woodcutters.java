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
import svenhjol.charmony.feature.woodcutting.Woodcutting;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, priority = 1, description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends CharmFeature {
    public static final String BLOCK_ID = "woodcutter";
    public static Supplier<WoodcutterBlock> block;
    public static Supplier<WoodcutterBlock.BlockItem> blockItem;
    public static Supplier<MenuType<WoodcutterMenu>> menu;
    public static Supplier<PoiType> poiType;
    public static Supplier<SoundEvent> useSound;
    public static Supplier<RecipeBookType> recipeBookType;

    @Override
    public void preRegister() {
        Charm.instance().registry().recipeBookTypeEnum("woodcutter");
    }

    @Override
    public void register() {
        // Must register Charmony's woodcutting recipe serializer as a dependency or woodcutting recipes will fail.
        Woodcutting.registerDependency();

        var registry = Charm.instance().registry();

        block = registry.block(BLOCK_ID,
            () -> new WoodcutterBlock(this));
        blockItem = registry.item(BLOCK_ID,
            () -> new WoodcutterBlock.BlockItem(this, block));

        poiType = registry.pointOfInterestType(BLOCK_ID,
            () -> new PoiType(ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()), 1, 1));

        recipeBookType = registry.recipeBookType(BLOCK_ID);
        menu = registry.menuType(BLOCK_ID,
            () -> new MenuType<>(WoodcutterMenu::new, FeatureFlags.VANILLA_SET));

        useSound = registry.soundEvent("woodcutter_use");
    }
}
