package svenhjol.charm.charmony.common;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
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
import net.minecraft.world.item.enchantment.Enchantment;
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
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.common.block.CharmStairBlock;
import svenhjol.charm.charmony.common.block.CharmWallHangingSignBlock;
import svenhjol.charm.charmony.common.block.CharmWallSignBlock;
import svenhjol.charm.charmony.common.deferred.DeferredPotionMix;
import svenhjol.charm.charmony.common.helper.DispenserHelper;
import svenhjol.charm.charmony.helper.ConfigHelper;
import svenhjol.charm.charmony.helper.EnumHelper;
import svenhjol.charm.charmony.helper.TextHelper;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;
import svenhjol.charm.charmony.iface.FuelProvider;
import svenhjol.charm.charmony.iface.IgniteProvider;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static net.minecraft.world.entity.npc.VillagerTrades.TRADES;
import static net.minecraft.world.entity.npc.VillagerTrades.WANDERING_TRADER_TRADES;

@SuppressWarnings({"unused", "UnusedReturnValue", "UnnecessaryLocalVariable"})
public final class CommonRegistry implements svenhjol.charm.charmony.Registry {
    private static final List<String> RECIPE_BOOK_TYPE_ENUMS = new ArrayList<>();
    private final List<DeferredPotionMix> deferredPotionMixes = new ArrayList<>(); // Must be on the instance!
    private final CommonLoader loader;
    private final Log log;

    public CommonRegistry(CommonLoader loader) {
        this.loader = loader;
        this.log = new Log(loader.id(), "CommonRegistry");
    }

