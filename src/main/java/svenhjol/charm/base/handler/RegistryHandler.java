package svenhjol.charm.base.handler;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.mixin.accessor.BrewingRecipeRegistryAccessor;
import svenhjol.charm.mixin.accessor.DefaultParticleTypeAccessor;
import svenhjol.charm.mixin.accessor.SignTypeAccessor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public class RegistryHandler {
    public static List<String> SUPPRESS_DATA_FIXER_ERROR = new ArrayList<>();

    public static Block block(Identifier id, Block block) {
        return Registry.register(Registry.BLOCK, id, block);
    }

    public static <T extends BlockEntity> BlockEntityType<T> blockEntity(Identifier id, BlockEntityType.BlockEntityFactory<T> builder, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, BlockEntityType.Builder.create(builder, blocks).build(null));
    }

    public static void brewingRecipe(Potion input, Item reagant, Potion output) {
        BrewingRecipeRegistryAccessor.invokeRegisterPotionRecipe(input, reagant, output);
    }

    public static ConfiguredFeature<?, ?> configuredFeature(Identifier id, ConfiguredFeature<?, ?> configuredFeature) {
        RegistryKey<ConfiguredFeature<?, ?>> key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, key.getValue(), configuredFeature);
        return configuredFeature;
    }

    public static ConfiguredStructureFeature<?, ?> configuredStructureFeature(Identifier id, ConfiguredStructureFeature<?, ?> configuredFeature) {
        RegistryKey<ConfiguredStructureFeature<?, ?>> key = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, id);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, key.getValue(), configuredFeature);
        return configuredFeature;
    }

    public static DefaultParticleType defaultParticleType(Identifier id) {
        DefaultParticleType type = DefaultParticleTypeAccessor.invokeConstructor(false);
        return Registry.register(Registry.PARTICLE_TYPE, id.toString(), type);
    }

    public static Enchantment enchantment(Identifier id, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, id, enchantment);
    }

    public static <T extends Entity> EntityType<T> entity(Identifier id, FabricEntityTypeBuilder<T> build) {
        SUPPRESS_DATA_FIXER_ERROR.add(id.toString());
        EntityType<T> entityType = build.build();
        return Registry.register(Registry.ENTITY_TYPE, id, entityType);
    }

    public static Item item(Identifier id, Item item) {
        return Registry.register(Registry.ITEM, id, item);
    }

    public static LootFunctionType lootFunctionType(Identifier id, LootFunctionType lootFunctionType) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, id, lootFunctionType);
    }

    public static SignType signType(Identifier id) {
        // crashes when using fully qualified namespace, so convert colon to underscore
        return SignTypeAccessor.invokeRegister(SignTypeAccessor.invokeInit(id.toString().replace(":", "_")));
    }

    public static PointOfInterestType pointOfInterestType(Identifier id, PointOfInterestType poit) {
        return Registry.register(Registry.POINT_OF_INTEREST_TYPE, id, poit);
    }

    public static Potion potion(Identifier id, Potion potion) {
        return Registry.register(Registry.POTION, id, potion);
    }

    public static <T extends Recipe<?>> RecipeType<T> recipeType(String recipeId) {
        return RecipeType.register(recipeId);
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S recipeSerializer(String recipeId, S serializer) {
        return RecipeSerializer.register(recipeId, serializer);
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> screenHandler(Identifier id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registry.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory));
    }

    public static <H extends ScreenHandler, S extends Screen & ScreenHandlerProvider<H>> void screenHandlerClient(ScreenHandlerType<H> screenHandler, ScreenRegistry.Factory<H, S> screen) {
        ScreenRegistry.register(screenHandler, screen);
    }

    public static SoundEvent sound(Identifier id, SoundEvent sound) {
        return Registry.register(Registry.SOUND_EVENT, id, sound);
    }

    public static StatusEffect statusEffect(Identifier id, StatusEffect statusEffect) {
        return Registry.register(Registry.STATUS_EFFECT, id, statusEffect);
    }

    public static StructurePieceType structurePiece(Identifier id, StructurePieceType structurePieceType) {
        return Registry.register(Registry.STRUCTURE_PIECE, id, structurePieceType);
    }

    public static VillagerProfession villagerProfession(Identifier id, VillagerProfession profession) {
        return Registry.register(Registry.VILLAGER_PROFESSION, id, profession);
    }
}
