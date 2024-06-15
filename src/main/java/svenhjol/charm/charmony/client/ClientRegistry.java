package svenhjol.charm.charmony.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.client.deferred.DeferredParticle;
import svenhjol.charm.charmony.helper.EnumHelper;
import svenhjol.charm.charmony.iface.PacketHandler;
import svenhjol.charm.charmony.iface.PacketRequest;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("UnusedReturnValue")
public final class ClientRegistry implements svenhjol.charm.charmony.Registry {
    private final Map<String, KeyMapping> keyMappings = new HashMap<>();
    private static final List<Pair<String, ItemLike>> RECIPE_BOOK_CATEGORY_ENUMS = new ArrayList<>();
    private static final Map<RecipeBookType, List<RecipeBookCategories>> RECIPE_BOOK_CATEGORY_BY_TYPE = new HashMap<>();
    private static final Map<RecipeType<?>, RecipeBookCategories> RECIPE_BOOK_MAIN_CATEGORY = new HashMap<>();
    private static final List<DeferredParticle> PARTICLES = new ArrayList<>();

    private final ClientLoader loader;
    private final Log log;

    public ClientRegistry(ClientLoader loader) {
        this.loader = loader;
        this.log = new Log(loader.id(), this);
    }

    @Override
    public String id() {
        return loader.id();
    }

    @Override
    public ResourceLocation id(String path) {
        return loader.id(path);
    }

    public void blockColor(BlockColor blockColor, List<Supplier<? extends Block>> blocks) {
        ColorProviderRegistry.BLOCK.register(blockColor, blocks.stream().map(Supplier::get).toList().toArray(Block[]::new));
    }

    public <T extends BlockEntity> void blockEntityRenderer(Supplier<BlockEntityType<T>> supplier, Supplier<BlockEntityRendererProvider<T>> provider) {
        loader.registerDeferred(() -> BlockEntityRenderers.register(supplier.get(), provider.get()));
    }

