package svenhjol.charm.foundation.common;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Registry;

import java.util.ArrayList;
import java.util.HashSet;
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

    public <T extends BlockEntity, U extends Block> Supplier<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder, List<Supplier<U>> blocks) {
        log.debug("Registering block entity " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, makeId(id),
            BlockEntityType.Builder.of(builder.get(), blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
        return () -> registered;
    }

    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder) {
        return blockEntity(id, builder, List.of());
    }

    public <T extends BlockEntity> void blockEntityBlocks(Supplier<BlockEntityType<T>> supplier, List<Supplier<? extends Block>> blocks) {
        var blockEntityBlocks = supplier.get().validBlocks;
        List<Block> mutable = new ArrayList<>(blockEntityBlocks);

        for (Supplier<? extends Block> blockSupplier : blocks) {
            var block = blockSupplier.get();
            if (!mutable.contains(block)) {
                mutable.add(block);
            }
        }

        supplier.get().validBlocks = new HashSet<>(mutable);
    }

    public <T extends IFuelProvider> void fuel(Supplier<T> provider) {
        var item = provider.get();
        FuelRegistry.INSTANCE.add((ItemLike) item, item.fuelTime());
    }

    public String id() {
        return this.id;
    }

    public <T extends IIgniteProvider> void ignite(Supplier<T> provider) {
        var block = provider.get();
        ((FireBlock) Blocks.FIRE).setFlammable((Block)block, block.igniteChance(), block.burnChance());
    }

    public <I extends Item> Supplier<I> item(String id, Supplier<I> supplier) {
        log.debug("Registering item " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.ITEM, makeId(id), supplier.get());
        return () -> registered;
    }

    private ResourceLocation makeId(String path) {
        return new ResourceLocation(this.id, path);
    }

    public void pointOfInterestBlockStates(Supplier<PoiType> poiType, Supplier<List<BlockState>> states) {
        var resourceKey = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(poiType.get()).orElseThrow();
        var holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(resourceKey);
        var blockStates = new ArrayList<>(holder.value().matchingStates());
        blockStates.addAll(states.get());

        blockStates.forEach(state -> PoiTypes.TYPE_BY_STATE.put(state, holder));
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
