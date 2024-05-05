package svenhjol.charm.feature.kilns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class Kilns extends CommonFeature {
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
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    public static void triggerFiredItem(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "fired_item"), player);
    }
}
