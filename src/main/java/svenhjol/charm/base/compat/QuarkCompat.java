package svenhjol.charm.base.compat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.DyeColor;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.meson.Meson;
import svenhjol.meson.enums.ColorVariant;
import svenhjol.meson.enums.VanillaWoodType;
import svenhjol.meson.helper.ItemNBTHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.building.module.VariantChestsModule;
import vazkii.quark.tools.item.RuneItem;
import vazkii.quark.tools.module.AncientTomesModule;
import vazkii.quark.tools.module.ColorRunesModule;
import vazkii.quark.world.block.CaveCrystalBlock;
import vazkii.quark.world.module.BigDungeonModule;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuarkCompat implements IQuarkCompat {
    public boolean hasColorRuneModule() {
        return ModuleLoader.INSTANCE.isModuleEnabled(ColorRunesModule.class);
    }

    public boolean isRune(ItemStack stack) {
        return stack.getItem() instanceof RuneItem;
    }

    @Nullable
    public ColorVariant getRuneColor(ItemStack stack) {
        ColorVariant color = ColorVariant.WHITE;

        if (isRune(stack)) {
            RuneItem item = (RuneItem) stack.getItem();
            ResourceLocation itemRegName = item.getRegistryName();
            if (itemRegName == null)
                return null;

            String colorName = itemRegName.getPath().replace("_rune", "").toUpperCase();
            try {
                color = ColorVariant.valueOf(colorName);
            } catch (Exception e) {
                Meson.LOG.debug("Failed to get color of rune" + stack.getItem());
                return null;
            }
        }

        return color;
    }

    public ItemStack getRune(ColorVariant color) {
        ResourceLocation res = new ResourceLocation(Quark.MOD_ID, color.getName() + "_rune");
        Item runeItem = ForgeRegistries.ITEMS.getValue(res);
        return runeItem == null ? ItemStack.EMPTY : new ItemStack(runeItem);
    }

    public ItemStack getQuiltedWool(ColorVariant color) {
        if (Meson.isModuleEnabled(new ResourceLocation("quark:quilted_wool"))) {
            final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("quark:" + color.getName() + "_quilted_wool"));
            if (item != null) {
                return new ItemStack(item);
            }
        }
        return ItemStack.EMPTY;
    }

    public void applyColor(ItemStack stack, DyeColor color) {
        // get the rune
        Item runeItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Quark.MOD_ID, color.getName() + "_rune"));
        if (runeItem instanceof RuneItem) {
            ItemStack rune = new ItemStack(runeItem);
            ItemNBTHelper.setBoolean(stack, ColorRunesModule.TAG_RUNE_ATTACHED, true);
            ItemNBTHelper.setCompound(stack, ColorRunesModule.TAG_RUNE_COLOR, rune.serializeNBT());
        }
    }

    public ItemStack getRandomAncientTome(Random rand) {
        List<Enchantment> validEnchants = AncientTomesModule.validEnchants;
        ItemStack tome = new ItemStack(AncientTomesModule.ancient_tome);

        Enchantment enchantment = validEnchants.get(rand.nextInt(validEnchants.size()));
        EnchantedBookItem.addEnchantment(tome, new EnchantmentData(enchantment, enchantment.getMaxLevel()));

        return tome;
    }

    public boolean isInsideBigDungeon(ServerWorld world, BlockPos pos) {
        return BigDungeonModule.structure.isPositionInsideStructure(world, pos);
    }

    public boolean isCrystal(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof CaveCrystalBlock;
    }

    public boolean hasVariantChests() {
        return ModuleLoader.INSTANCE.isModuleEnabled(VariantChestsModule.class);
    }

    public boolean hasBigDungeons() {
        return ModuleLoader.INSTANCE.isModuleEnabled(BigDungeonModule.class);
    }

    public String getBigDungeonResName() {
        if (BigDungeonModule.structure != null) {
            return BigDungeonModule.structure.getStructureName();
        } else {
            return "quark:big_dungeon";
        }
    }

    public Structure<?> getBigDungeonStructure() {
        return BigDungeonModule.structure;
    }

    @Nullable
    public Block getRandomChest(Random rand) {
        List<VanillaWoodType> types = Arrays.asList(VanillaWoodType.values());
        VanillaWoodType type = types.get(rand.nextInt(types.size()));
        ResourceLocation res = new ResourceLocation(Quark.MOD_ID, type.name().toLowerCase() + "_chest");
        return ForgeRegistries.BLOCKS.getValue(res);
    }
}
