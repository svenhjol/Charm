package svenhjol.charm.foundation.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class CommonRegistry implements Registry {
    private final String id;
    private final Log log;

    public CommonRegistry(String id) {
        this.id = id;
        this.log = new Log(id, this);
    }

    private static final List<String> RECIPE_BOOK_TYPE_ENUMS = new ArrayList<>();

    public <B extends Block> Supplier<B> block(String id, Supplier<B> supplier) {
        log.debug("Registering block " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.BLOCK, makeId(id), supplier.get());
        return () -> registered;
    }

    public static List<String> getRecipeBookTypeEnums() {
        return RECIPE_BOOK_TYPE_ENUMS;
    }

    public void recipeBookTypeEnum(String name) {
        RECIPE_BOOK_TYPE_ENUMS.add(name);
    }

    public <I extends Item> Supplier<I> item(String id, Supplier<I> supplier) {
        log.debug("Registering item " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.ITEM, makeId(id), supplier.get());
        return () -> registered;
    }

    private ResourceLocation makeId(String path) {
        return new ResourceLocation(this.id, path);
    }
}