    public Register<Holder<Attribute>> attribute(String id, Supplier<Attribute> supplier) {
        return new Register<>(() -> {
            log("Attribute " + id);
            return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, id(id), supplier.get());
        });
    }

    public <E extends Entity> void biomeSpawn(Predicate<Holder<Biome>> predicate, MobCategory category, Supplier<EntityType<E>> entity, int weight, int minGroupSize, int maxGroupSize) {
        Predicate<BiomeSelectionContext> biomeSelectionContext = c -> predicate.test(c.getBiomeRegistryEntry());
        BiomeModifications.addSpawn(biomeSelectionContext, category, entity.get(), weight, minGroupSize, maxGroupSize);
    }

    /**
     * May be run late.
     * Use this to conditionally modify a biome in the onEnabled() function of your register.
     */
    public void biomeAddition(String id, Predicate<Holder<Biome>> predicate, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
        log("Biome addition " + id);
        Predicate<BiomeSelectionContext> biomeSelectionContext = c -> predicate.test(c.getBiomeRegistryEntry());
        BiomeModifications.create(id(id + "_biome_addition")).add(
            ModificationPhase.ADDITIONS,
            biomeSelectionContext,
            ctx -> ctx.getGenerationSettings().addFeature(step, feature));
    }

    public <B extends Block> Register<B> block(String id, Supplier<B> supplier) {
        return new Register<>(() -> {
            log("Block " + id);
            return Registry.register(BuiltInRegistries.BLOCK, id(id), supplier.get());
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public <T extends BlockEntity, U extends Block> Register<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder, List<Supplier<U>> blocks) {
        return new Register<>(() -> {
            log("Block entity " + id);
            var builderToAdd = builder.get();
            var blocksToAdd = blocks.stream().map(Supplier::get).toArray(Block[]::new);

            return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(id),
                BlockEntityType.Builder.of(builderToAdd, blocksToAdd).build(null));
        });
    }

    public <T extends BlockEntity> Register<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder) {
        return blockEntity(id, builder, List.of());
    }

    public <T extends BlockEntity> void blockEntityBlocks(Supplier<BlockEntityType<T>> supplier, List<Supplier<? extends Block>> blocks) {
        loader.registerDeferred(() -> {
            var blockEntity = supplier.get();
            var blockEntityBlocks = blockEntity.validBlocks;
            List<Block> mutable = new ArrayList<>(blockEntityBlocks);

            for (Supplier<? extends Block> blockSupplier : blocks) {
                var block = blockSupplier.get();
                if (!mutable.contains(block)) {
                    mutable.add(block);
                }
            }

            if (ConfigHelper.isDevEnvironment()) {
                var key = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity);
                log("Blocks for block entity " + key + ": " + mutable);
            }
            blockEntity.validBlocks = new HashSet<>(mutable);
        });
    }

    public Register<BlockSetType> blockSetType(Supplier<CustomWoodMaterial> material) {
        return new Register<>(() -> {
            var materialName = material.get().getSerializedName();
            log("Block set type " + materialName);
            return BlockSetType.register(new BlockSetType(materialName));
        });
    }

    public void brewingRecipe(Holder<Potion> input, Supplier<Item> reagent, Holder<Potion> output) {
        deferredPotionMixes.add(new DeferredPotionMix(input, reagent, output));
    }

    public <T extends CustomPacketPayload> void clientPacketSender(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
        loader.registerDeferred(() -> {
            log("Client packet sender " + type.id());
            PayloadTypeRegistry.playC2S().register(type, codec);
        });
    }

    public <T> Register<DataComponentType<T>> dataComponent(String id, Supplier<UnaryOperator<DataComponentType.Builder<T>>> dataComponent) {
        return new Register<>(() -> {
            var res = id(id);
            log("Data component " + id);
            return DataComponents.register(res.toString(), dataComponent.get());
        });
    }

    public <I extends ItemLike, D extends DispenseItemBehavior> void dispenserBehavior(Supplier<I> item, Supplier<D> dispenserBehavior) {
        loader.registerDeferred(() -> {
            var behavior = dispenserBehavior.get();
            DispenserBlock.registerBehavior(item.get(), behavior);
        });
    }

    public ResourceKey<Enchantment> enchantment(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, id(name));
    }

    public <T extends Entity> Register<EntityType<T>> entity(String id, Supplier<EntityType.Builder<T>> builder) {
        return new Register<>(() -> {
            log("Entity " + id);
            return Registry.register(BuiltInRegistries.ENTITY_TYPE, id(id), builder.get().build(id(id).toString()));
        });
    }

    public <T extends LivingEntity> void entityAttribute(Supplier<EntityType<T>> entitySupplier, Supplier<Holder<Attribute>> attributeSupplier) {
        loader.registerDeferred(() -> {
            // Unwrap suppliers
            var entity = entitySupplier.get();
            var attribute = attributeSupplier.get();

            log("Entity attribute " + attribute.getRegisteredName() + " for entity type " + entity.getDescriptionId());
            makeAttributeInstancesMutable(entity); // MUST do this so that we can add custom attributes.

            var attributes = DefaultAttributes.getSupplier(entity);
            if (attributes.hasAttribute(attribute)) {
                log.error("Entity type " + entity.getDescriptionId() + " already has attribute " + attribute.getRegisteredName());
                return;
            }
            var instance = new AttributeInstance(attribute, x -> {});
            attributes.instances.put(attribute, instance);
        });
    }

    /**
     * This sets all the attributes for an entity type.
     * Don't do this for vanilla entities; use entityAttribute() to add new attributes to an existing entity.
     */
    public <T extends LivingEntity> void entityAttributes(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> builder) {
        loader.registerDeferred(() -> {
            log("Entity attributes for " + entity.get().getDescriptionId());
            FabricDefaultAttributeRegistry.register(entity.get(), builder.get());
        });
    }

    public <T extends Mob> void entitySpawnPlacement(Supplier<EntityType<T>> entity, SpawnPlacementType placementType,
                                                     Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        loader.registerDeferred(() -> {
            log("Entity spawn placement " + entity.get().getDescriptionId());
            SpawnPlacements.register(entity.get(), placementType, heightmapType, predicate);
        });
    }

    public <T extends FuelProvider> void fuel(Supplier<T> provider) {
        loader.registerDeferred(() -> {
            var item = provider.get();
            FuelRegistry.INSTANCE.add((ItemLike) item, item.fuelTime());
        });
    }

    public String id() {
        return this.loader.id();
    }

    public ResourceLocation id(String path) {
        return loader.id(path);
    }

    public <T extends IgniteProvider> void ignite(Supplier<T> provider) {
        loader.registerDeferred(() -> {
            var block = provider.get();
            ((FireBlock) Blocks.FIRE).setFlammable((Block)block, block.igniteChance(), block.burnChance());
        });
    }

    public <I extends Item> Register<I> item(String id, Supplier<I> supplier) {
        return new Register<>(() -> {
            log("Item " + id);
            return Registry.register(BuiltInRegistries.ITEM, id(id), supplier.get());
        });
    }

    private void log(String message) {
        log.debug("Registering " + TextHelper.uncapitalize(message));
    }

    public ResourceKey<LootTable> lootTable(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, id(name));
    }

    public <T> Register<MemoryModuleType<T>> memoryModuleType(String id) {
        return memoryModuleType(id, () -> new MemoryModuleType<>(Optional.empty()));
    }

    public <T> Register<MemoryModuleType<T>> memoryModuleType(String id, Supplier<MemoryModuleType<T>> supplier) {
        return new Register<>(() -> {
            log("Memory module type " + id);
            return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, id(id), supplier.get());
        });
    }

    public <T extends MenuType<U>, U extends AbstractContainerMenu> Register<T> menuType(String id, Supplier<T> supplier) {
        return new Register<>(() -> {
            log("Menu type " + id);
            return Registry.register(BuiltInRegistries.MENU, id(id), supplier.get());
        });
    }

    public Register<Holder<MobEffect>> mobEffect(String id, Supplier<MobEffect> supplier) {
        return new Register<>(() -> {
            log("Mob effect " + id);
            return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id(id), supplier.get());
        });
    }

    public <T extends CustomPacketPayload> void packetReceiver(CustomPacketPayload.Type<T> type, Supplier<BiConsumer<Player, T>> handler) {
        loader.registerDeferred(() -> {
            log("Packet receiver " + type.id());
            ServerPlayNetworking.registerGlobalReceiver(type,
                (packet, context) -> context.player().server.execute(() -> handler.get().accept(context.player(), packet)));
        });
    }

    public <T extends CustomPacketPayload> void serverPacketSender(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
        loader.registerDeferred(() -> {
            log("Server packet sender " + type.id());
            PayloadTypeRegistry.playS2C().register(type, codec);
        });
    }

    public Register<SimpleParticleType> particleType(String id, Supplier<SimpleParticleType> supplier) {
        return new Register<>(() -> {
            log("Particle type " + id);
            return Registry.register(BuiltInRegistries.PARTICLE_TYPE, id(id), supplier.get());
        });
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
            log("Point of interest type " + id);
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
            log("Point of interest block states for " + resourceKey);
        });
    }

    public Register<Holder<Potion>> potion(String id, Supplier<Potion> supplier) {
        return new Register<>(() -> {
            log("Potion " + id);
            return Registry.registerForHolder(BuiltInRegistries.POTION, id(id), supplier.get());
        });
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

        loader.registerDeferred(() -> {
            var tagFields = new HashMap<>(RecipeBookSettings.TAG_FIELDS);
            tagFields.put(register.get(), Pair.of("is" + capitalized + "GuiOpen", "is" + capitalized + "FilteringCraftable"));
            log("Recipe book type " + id);
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
            log("Recipe serializer " + id);
            return RecipeSerializer.register(id(id).toString(), supplier.get());
        });
    }

    public <R extends Recipe<?>> Register<RecipeType<R>> recipeType(String id) {
        return new Register<>(() -> {
            log("Recipe type " + id);
            var res = id(id);
            var recipe = Registry.register(BuiltInRegistries.RECIPE_TYPE, res, new RecipeType<R>() {
                @Override
                public String toString() {
                    return res.toString();
                }
            });
            return recipe;
        });
    }

    public <T extends SoundEvent> Register<T> soundEvent(String id, Supplier<T> supplier) {
        return new Register<>(() -> {
            log("Sound event " + id);
            return Registry.register(BuiltInRegistries.SOUND_EVENT, id(id), supplier.get());
        });
    }

    public Register<SoundEvent> soundEvent(String id) {
        if (id.contains(":")) {
            var res = ResourceLocation.parse(id);
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

    public <B extends StairBlock & IgniteProvider, I extends BlockItem> Pair<Register<CharmStairBlock>, Register<CharmStairBlock.BlockItem>> stairsBlock(String id, Supplier<CustomMaterial> material, Supplier<BlockState> state) {
        var block = block(id, () -> new CharmStairBlock(material.get(), state.get()));
        var item = item(id, () -> new CharmStairBlock.BlockItem(block));
        return Pair.of(block, item);
    }

    public <B extends Block, S extends Block> void strippable(Supplier<B> block, Supplier<S> strippedBlock) {
        loader.registerDeferred(() -> {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
            AxeItem.STRIPPABLES.put(block.get(), strippedBlock.get());
        });
    }

    public void villagerGift(String id) {
        loader.registerDeferred(() -> {
            log("Villager gift " + id);
            var res = id(id);
            var lootTableName = res.getNamespace() + ":gameplay/hero_of_the_village/" + res.getPath() + "_gift";
            var lootTable = lootTable(lootTableName);
            var profession = BuiltInRegistries.VILLAGER_PROFESSION.getOptional(res).orElseThrow();
            GiveGiftToHero.GIFTS.put(profession, lootTable);
        });
    }

    public <B extends Block> Supplier<VillagerProfession> villagerProfession(String professionId, String poitId, List<Supplier<B>> jobSiteBlocks, Supplier<SoundEvent> workSound) {
        return new Register<>(() -> {
            log("Villager profession " + professionId + " with POIT " + poitId);
            var res = id(professionId);
            var poitKey = ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), id(poitId));
            var jobSites = jobSiteBlocks.stream().map(b -> (Block)b.get()).collect(ImmutableSet.toImmutableSet());
            var registered = Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, res, new VillagerProfession(
                res.toString(),
                holder -> holder.is(poitKey),
                holder -> holder.is(poitKey),
                ImmutableSet.of(),
                jobSites,
                workSound.get()
            ));
            loader.registerDeferred(() -> TRADES.put(registered, new Int2ObjectOpenHashMap<>()));
            return registered;
        });
    }

    /**
     * May be run late. Use this to conditionally add villager trades if the feature is enabled.
     */
    public void villagerTrade(Supplier<VillagerProfession> supplier, int tier, Supplier<ItemListing> trade) {
        var profession = supplier.get();
        var trades = getMutableTrades(profession);
        log("Villager trade for profession " + profession.name());
        trades.get(tier).add(trade.get());
        reassembleTrades(profession, trades);
    }

    public <W extends WallHangingSignBlock, S extends CeilingHangingSignBlock> Register<CharmWallHangingSignBlock> wallHangingSignBlock(String id, CustomWoodMaterial material, Supplier<S> drops, WoodType type) {
        return block(id, () -> new CharmWallHangingSignBlock(material, drops.get(), type));
    }

    public <W extends WallSignBlock, S extends SignBlock> Register<CharmWallSignBlock> wallSignBlock(String id, CustomWoodMaterial material, Supplier<S> drops, WoodType type) {
        return block(id, () -> new CharmWallSignBlock(material, drops.get(), type));
    }

    /**
     * May be run late. Use this to conditionally add trades to a wandering trade if the feature is enabled.
     */
    public void wandererTrade(Supplier<VillagerTrades.ItemListing> supplier, boolean isRare) {
        List<VillagerTrades.ItemListing> trades = NonNullList.create();
        int index = isRare ? 2 : 1;

        trades.addAll(Arrays.asList(WANDERING_TRADER_TRADES.get(index)));
        trades.add(supplier.get());

        WANDERING_TRADER_TRADES.put(index, trades.toArray(new VillagerTrades.ItemListing[0]));
    }

    public Register<WoodType> woodType(Supplier<CustomWoodMaterial> material) {
        return new Register<>(() -> {
            var materialName = material.get().getSerializedName();
            log("Wood type " + materialName);
            return WoodType.register(new WoodType(
                id(materialName).toString().replace(":", "_"),
                material.get().blockSetType()));
        });
    }

    private <T extends LivingEntity> void makeAttributeInstancesMutable(EntityType<T> entityType) {
        var attributes = DefaultAttributes.getSupplier(entityType);
        if (attributes.instances instanceof LinkedHashMap<Holder<Attribute>, AttributeInstance>) {
            return; // No action needed.
        }

        log.warnIfDebug("Making attribute instances mutable for entity type " + entityType.getDescriptionId());
        attributes.instances = new LinkedHashMap<>(attributes.instances);
    }

    private Int2ObjectMap<List<ItemListing>> getMutableTrades(VillagerProfession profession) {
        var fixedTrades = TRADES.get(profession);
        Int2ObjectMap<List<ItemListing>> mutable = new Int2ObjectOpenHashMap<>();

        for (int i = 1; i <= 5; i++) {
            mutable.put(i, NonNullList.create());
        }

        fixedTrades.int2ObjectEntrySet().forEach(e
            -> Arrays.stream(e.getValue())
            .forEach(a -> mutable.get(e.getIntKey()).add(a)));

        return mutable;
    }

    private void reassembleTrades(VillagerProfession profession, Int2ObjectMap<List<ItemListing>> trades) {
        Int2ObjectMap<ItemListing[]> mappedTrades = new Int2ObjectOpenHashMap<>();
        trades.int2ObjectEntrySet().forEach(e
            -> mappedTrades.put(e.getIntKey(), e.getValue().toArray(new ItemListing[0])));

        log.warnIfDebug("Reassembling trades for profession " + profession.name());
        TRADES.put(profession, mappedTrades);
    }

    public class Register<R> implements Supplier<R> {
        private final Supplier<R> supplier;
        private R instance;

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

    public static class Late {

    }
}
