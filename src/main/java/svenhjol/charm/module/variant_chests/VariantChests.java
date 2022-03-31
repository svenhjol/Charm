package svenhjol.charm.module.variant_chests;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, priority = 10, description = "Chests available in all types of vanilla wood.")
public class VariantChests extends CharmModule {
    public static final ResourceLocation NORMAL_ID = new ResourceLocation(Charm.MOD_ID, "variant_chest");
    public static final ResourceLocation TRAPPED_ID = new ResourceLocation(Charm.MOD_ID, "trapped_chest");

    public static final Map<IWoodMaterial, VariantChestBlock> NORMAL_CHEST_BLOCKS = new HashMap<>();
    public static final Map<IWoodMaterial, VariantTrappedChestBlock> TRAPPED_CHEST_BLOCKS = new HashMap<>();

    public static BlockEntityType<VariantChestBlockEntity> NORMAL_BLOCK_ENTITY;
    public static BlockEntityType<VariantTrappedChestBlockEntity> TRAPPED_BLOCK_ENTITY;

    public static final ResourceLocation VARIANT_CHEST_BOAT_RECIPE_ID = new ResourceLocation(Charm.MOD_ID, "crafting_special_variantchestboats");
    public static Map<Item, Item> CHEST_BOATS = new HashMap<>();
    public static Map<String, Integer> CHEST_LAYER_COLORS = new HashMap<>();
    public static SimpleRecipeSerializer<VariantChestBoatRecipe> VARIANT_CHEST_BOAT_RECIPE;

    @Override
    public void register() {
        NORMAL_BLOCK_ENTITY = CommonRegistry.blockEntity(NORMAL_ID, VariantChestBlockEntity::new, NORMAL_CHEST_BLOCKS.values().toArray(new Block[0]));
        TRAPPED_BLOCK_ENTITY = CommonRegistry.blockEntity(TRAPPED_ID, VariantTrappedChestBlockEntity::new, TRAPPED_CHEST_BLOCKS.values().toArray(new Block[0]));

        for (VanillaWoodMaterial type : VanillaWoodMaterial.values()) {
            registerChest(this, type);
            registerTrappedChest(this, type);
        }

        VARIANT_CHEST_BOAT_RECIPE = CommonRegistry.recipeSerializer(VARIANT_CHEST_BOAT_RECIPE_ID.toString(), new SimpleRecipeSerializer<>(VariantChestBoatRecipe::new));

        CHEST_BOATS.put(Items.ACACIA_BOAT, Items.ACACIA_CHEST_BOAT);
        CHEST_BOATS.put(Items.BIRCH_BOAT, Items.BIRCH_CHEST_BOAT);
        CHEST_BOATS.put(Items.DARK_OAK_BOAT, Items.DARK_OAK_CHEST_BOAT);
        CHEST_BOATS.put(Items.JUNGLE_BOAT, Items.JUNGLE_CHEST_BOAT);
        CHEST_BOATS.put(Items.MANGROVE_BOAT, Items.MANGROVE_CHEST_BOAT);
        CHEST_BOATS.put(Items.OAK_BOAT, Items.OAK_CHEST_BOAT);
        CHEST_BOATS.put(Items.SPRUCE_BOAT, Items.SPRUCE_CHEST_BOAT);

        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.ACACIA.getSerializedName(), 0xda8357);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.BIRCH.getSerializedName(), 0xf7e1a5);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.CRIMSON.getSerializedName(), 0x9e5a76);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.DARK_OAK.getSerializedName(), 0x5e4932);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.JUNGLE.getSerializedName(), 0xca9974);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.MANGROVE.getSerializedName(), 0x9f6254);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.OAK.getSerializedName(), 0x71614b);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.SPRUCE.getSerializedName(), 0x9a7a54);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.WARPED.getSerializedName(), 0x59a3a2);
    }

    @Override
    public void runWhenEnabled() {
        UseEntityCallback.EVENT.register(this::handleEntityInteract);
    }

    private InteractionResult handleEntityInteract(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof AbstractChestedHorse horse) {
            ItemStack held = player.getItemInHand(hand);
            Item item = held.getItem();
            Block block = Block.byItem(item);
            if (block instanceof VariantChestBlock
                && horse.isTamed()
                && !horse.hasChest()
                && !horse.isBaby()) {
                horse.setChest(true);
                horse.playSound(SoundEvents.DONKEY_CHEST, 1.0f, (horse.getRandom().nextFloat() - horse.getRandom().nextFloat()) * 0.2f + 1.0f);
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
                horse.createInventory();
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public static VariantChestBlock registerChest(CharmModule module, IWoodMaterial material) {
        VariantChestBlock chest = new VariantChestBlock(module, material);
        NORMAL_CHEST_BLOCKS.put(material, chest);
        CommonRegistry.addBlocksToBlockEntity(NORMAL_BLOCK_ENTITY, chest);
        return chest;
    }

    public static VariantTrappedChestBlock registerTrappedChest(CharmModule module, IWoodMaterial material) {
        VariantTrappedChestBlock chest = new VariantTrappedChestBlock(module, material);
        TRAPPED_CHEST_BLOCKS.put(material, chest);
        CommonRegistry.addBlocksToBlockEntity(TRAPPED_BLOCK_ENTITY, chest);
        return chest;
    }

    public static int getLayerColor(ItemStack stack) {
        int color = 0xdf9f43; // this is the default color when there's no variant chest tag

        // TODO: Temporary hack so I can change colors in debug
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.ACACIA.getSerializedName(), 0xda8357);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.BIRCH.getSerializedName(), 0xffefa0);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.CRIMSON.getSerializedName(), 0x9f4a6f);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.DARK_OAK.getSerializedName(), 0x6b4224);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.JUNGLE.getSerializedName(), 0xd08f67);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.MANGROVE.getSerializedName(), 0xaf5944);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.OAK.getSerializedName(), 0xf0ba70);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.SPRUCE.getSerializedName(), 0x91683e);
        CHEST_LAYER_COLORS.put(VanillaWoodMaterial.WARPED.getSerializedName(), 0x408a82);
        CHEST_LAYER_COLORS.put(CharmWoodMaterial.AZALEA.getSerializedName(), 0xffaf9f);

        var tag = stack.getTag();
        if (tag != null && tag.contains(VariantChestBoatRecipe.CHEST_TYPE_TAG)) {
            var type = tag.getString(VariantChestBoatRecipe.CHEST_TYPE_TAG);
            color = CHEST_LAYER_COLORS.getOrDefault(type, color);
        }

        return color;
    }
}
