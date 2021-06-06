package svenhjol.charm.module.cooking_pots;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.cooking_pots.CookingPotBlock;
import svenhjol.charm.module.cooking_pots.CookingPots;
import svenhjol.charm.module.cooking_pots.MixedStewItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CookingPotBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public static final int MAX_PORTIONS = 64;

    public static final String PORTIONS_NBT = "Portions";
    public static final String HUNGER_NBT = "Hunger";
    public static final String SATURATION_NBT = "Saturation";
    public static final String CONTENTS_NBT = "Contents";
    public static final String NAME_NBT = "Name";

    public String name = "";
    public int portions = 0;
    public float hunger = 0.0F;
    public float saturation = 0.0F;
    public List<ResourceLocation> contents = new ArrayList<>();

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(svenhjol.charm.module.cooking_pots.CookingPots.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.name = nbt.getString(NAME_NBT);
        this.portions = nbt.getInt(PORTIONS_NBT);
        this.hunger = nbt.getFloat(HUNGER_NBT);
        this.saturation = nbt.getFloat(SATURATION_NBT);
        this.contents = new ArrayList<>();
        ListTag list = nbt.getList(CONTENTS_NBT, 8);
        list.stream()
            .map(Tag::getAsString)
            .map(i -> i.replace("\"", "")) // madness
            .forEach(item -> this.contents.add(new ResourceLocation(item)));
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);

        nbt.putString(NAME_NBT, this.name);
        nbt.putInt(PORTIONS_NBT, this.portions);
        nbt.putFloat(HUNGER_NBT, this.hunger);
        nbt.putFloat(SATURATION_NBT, this.saturation);
        ListTag list = new ListTag();
        this.contents.forEach(item -> {
            list.add(StringTag.valueOf(item.toString()));
        });
        nbt.put(CONTENTS_NBT, list);

        return nbt;
    }

    @Override
    public void fromClientTag(CompoundTag nbt) {
        load(nbt);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag nbtCompound) {
        return save(nbtCompound);
    }

    public boolean add(Level world, BlockPos pos, BlockState state, ItemStack food) {
        if (!food.isEdible())
            return false;

        FoodProperties foodComponent = food.getItem().getFoodProperties();
        if (foodComponent == null)
            return false;

        if (portions < MAX_PORTIONS) {

            // reset contents if fresh pot
            if (portions == 0)
                contents = new ArrayList<>();

            portions++;
            int foodHunger = foodComponent.getNutrition();
            float foodSaturation = foodComponent.getSaturationModifier();

            hunger = (float)Math.round(100 * (((portions - 1) * hunger) + foodHunger) / portions) / 100;
            saturation = (float)Math.round(100 * (((portions - 1) * saturation) + foodSaturation) / portions) / 100;
            ResourceLocation id = Registry.ITEM.getKey(food.getItem());

            if (id.toString().equals("minecraft:air"))
                throw new IllegalStateException("THIS SHOULD NOT HAPPEN WHY IS THIS HAPPENING OMFG");

            if (!contents.contains(id))
                contents.add(id);

            setChanged();
            sync();

            food.shrink(1);
            world.setBlock(pos, state.setValue(svenhjol.charm.module.cooking_pots.CookingPotBlock.LIQUID, 2), 2);
            return true;
        }

        return false;
    }

    @Nullable
    public ItemStack take(Level world, BlockPos pos, BlockState state, ItemStack container) {
        // might support other containers in future
        if (container.getItem() != Items.BOWL)
            return null;

        if (portions > 0) {
            // create a stew from the pot's contents
            ItemStack stew = new ItemStack(CookingPots.MIXED_STEW);
            svenhjol.charm.module.cooking_pots.MixedStewItem.setHunger(stew, hunger);
            svenhjol.charm.module.cooking_pots.MixedStewItem.setSaturation(stew, saturation);
            MixedStewItem.setContents(stew, contents.stream().distinct().collect(Collectors.toList()));
            container.shrink(1);

            // if no more portions in the pot, flush out the pot data
            if (--portions <= 0)
                this.flush(world, pos, state);

            // match cooking pot name if set
            if (!name.isEmpty())
                stew.setHoverName(new TextComponent(name));

            return stew;
        }

        return null;
    }

    public static <T extends CookingPotBlockEntity> void tick(Level world, BlockPos pos, BlockState state, T cookingPot) {
        if (world == null || world.getGameTime() % 20 == 0)
            return;

        BlockState belowState = world.getBlockState(pos.below());
        Block belowBlock = belowState.getBlock();

        if (belowBlock == Blocks.FIRE
            || belowBlock == Blocks.SOUL_FIRE
            || (belowBlock == Blocks.CAMPFIRE && belowState.getValue(CampfireBlock.LIT))
            || (belowBlock == Blocks.SOUL_CAMPFIRE && belowState.getValue(CampfireBlock.LIT))
        ) {
            if (!state.getValue(svenhjol.charm.module.cooking_pots.CookingPotBlock.HAS_FIRE)) {
                world.setBlock(pos, state.setValue(svenhjol.charm.module.cooking_pots.CookingPotBlock.HAS_FIRE, true), 3);
            }
        } else {
            if (state.getValue(svenhjol.charm.module.cooking_pots.CookingPotBlock.HAS_FIRE)) {
                world.setBlock(pos, state.setValue(svenhjol.charm.module.cooking_pots.CookingPotBlock.HAS_FIRE, false), 3);
            }
        }
    }

    private void flush(Level world, BlockPos pos, BlockState state) {
        this.contents = new ArrayList<>();
        this.hunger = 0;
        this.saturation = 0;
        this.portions = 0;
        world.setBlock(pos, state.setValue(CookingPotBlock.LIQUID, 0), 2);

        setChanged();
        sync();
    }
}