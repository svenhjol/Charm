package svenhjol.meson;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public abstract class MesonTileInventory extends MesonTile
{
    protected String lootTable;
    protected int lootSize;

    public abstract ItemStackHandler getInventory();

    public abstract int getInventorySize();

    public void generateLoot(@Nullable EntityPlayer player)
    {
        if (!this.world.isRemote && hasLootTable()) {
            ResourceLocation res = new ResourceLocation(lootTable);
            LootTable table = world.getLootTableManager().getLootTableFromLocation(res);
            LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
            if (player != null) builder.withLuck(player.getLuck());

            LootContext context = builder.build();
            List<ItemStack> list = generateLootForPools(table, world.rand, context);

            List<Integer> slots = new ArrayList<>();
            for (int i = 0; i < getInventorySize(); i++) {
                slots.add(i);
            }
            Collections.shuffle(slots);

            int i = 0;
            for (ItemStack item : list) {
                if (i >= getInventorySize()) continue;
                getInventory().setStackInSlot(slots.get(i++), item);
            }

            this.lootTable = null;
            this.lootSize = 0;
        }
    }

    protected List<ItemStack> generateLootForPools(LootTable table, Random rand, LootContext context)
    {
        List<ItemStack> loot = table.generateLootForPools(rand, context);

        if (lootSize > 0) {
            int i = 0;
            while (loot.size() < lootSize && i++ < 10) {
                loot.addAll(table.generateLootForPools(rand, context));
            }

            if (loot.size() > lootSize) {
                loot = loot.subList(0, lootSize);
            }
        }

        return loot;
    }

    public int getComparatorOutput()
    {
        float f = 0;
        int i = 0;
        ItemStackHandler inventory = getInventory();

        for (int slot = 0; slot < getInventorySize(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                f += (float)stack.getCount() / (float)Math.min(inventory.getSlotLimit(slot), stack.getMaxStackSize());
                ++i;
            }
        }

        f = f / (float)getInventorySize();

        return MathHelper.floor(f * 14.0) + (i > 0 ? 1 : 0);
    }

    @Override
    public void readTag(NBTTagCompound tag)
    {
        super.readTag(tag);
        getInventory().deserializeNBT(tag.getCompoundTag("inventory"));
        setLootTable(tag.getString("lootTable"));
        setLootSize(tag.getInteger("lootSize"));
    }

    @Override
    public void writeTag(NBTTagCompound tag)
    {
        super.writeTag(tag);
        tag.setTag("inventory", getInventory().serializeNBT());
        tag.setInteger("lootSize", getLootSize());
        tag.setString("lootTable", getLootTable());
    }

    public boolean hasLootTable()
    {
        return lootTable != null && !lootTable.isEmpty();
    }

    public String getLootTable()
    {
        return hasLootTable() ? lootTable : "";
    }

    public void setLootTable(ResourceLocation lootTable)
    {
        this.lootTable = lootTable.toString();
    }

    public void setLootTable(ResourceLocation lootTable, int lootSize)
    {
        setLootTable(lootTable.toString(), lootSize);
    }

    public void setLootTable(ResourceLocation lootTable, long seed)
    {
        setLootTable(lootTable.toString());
    }

    public void setLootTable(String lootTable)
    {
        this.lootTable = lootTable;
    }

    public void setLootTable(String lootTable, int lootSize)
    {
        this.setLootTable(lootTable);
        this.setLootSize(lootSize);
    }

    public int getLootSize()
    {
        return this.lootSize;
    }

    public void setLootSize(int size)
    {
        this.lootSize = size;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        //noinspection unchecked
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)getInventory() : super.getCapability(capability, facing);
    }
}
