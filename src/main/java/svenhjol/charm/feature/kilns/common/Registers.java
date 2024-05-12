package svenhjol.charm.feature.kilns.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.List;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Kilns> {
    public static final String BLOCK_ID = "kiln";
    public final Supplier<Block> block;
    public final Supplier<BlockItem> blockItem;
    public final Supplier<MenuType<KilnMenu>> menu;
    public final Supplier<BlockEntityType<BlockEntity>> blockEntity;
    public final Supplier<RecipeBookType> recipeBookType;
    public final Supplier<SoundEvent> bakeSound;

    public Registers(Kilns feature) {
        super(feature);
        var registry = feature.registry();

        // Early registration of enums.
        feature.registry().recipeBookTypeEnum("kiln");

        block = registry.block(BLOCK_ID, Block::new);
        blockItem = registry.item(BLOCK_ID, () -> new Block.BlockItem(block));
        blockEntity = registry.blockEntity(BLOCK_ID, () -> BlockEntity::new, List.of(block));

        recipeBookType = registry.recipeBookType(BLOCK_ID);
        menu = registry.menuType(BLOCK_ID, () -> new MenuType<>(KilnMenu::new, FeatureFlags.VANILLA_SET));

        bakeSound = registry.soundEvent("kiln_bake");
    }
}
