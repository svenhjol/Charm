package svenhjol.charm.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import svenhjol.charm.block.CookingPotBlock;
import svenhjol.charm.item.MixedStewItem;
import svenhjol.charm.module.CookingPots;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CookingPotBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public static final String PORTIONS_NBT = "Portions";
    public static final String HUNGER_NBT = "Hunger";
    public static final String SATURATION_NBT = "Saturation";
    public static final String CONTENTS_NBT = "Contents";

    public int portions = 0;
    public float hunger = 0.0F;
    public float saturation = 0.0F;
    public List<Identifier> contents = new ArrayList<>();

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(CookingPots.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.portions = nbt.getInt(PORTIONS_NBT);
        this.hunger = nbt.getFloat(HUNGER_NBT);
        this.saturation = nbt.getFloat(SATURATION_NBT);
        this.contents = new ArrayList<>();
        NbtList list = nbt.getList(CONTENTS_NBT, 8);
        list.stream()
            .map(NbtElement::asString)
            .map(i -> i.replace("\"", "")) // madness
            .forEach(item -> this.contents.add(new Identifier(item)));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt(PORTIONS_NBT, this.portions);
        nbt.putFloat(HUNGER_NBT, this.hunger);
        nbt.putFloat(SATURATION_NBT, this.saturation);
        NbtList list = new NbtList();
        this.contents.forEach(item -> {
            list.add(NbtString.of(item.toString()));
        });
        nbt.put(CONTENTS_NBT, list);

        return nbt;
    }

    @Override
    public void fromClientTag(NbtCompound nbt) {
        readNbt(nbt);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound nbtCompound) {
        return writeNbt(nbtCompound);
    }

    public boolean add(World world, BlockPos pos, BlockState state, ItemStack food) {
        if (!food.isFood())
            return false;

        FoodComponent foodComponent = food.getItem().getFoodComponent();
        if (foodComponent == null)
            return false;

        if (portions == 0)
            contents = new ArrayList<>();

        if (portions < 64) {
            portions++;
            int foodHunger = foodComponent.getHunger();
            float foodSaturation = foodComponent.getSaturationModifier();

            hunger = (float)Math.round(100 * (((portions - 1) * hunger) + foodHunger) / portions) / 100;
            saturation = (float)Math.round(100 * (((portions - 1) * saturation) + foodSaturation) / portions) / 100;
            Identifier id = Registry.ITEM.getId(food.getItem());

            if (id.toString().equals("minecraft:air"))
                throw new IllegalStateException("THIS SHOULD NOT HAPPEN WHY IS THIS HAPPENING OMFG");

            if (!contents.contains(id))
                contents.add(id);

            markDirty();
            sync();

            food.decrement(1);
            world.setBlockState(pos, state.with(CookingPotBlock.HAS_LIQUID, 2), 2);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public ItemStack take(World world, BlockPos pos, BlockState state, ItemStack container) {
        // might support other containers in future
        if (container.getItem() != Items.BOWL)
            return null;

        if (portions > 0) {
            // create a stew from the pot's contents
            ItemStack stew = new ItemStack(CookingPots.MIXED_STEW);
            MixedStewItem.setHunger(stew, hunger);
            MixedStewItem.setSaturation(stew, saturation);
            MixedStewItem.setContents(stew, contents.stream().distinct().collect(Collectors.toList()));
            container.decrement(1);

            // if no more portions in the pot, flush out the pot data
            if (--portions <= 0)
                this.flush(world, pos, state);

            return stew;
        }

        return null;
    }

    public static <T extends CookingPotBlockEntity> void tick(World world, BlockPos pos, BlockState state, T cookingPot) {
        if (world == null || world.getTime() % 20 == 0)
            return;

        BlockState belowState = world.getBlockState(pos.down());
        Block belowBlock = belowState.getBlock();

        if (belowBlock == Blocks.FIRE
            || belowBlock == Blocks.SOUL_FIRE
            || (belowBlock == Blocks.CAMPFIRE && belowState.get(CampfireBlock.LIT))
            || (belowBlock == Blocks.SOUL_CAMPFIRE && belowState.get(CampfireBlock.LIT))
        ) {
            if (!state.get(CookingPotBlock.HAS_FIRE)) {
                world.setBlockState(pos, state.with(CookingPotBlock.HAS_FIRE, true), 3);
            }
        } else {
            if (state.get(CookingPotBlock.HAS_FIRE)) {
                world.setBlockState(pos, state.with(CookingPotBlock.HAS_FIRE, false), 3);
            }
        }
    }

    private void flush(World world, BlockPos pos, BlockState state) {
        this.contents = new ArrayList<>();
        this.hunger = 0;
        this.saturation = 0;
        this.portions = 0;
        world.setBlockState(pos, state.with(CookingPotBlock.HAS_LIQUID, 0), 2);

        markDirty();
        sync();
    }
}