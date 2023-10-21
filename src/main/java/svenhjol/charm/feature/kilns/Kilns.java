package svenhjol.charm.feature.kilns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.feature.firing.Firing;

import java.util.List;
import java.util.function.Supplier;

public class Kilns extends CommonFeature {
    private static final String BLOCK_ID = "kiln";
    public static Supplier<KilnBlock> block;
    public static Supplier<BlockItem> blockItem;
    public static Supplier<MenuType<KilnMenu>> menu;
    public static Supplier<BlockEntityType<KilnBlockEntity>> blockEntity;
    public static Supplier<RecipeBookType> recipeBookType;
    public static Supplier<SoundEvent> bakeSound;

    @Override
    public String description() {
        return "A functional block that speeds up cooking of clay, glass, bricks and terracotta.";
    }

    @Override
    public void preRegister() {
        mod().registry().recipeBookTypeEnum("kiln");
    }

    @Override
    public void register() {
        // Must register Charmony's firing recipe serializer as a dependency or firing recipes will fail.
        Firing.registerDependency();

        var registry = mod().registry();

        block = registry.block(BLOCK_ID, () -> new KilnBlock(this));
        blockItem = registry.item(BLOCK_ID, () -> new KilnBlock.BlockItem(this, block));
        blockEntity = registry.blockEntity(BLOCK_ID, () -> KilnBlockEntity::new, List.of(block));

        recipeBookType = registry.recipeBookType(BLOCK_ID);
        menu = registry.menuType(BLOCK_ID, () -> new MenuType<>(KilnMenu::new, FeatureFlags.VANILLA_SET));

        bakeSound = registry.soundEvent("kiln_bake");
    }

    public static void triggerFiredItem(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "fired_item"), player);
    }
}
