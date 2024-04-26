package svenhjol.charm.feature.woodcutters;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.function.Supplier;

public class Woodcutters extends CommonFeature {
    static Supplier<WoodcutterBlock> block;
    static Supplier<WoodcutterBlock.BlockItem> blockItem;
    static Supplier<MenuType<WoodcutterMenu>> menu;
    static Supplier<PoiType> poiType;
    static Supplier<SoundEvent> useSound;
    static Supplier<RecipeBookType> recipeBookType;

    @Override
    public String description() {
        return "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.";
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new CommonRegistration(this));
    }
}
