package svenhjol.charm.foundation.common;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.block.CharmStairBlock;
import svenhjol.charm.foundation.block.CharmWallHangingSignBlock;
import svenhjol.charm.foundation.block.CharmWallSignBlock;
import svenhjol.charm.foundation.deferred.DeferredPotionMix;
import svenhjol.charm.foundation.helper.DispenserHelper;
import svenhjol.charm.foundation.helper.EnumHelper;
import svenhjol.charm.foundation.helper.TextHelper;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class CommonRegistry implements svenhjol.charm.foundation.Registry {
    private static final List<String> RECIPE_BOOK_TYPE_ENUMS = new ArrayList<>();
    private final List<DeferredPotionMix> deferredPotionMixes = new ArrayList<>(); // Must be on the instance!
    private final CommonLoader loader;
    private final Log log;

    public CommonRegistry(CommonLoader loader) {
        this.loader = loader;
        this.log = new Log(loader.id(), "CommonRegistry");
    }

    public <E extends Entity> void biomeSpawn(Predicate<Holder<Biome>> predicate, MobCategory category, Supplier<EntityType<E>> entity, int weight, int minGroupSize, int maxGroupSize) {
        Predicate<BiomeSelectionContext> biomeSelectionContext = c -> predicate.test(c.getBiomeRegistryEntry());
        BiomeModifications.addSpawn(biomeSelectionContext, category, entity.get(), weight, minGroupSize, maxGroupSize);
    }

    public void biomeAddition(String id, Predicate<Holder<Biome>> predicate, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
        Predicate<BiomeSelectionContext> biomeSelectionContext = c -> predicate.test(c.getBiomeRegistryEntry());
        BiomeModifications.create(id(id + "_biome_addition")).add(
            ModificationPhase.ADDITIONS,
            biomeSelectionContext,
            ctx -> ctx.getGenerationSettings().addFeature(step, feature));
    }

    public <B extends Block> Register<B> block(String id, Supplier<B> supplier) {
        return new Register<>(() -> Registry.register(BuiltInRegistries.BLOCK, id(id), supplier.get()));
//        loader.registerRunnable(reg.supplier::get);
//
//
//        Supplier<B> registered = () -> Registry.register(BuiltInRegistries.BLOCK, id(id), supplier.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    @SuppressWarnings("DataFlowIssue")
    public <T extends BlockEntity, U extends Block> Register<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder, List<Supplier<U>> blocks) {
        return new Register<>(() -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(id),
            BlockEntityType.Builder.of(builder.get(), blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null)));

//        Supplier<BlockEntityType<T>> registered = () -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(id),
//            BlockEntityType.Builder.of(builder.get(), blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public <T extends BlockEntity> Register<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder) {
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

        loader.registerDeferred(() -> supplier.get().validBlocks = new HashSet<>(mutable));
    }

    public Register<BlockSetType> blockSetType(Supplier<IVariantWoodMaterial> material) {
        return new Register<>(() -> BlockSetType.register(new BlockSetType(material.get().getSerializedName())));
//        Supplier<BlockSetType> registered = () -> BlockSetType.register(new BlockSetType(material.get().getSerializedName()));
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public void brewingRecipe(Holder<Potion> input, Supplier<Item> reagent, Holder<Potion> output) {
        deferredPotionMixes.add(new DeferredPotionMix(input, reagent, output));
    }

    public <T extends CustomPacketPayload> void clientPacketSender(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
        loader.registerDeferred(() -> PayloadTypeRegistry.playC2S().register(type, codec));
    }

    public <T> Register<DataComponentType<T>> dataComponent(String id, Supplier<UnaryOperator<DataComponentType.Builder<T>>> dataComponent) {
        return new Register<>(() -> DataComponents.register(id, dataComponent.get()));
//        Supplier<DataComponentType<T>> registered = () -> DataComponents.register(id, dataComponent.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public <I extends ItemLike, D extends DispenseItemBehavior> void dispenserBehavior(Supplier<I> item, Supplier<D> dispenserBehavior) {
        loader.registerDeferred(() -> {
            var behavior = dispenserBehavior.get();
            DispenserBlock.registerBehavior(item.get(), behavior);
        });
    }

//    public <T extends Enchantment> Supplier<T> enchantment(String id, Supplier<T> enchantment) {
//        log.debug("Registering enchantment " + id);
//        var registered = Registry.register(BuiltInRegistries.ENCHANTMENT, id(id), enchantment.get());
//        return () -> registered;
//    }

    public <T extends Entity> Register<EntityType<T>> entity(String id, Supplier<EntityType.Builder<T>> builder) {
        return new Register<>(() -> Registry.register(BuiltInRegistries.ENTITY_TYPE, id(id), builder.get().build(id(id).toString())));
//        Supplier<EntityType<T>> registered = () -> Registry.register(BuiltInRegistries.ENTITY_TYPE, id(id), builder.get().build(id(id).toString()));
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public <T extends LivingEntity> void entityAttributes(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> builder) {
        loader.registerDeferred(() -> FabricDefaultAttributeRegistry.register(entity.get(), builder.get()));
    }

    public <T extends Mob> void entitySpawnPlacement(Supplier<EntityType<T>> entity, SpawnPlacementType placementType,
                                                     Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        loader.registerDeferred(() -> SpawnPlacements.register(entity.get(), placementType, heightmapType, predicate));
    }

    public <T extends IFuelProvider> void fuel(Supplier<T> provider) {
        loader.registerDeferred(() -> {
            var item = provider.get();
            FuelRegistry.INSTANCE.add((ItemLike) item, item.fuelTime());
        });
    }

    public String id() {
        return this.loader.id();
    }

    public ResourceLocation id(String path) {
        return new ResourceLocation(this.loader.id(), path);
    }

    public <T extends IIgniteProvider> void ignite(Supplier<T> provider) {
        loader.registerDeferred(() -> {
            var block = provider.get();
            ((FireBlock) Blocks.FIRE).setFlammable((Block)block, block.igniteChance(), block.burnChance());
        });
    }

    public <I extends Item> Register<I> item(String id, Supplier<I> supplier) {
        return new Register<>(() -> Registry.register(BuiltInRegistries.ITEM, id(id), supplier.get()));
//        Supplier<I> registered = () -> Registry.register(BuiltInRegistries.ITEM, id(id), supplier.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public ResourceKey<LootTable> lootTable(String resource) {
        return ResourceKey.create(Registries.LOOT_TABLE, new ResourceLocation(resource));
    }

    public <T extends MenuType<U>, U extends AbstractContainerMenu> Register<T> menuType(String id, Supplier<T> supplier) {
        return new Register<>(() -> Registry.register(BuiltInRegistries.MENU, id(id), supplier.get()));
//        Supplier<T> registered = () -> Registry.register(BuiltInRegistries.MENU, id(id), supplier.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public Register<Holder<MobEffect>> mobEffect(String id, Supplier<MobEffect> supplier) {
        return new Register<>(() -> Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id(id), supplier.get()));
//        Supplier<Holder<MobEffect>> registered = () -> Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id(id), supplier.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public <T extends CustomPacketPayload> void packetReceiver(CustomPacketPayload.Type<T> type, BiConsumer<Player, T> handler) {
        loader.registerDeferred(() -> ServerPlayNetworking.registerGlobalReceiver(type,
            (packet, context) -> context.player().server.execute(() -> handler.accept(context.player(), packet))));
    }

    public <T extends CustomPacketPayload> void serverPacketSender(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
        loader.registerDeferred(() -> PayloadTypeRegistry.playS2C().register(type, codec));
    }

    public Register<SimpleParticleType> particleType(String id, Supplier<SimpleParticleType> supplier) {
        return new Register<>(() -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, id(id), supplier.get()));
//        Supplier<SimpleParticleType> registered = () -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, id(id), supplier.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public Register<PoiType> pointOfInterestType(String id, Supplier<PoiType> supplier) {
        var register = new Register<>(supplier);

        loader.registerDeferred(() -> {
            var poiType = register.get();
            var poitKey = ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), id(id));

            Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, poitKey, poiType);

            var holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(poitKey);
            var blockStates = holder.value().matchingStates();
            PoiTypes.registerBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(poitKey), blockStates);
        });

        return register;
    }

    public void pointOfInterestBlockStates(Supplier<PoiType> poiType, Supplier<List<BlockState>> states) {
        loader.registerDeferred(() -> {
            var resourceKey = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(poiType.get()).orElseThrow();
            var holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(resourceKey);
            var blockStates = new ArrayList<>(holder.value().matchingStates());
            blockStates.addAll(states.get());
            blockStates.forEach(state -> PoiTypes.TYPE_BY_STATE.put(state, holder));
        });
    }

    public Register<Holder<Potion>> potion(String id, Supplier<Potion> supplier) {
        return new Register<>(() -> Registry.registerForHolder(BuiltInRegistries.POTION, id(id), supplier.get()));
//        Supplier<Holder<Potion>> registered = () -> Registry.registerForHolder(BuiltInRegistries.POTION, id(id), supplier.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public List<DeferredPotionMix> potionMixes() {
        return deferredPotionMixes;
    }

    public void runnable(Runnable runnable) {
        loader.registerDeferred(runnable);
    }

    public Register<RecipeBookType> recipeBookType(String id) {
        var upper = id.toUpperCase(Locale.ROOT);
        var capitalized = TextHelper.capitalize(id.toLowerCase(Locale.ROOT));

        var register = new Register<>(() -> EnumHelper.getValueOrDefault(() -> RecipeBookType.valueOf(upper), RecipeBookType.CRAFTING));

//        Supplier<RecipeBookType> registered = () -> EnumHelper.getValueOrDefault(() -> RecipeBookType.valueOf(upper), RecipeBookType.CRAFTING);

        loader.registerDeferred(() -> {
            var tagFields = new HashMap<>(RecipeBookSettings.TAG_FIELDS);
            tagFields.put(register.get(), Pair.of("is" + capitalized + "GuiOpen", "is" + capitalized + "FilteringCraftable"));
            RecipeBookSettings.TAG_FIELDS = tagFields;
        });

        return register;
    }

    public void recipeBookTypeEnum(String name) {
        RECIPE_BOOK_TYPE_ENUMS.add(name);
    }

    public static List<String> recipeBookTypeEnums() {
        return RECIPE_BOOK_TYPE_ENUMS;
    }

    public <S extends RecipeSerializer<T>, T extends Recipe<?>> Register<S> recipeSerializer(String id, Supplier<S> supplier) {
        return new Register<>(() -> {
            log.debug("Recipe serializer " + id);
            return RecipeSerializer.register(id(id).toString(), supplier.get());
        });
//        Supplier<S> registered = () -> RecipeSerializer.register(id(id).toString(), serializer.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public <R extends Recipe<?>> Register<RecipeType<R>> recipeType(String id) {
        return new Register<>(() -> {
            log.debug("Recipe type " + id);
            return RecipeType.register(id(id).toString());
        });
//        Supplier<RecipeType<R>> registered = () -> RecipeType.register(id(id).toString());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public <T extends SoundEvent> Register<T> soundEvent(String id, Supplier<T> supplier) {
        return new Register<>(() -> Registry.register(BuiltInRegistries.SOUND_EVENT, id(id), supplier.get()));
//        Supplier<T> registered = () -> Registry.register(BuiltInRegistries.SOUND_EVENT, id(id), supplier.get());
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public Register<SoundEvent> soundEvent(String id) {
        if (id.contains(":")) {
            var res = new ResourceLocation(id);
            return soundEvent(res.getPath(), () -> SoundEvent.createVariableRangeEvent(res));
        } else {
            return soundEvent(id, () -> SoundEvent.createVariableRangeEvent(id(id)));
        }
    }

    public <I extends Item, E extends EntityType<? extends Mob>> Register<SpawnEggItem> spawnEggItem(String id, Supplier<E> entity, int primaryColor, int secondaryColor, Item.Properties properties) {
        var item = item(id, () -> new SpawnEggItem(entity.get(), primaryColor, secondaryColor, properties));
        dispenserBehavior(item, DispenserHelper::getDefaultDispenseBehavior);
        return item;
    }

    @SuppressWarnings("unchecked")
    public <B extends StairBlock & IIgniteProvider, I extends BlockItem> Pair<Register<B>, Register<I>> stairsBlock(String id, IVariantMaterial material, Supplier<BlockState> state) {
        var block = block(id, () -> new CharmStairBlock(material, state.get()));
        var item = item(id, () -> new CharmStairBlock.BlockItem(block));
        return Pair.of((Register<B>)block, (Register<I>)item);
    }

    public <B extends Block, S extends Block> void strippable(Supplier<B> block, Supplier<S> strippedBlock) {
        loader.registerDeferred(() -> {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
            AxeItem.STRIPPABLES.put(block.get(), strippedBlock.get());
        });
    }

    public <W extends WallHangingSignBlock, S extends CeilingHangingSignBlock> Register<CharmWallHangingSignBlock> wallHangingSignBlock(String id, IVariantWoodMaterial material, Supplier<S> drops, WoodType type) {
        return block(id, () -> new CharmWallHangingSignBlock(material, drops.get(), type));
    }

    public <W extends WallSignBlock, S extends SignBlock> Register<CharmWallSignBlock> wallSignBlock(String id, IVariantWoodMaterial material, Supplier<S> drops, WoodType type) {
        return block(id, () -> new CharmWallSignBlock(material, drops.get(), type));
    }

    public Register<WoodType> woodType(Supplier<IVariantWoodMaterial> material) {
        return new Register<>(() -> WoodType.register(new WoodType(
            id(material.get().getSerializedName()).toString().replace(":", "_"),
            material.get().blockSetType())));
//        Supplier<WoodType> registered = () -> WoodType.register(new WoodType(
//            id(material.get().getSerializedName()).toString().replace(":", "_"),
//            material.get().blockSetType()));
//
//        loader.registerRunnable(registered::get);
//        return registered;
    }

    public class Register<R> implements Supplier<R> {
        private R instance;
        private final Supplier<R> supplier;

        public Register(Supplier<R> supplier) {
            this.supplier = supplier;
            loader.registerDeferred(this::get);
        }

        public R get() {
            if (instance == null) {
                instance = supplier.get();
            }
            return instance;
        }
    }
}
