package svenhjol.charm.foundation.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
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

    public <I extends Item> Supplier<I> item(String id, Supplier<I> supplier) {
        log.debug("Registering item " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.ITEM, makeId(id), supplier.get());
        return () -> registered;
    }

    private ResourceLocation makeId(String path) {
        return new ResourceLocation(this.id, path);
    }

    public void recipeBookTypeEnum(String name) {
        RECIPE_BOOK_TYPE_ENUMS.add(name);
    }

    public static List<String> recipeBookTypeEnums() {
        return RECIPE_BOOK_TYPE_ENUMS;
    }

    public <S extends RecipeSerializer<T>, T extends Recipe<?>> Supplier<S> recipeSerializer(String id, Supplier<S> serializer) {
        log.debug("Registering recipe serializer " + id);
        var registered = RecipeSerializer.register(makeId(id).toString(), serializer.get());
        return () -> registered;
    }

    @SuppressWarnings("unchecked")
    public <R extends Recipe<?>> Supplier<RecipeType<R>> recipeType(String id) {
        log.debug("Registering recipe type " + id);
        var registered = RecipeType.register(makeId(id).toString());
        return () -> (RecipeType<R>) registered;
    }
}
