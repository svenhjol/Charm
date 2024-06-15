package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.charmony.Resolve;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockEntity extends net.minecraft.world.level.block.entity.BlockEntity {
    private static final TotemOfPreserving TOTEM_OF_PRESERVING = Resolve.feature(TotemOfPreserving.class);
    private static final String OWNER_TAG = "owner";
    private static final String ITEMS_TAG = "items";
    private static final String MESSAGE_TAG = "message";
    private static final String DAMAGE_TAG = "damage";

    private List<ItemStack> items = new ArrayList<>();
    private String message;
    private UUID owner;
    private float rotateTicks = 0f;
    private int damage = 0;

    public BlockEntity(BlockPos pos, BlockState state) {
        super(TOTEM_OF_PRESERVING.registers.blockEntity.get(), pos, state);
    }

    public float getRotateTicks() {
        return rotateTicks;
    }

    public void setRotateTicks(float rotateTicks) {
        this.rotateTicks = rotateTicks;
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
        damage = tag.getInt(DAMAGE_TAG);
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
        tag.putInt(DAMAGE_TAG, damage);
    }

    public void setDirty() {
        var blockState = this.getBlockState();
        if (level != null) {
            level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(blockState));
        }
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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

    public int getDamage() {
        return damage;
    }
}
