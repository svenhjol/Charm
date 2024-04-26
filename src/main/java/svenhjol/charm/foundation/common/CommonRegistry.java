package svenhjol.charm.foundation.common;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.block.CharmStairBlock;
import svenhjol.charm.foundation.block.CharmWallHangingSignBlock;
import svenhjol.charm.foundation.block.CharmWallSignBlock;
import svenhjol.charm.foundation.deferred.DeferredPotionMix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class CommonRegistry implements svenhjol.charm.foundation.Registry {
    private final String id;
    private final Log log;

    private static final List<String> RECIPE_BOOK_TYPE_ENUMS = new ArrayList<>();
    private final List<DeferredPotionMix> deferredPotionMixes = new ArrayList<>();

    public CommonRegistry(String id) {
        this.id = id;
        this.log = new Log(id, this);
    }

    public <B extends Block> Supplier<B> block(String id, Supplier<B> supplier) {
        log.debug("Registering block " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.BLOCK, id(id), supplier.get());
        return () -> registered;
    }

    public <T extends BlockEntity, U extends Block> Supplier<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder, List<Supplier<U>> blocks) {
        log.debug("Registering block entity " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(id),
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

    public Supplier<BlockSetType> blockSetType(IVariantWoodMaterial material) {
        var registered = BlockSetType.register(new BlockSetType(material.getSerializedName()));
        return () -> registered;
    }

    public void brewingRecipe(Holder<Potion> input, Supplier<Item> reagent, Holder<Potion> output) {
        deferredPotionMixes.add(new DeferredPotionMix(input, reagent, output));
    }

    public <I extends ItemLike, D extends DispenseItemBehavior> Supplier<D> dispenserBehavior(Supplier<I> item, Supplier<D> dispenserBehavior) {
        var behavior = dispenserBehavior.get();
        log.debug("Registering dispenser behavior " + behavior.toString());
        DispenserBlock.registerBehavior(item.get(), behavior);
        return dispenserBehavior;
    }

    public <T extends Entity> Supplier<EntityType<T>> entity(String id, Supplier<EntityType.Builder<T>> builder) {
        log.debug("Registering entity " + id);
        var registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, id(id), builder.get().build(id(id).toString()));
        return () -> registered;
    }

    public <T extends LivingEntity> void entityAttributes(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> builder) {
        FabricDefaultAttributeRegistry.register(entity.get(), builder.get());
    }

    public <T extends Mob> void entitySpawnPlacement(Supplier<EntityType<T>> entity, SpawnPlacementType placementType,
                                                     Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(entity.get(), placementType, heightmapType, predicate);
    }

    public <T extends IFuelProvider> void fuel(Supplier<T> provider) {
        var item = provider.get();
        FuelRegistry.INSTANCE.add((ItemLike) item, item.fuelTime());
    }

    public String id() {
        return this.id;
    }

    public ResourceLocation id(String path) {
        return new ResourceLocation(this.id, path);
    }


    public <T extends IIgniteProvider> void ignite(Supplier<T> provider) {
        var block = provider.get();
        ((FireBlock) Blocks.FIRE).setFlammable((Block)block, block.igniteChance(), block.burnChance());
    }

    public <I extends Item> Supplier<I> item(String id, Supplier<I> supplier) {
        log.debug("Registering item " + id);
        var registered = net.minecraft.core.Registry.register(BuiltInRegistries.ITEM, id(id), supplier.get());
        return () -> registered;
    }

    public void pointOfInterestBlockStates(Supplier<PoiType> poiType, Supplier<List<BlockState>> states) {
        var resourceKey = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(poiType.get()).orElseThrow();
        var holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(resourceKey);
        var blockStates = new ArrayList<>(holder.value().matchingStates());
        blockStates.addAll(states.get());

        blockStates.forEach(state -> PoiTypes.TYPE_BY_STATE.put(state, holder));
    }

    public Holder<Potion> potion(String id, Supplier<Potion> supplier) {
        var potion = net.minecraft.core.Registry.registerForHolder(BuiltInRegistries.POTION, id(id), supplier.get());
        return potion;
    }

    public List<DeferredPotionMix> potionMixes() {
        return deferredPotionMixes;
    }

    public void recipeBookTypeEnum(String name) {
        RECIPE_BOOK_TYPE_ENUMS.add(name);
    }

    public static List<String> recipeBookTypeEnums() {
        return RECIPE_BOOK_TYPE_ENUMS;
    }

    public <S extends RecipeSerializer<T>, T extends Recipe<?>> Supplier<S> recipeSerializer(String id, Supplier<S> serializer) {
        log.debug("Registering recipe serializer " + id);
        var registered = RecipeSerializer.register(id(id).toString(), serializer.get());
        return () -> registered;
    }

    @SuppressWarnings("unchecked")
    public <R extends Recipe<?>> Supplier<RecipeType<R>> recipeType(String id) {
        log.debug("Registering recipe type " + id);
        var registered = RecipeType.register(id(id).toString());
        return () -> (RecipeType<R>) registered;
    }

    public <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier) {
        log.debug("Registering sound event " + id);
        var registered = Registry.register(BuiltInRegistries.SOUND_EVENT, id(id), supplier.get());
        return () -> registered;
    }

    public Supplier<SoundEvent> soundEvent(String id) {
        if (id.contains(":")) {
            var res = new ResourceLocation(id);
            return soundEvent(res.getPath(), () -> SoundEvent.createVariableRangeEvent(res));
        } else {
            return soundEvent(id, () -> SoundEvent.createVariableRangeEvent(id(id)));
        }
    }

    @SuppressWarnings("unchecked")
    public <B extends StairBlock & IIgniteProvider, I extends BlockItem> Pair<Supplier<B>, Supplier<I>> stairsBlock(String id, IVariantMaterial material, Supplier<BlockState> state) {
        log.debug("Registering stairs block " + id);
        var block = block(id, () -> new CharmStairBlock(material, state.get()));
        var item = item(id, () -> new CharmStairBlock.BlockItem(block));
        return Pair.of((Supplier<B>)block, (Supplier<I>)item);
    }

    public <B extends Block, S extends Block> void strippable(Supplier<B> block, Supplier<S> strippedBlock) {
        // Make axe strippables map mutable.
        AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        AxeItem.STRIPPABLES.put(block.get(), strippedBlock.get());
    }

    @SuppressWarnings("unchecked")
    public <W extends WallHangingSignBlock, S extends CeilingHangingSignBlock> Supplier<W> wallHangingSignBlock(String id, IVariantWoodMaterial material, Supplier<S> drops, WoodType type) {
        log.debug("Registering wall hanging sign block " + id);
        var block = block(id, () -> new CharmWallHangingSignBlock(material, drops.get(), type));
        return (Supplier<W>)block;
    }

    @SuppressWarnings("unchecked")
    public <W extends WallSignBlock, S extends SignBlock> Supplier<W> wallSignBlock(String id, IVariantWoodMaterial material, Supplier<S> drops, WoodType type) {
        log.debug("Registering wall sign block " + id);
        var block = block(id, () -> new CharmWallSignBlock(material, drops.get(), type));
        return (Supplier<W>)block;
    }

    @SuppressWarnings("unchecked")
    public <T extends WoodType> Supplier<T> woodType(String id, IVariantWoodMaterial material) {
        var registered = WoodType.register(new WoodType(id(id).toString().replace(":", "_"), material.blockSetType()));
        return () -> (T)registered;
    }
}
