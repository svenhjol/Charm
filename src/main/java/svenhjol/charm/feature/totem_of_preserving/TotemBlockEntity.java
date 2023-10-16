package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TotemBlockEntity extends BlockEntity {
    static final String OWNER_TAG = "owner";
    static final String ITEMS_TAG = "items";
    static final String MESSAGE_TAG = "message";

    List<ItemStack> items = new ArrayList<>();
    String message;
    UUID owner;
    float rotateTicks = 0f;

    public TotemBlockEntity(BlockPos pos, BlockState state) {
        super(TotemOfPreserving.blockEntity.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.clear();

        var itemsList = tag.getList(ITEMS_TAG, 10);
        for (var t : itemsList) {
            var stack = ItemStack.of((CompoundTag) t);
            items.add(stack);
        }

        message = tag.getString(MESSAGE_TAG);
        owner = tag.getUUID(OWNER_TAG);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        var itemsList = new ListTag();
        for (ItemStack stack : items) {
            var saved = new CompoundTag();
            itemsList.add(stack.save(saved));
        }

        tag.put(ITEMS_TAG, itemsList);
        tag.putString(MESSAGE_TAG, message);
        tag.putUUID(OWNER_TAG, owner);
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }

    public UUID getOwner() {
        return owner;
    }
}