    public <T extends Block> void blockRenderType(Supplier<T> block, Supplier<RenderType> renderType) {
        loader.registerDeferred(() -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType.get()));
    }

    public <T extends Entity> void entityRenderer(Supplier<EntityType<T>> entity, Supplier<EntityRendererProvider<T>> provider) {
        loader.registerDeferred(() -> EntityRendererRegistry.register(entity.get(), provider.get()));
    }

    public void itemColor(List<Supplier<? extends ItemLike>> items) {
        ColorProviderRegistry.ITEM.register(this::handleItemColor, items.stream().map(Supplier::get).toList().toArray(ItemLike[]::new));
    }

    @SuppressWarnings("deprecation")
    public <T extends Item> void itemProperties(String id, Supplier<T> item, Supplier<ItemPropertyFunction> function) {
        var itemPropertyFunction = function.get();
        var clampedItemPropertyFunction = new ClampedItemPropertyFunction() {
            @Override
            public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                return itemPropertyFunction.call(itemStack, clientLevel, livingEntity, i);
            }

            @Override
            public float call(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                return itemPropertyFunction.call(itemStack, clientLevel, livingEntity, i);
            }
        };
        ItemProperties.register(item.get(), id(id), clampedItemPropertyFunction);
    }

    /**
     * May be run late. Use this to conditionally add and item to the creative menu if the feature is enabled.
     */
    public <T extends ItemLike> void itemTab(Supplier<T> item, ResourceKey<CreativeModeTab> key, @Nullable ItemLike showAfter) {
        if (showAfter != null) {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.addAfter(showAfter, item.get()));
        } else {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.accept(item.get()));
        }
    }

    public Supplier<String> key(String id, Supplier<KeyMapping> supplier) {
        Supplier<KeyMapping> mapping = () -> KeyBindingHelper.registerKeyBinding(supplier.get());
        loader.registerDeferred(() -> keyMappings.put(id, mapping.get()));
        return () -> id;
    }

    public Map<String, KeyMapping> keyMappings() {
        return keyMappings;
    }

    public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void menuScreen(Supplier<MenuType<T>> menuType, Supplier<MenuScreens.ScreenConstructor<T, U>> screenConstructor) {
        loader.registerDeferred(() -> MenuScreens.register(menuType.get(), screenConstructor.get()));
    }

    public Supplier<ModelLayerLocation> modelLayer(Supplier<ModelLayerLocation> location, Supplier<LayerDefinition> definition) {
        loader.registerDeferred(() -> EntityModelLayerRegistry.registerModelLayer(location.get(), definition::get));
        return location;
    }

    public <T extends PacketRequest, U extends PacketHandler<T>> Supplier<ResourceLocation> clientPacketReceiver(T packet, Supplier<U> handler) {
        ResourceLocation id = packet.id();
        loader.registerDeferred(() -> ClientPlayNetworking.registerGlobalReceiver(packet.id(), (client, listener, buf, sender) -> {
            packet.decode(buf);
            client.execute(() -> ((PacketHandler)handler.get()).handle(packet, Minecraft.getInstance().player));
        }));
        return () -> id;
    }

    public Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particle(Supplier<SimpleParticleType> particleType,
                                                                                            Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particleProvider) {
        loader.registerDeferred(() -> PARTICLES.add(new DeferredParticle(particleType, particleProvider)));
        return particleProvider;
    }

    /**
     * Particles are registered via mixin.
     * @see svenhjol.charm.charmony.client.mixin.registry.particle_engine.ParticleEngineMixin
     */
    public static List<DeferredParticle> particles() {
        return PARTICLES;
    }

    public <R extends Recipe<?>> void recipeBookCategory(String id, Supplier<RecipeType<R>> recipeType, Supplier<RecipeBookType> recipeBookType) {
        loader.registerDeferred(() -> {
            var upper = id.toUpperCase(Locale.ROOT);
            var searchCategory = EnumHelper.getValueOrDefault(() -> RecipeBookCategories.valueOf(upper + "_SEARCH"), RecipeBookCategories.CRAFTING_SEARCH);
            var mainCategory = EnumHelper.getValueOrDefault(() -> RecipeBookCategories.valueOf(upper), RecipeBookCategories.CRAFTING_MISC);
            
            RECIPE_BOOK_MAIN_CATEGORY.put(recipeType.get(), mainCategory);
            RECIPE_BOOK_CATEGORY_BY_TYPE.put(recipeBookType.get(), List.of(searchCategory, mainCategory));

            var aggregateCategories = new HashMap<>(RecipeBookCategories.AGGREGATE_CATEGORIES);
            aggregateCategories.put(searchCategory, List.of(mainCategory));
            RecipeBookCategories.AGGREGATE_CATEGORIES = aggregateCategories;
        });
    }

    public void recipeBookCategoryEnum(String name, Supplier<? extends ItemLike> menuIcon) {
        RECIPE_BOOK_CATEGORY_ENUMS.add(Pair.of(name, menuIcon.get()));
    }

    public static List<Pair<String, ItemLike>> recipeBookCategoryEnums() {
        return RECIPE_BOOK_CATEGORY_ENUMS;
    }

    public static Map<RecipeBookType, List<RecipeBookCategories>> recipeBookCategoryByType() {
        return RECIPE_BOOK_CATEGORY_BY_TYPE;
    }

    public static Map<RecipeType<?>, RecipeBookCategories> recipeBookMainCategory() {
        return RECIPE_BOOK_MAIN_CATEGORY;
    }

    public void signMaterial(Supplier<WoodType> woodType) {
        loader.registerDeferred(() -> {
            Sheets.SIGN_MATERIALS.put(woodType.get(), new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + woodType.get().name())));
            Sheets.HANGING_SIGN_MATERIALS.put(woodType.get(), new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/hanging/" + woodType.get().name())));
        });
    }

    private int handleItemColor(ItemStack stack, int tintIndex) {
        var state = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
        var blockColors = Minecraft.getInstance().getBlockColors();
        return blockColors.getColor(state, null, null, tintIndex);
    }

    public int handleLeavesColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        return level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.getDefaultColor();
    }
}
