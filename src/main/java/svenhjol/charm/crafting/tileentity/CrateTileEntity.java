package svenhjol.charm.crafting.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.CrateOpenBlock;
import svenhjol.charm.crafting.container.CrateContainer;
import svenhjol.meson.iface.IMesonTileEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class CrateTileEntity extends LockableLootTileEntity implements ISidedInventory, IMesonTileEntity
{
    private static final int[] SLOTS = IntStream.range(0, 9).toArray();
    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    @ObjectHolder("charm:crate")
    public static TileEntityType<?> CRATE;

    public CrateTileEntity()
    {
        super(CRATE);
    }

    public static Supplier<? extends TileEntity> factory()
    {
        return CrateTileEntity::new;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public String getBaseName()
    {
        return "crate";
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        loadFromNBT(tag);
    }

    public void loadFromNBT(CompoundNBT tag)
    {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(tag) && tag.contains("Items")) {
            ItemStackHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        return writeToNBT(tag);
    }

    public CompoundNBT writeToNBT(CompoundNBT tag)
    {
        if (!this.checkLootAndRead(tag)) {
            ItemStackHelper.saveAllItems(tag, this.items, false);
        }
        return tag;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, Direction direction)
    {
        return true;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack stack, @Nullable Direction direction)
    {
        /* @todo still possible to insert crates into crates */
        Class<? extends Block> blockClass = Block.getBlockFromItem(stack.getItem()).getClass();
        List<Class<? extends Block>> invalid = new ArrayList<>(Arrays.asList(ShulkerBoxBlock.class, CrateOpenBlock.class)); /* @todo Add crate here */
        return !(invalid.contains(blockClass));
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new CrateContainer(id, player, this);
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile.charm.crate");
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        return SLOTS;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        if (!player.isSpectator()) {
            this.world.playSound(null, this.pos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        this.world.playSound(null, this.pos, SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1F + 0.9F);
    }
}
