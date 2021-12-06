package svenhjol.charm.module.totem_of_preserving;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import svenhjol.charm.helper.NbtHelper;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.TotemHelper;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class TotemOfPreservingItem extends CharmItem {
    public static final String MESSAGE_TAG = "message";
    public static final String ITEMS_TAG = "items";
    public static final String XP_TAG = "xp";

    public TotemOfPreservingItem(CharmModule module) {
        super(module, "totem_of_preserving", new Item.Properties()
            .tab(CreativeModeTab.TAB_MISC)
            .rarity(Rarity.UNCOMMON)
            .fireResistant()
            .stacksTo(1));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return hasItems(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack totem = user.getItemInHand(hand);
        CompoundTag items = getItems(totem);
        int xp = getXp(totem);
        if (xp > 0) {
            level.playSound(null, user.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 1.0F);
            user.giveExperiencePoints(xp);
        }

        TotemHelper.destroy(user, totem);

        if (!level.isClientSide) {
            Set<String> keys = items.getAllKeys();

            keys.forEach(k -> {
                Tag tag = items.get(k);
                if (tag == null) {
                    LogHelper.warn(this.getClass(), "Item tag missing from totem");
                } else {
                    ItemStack stack = ItemStack.of((CompoundTag) tag);
                    BlockPos pos = user.blockPosition();
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY() + 0.5D, pos.getZ(), stack);
                    level.addFreshEntity(itemEntity);
                }
            });
        }

        return super.use(level, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        String message = getMessage(stack);
        CompoundTag items = getItems(stack);

        if (!message.isEmpty())
            tooltip.add(new TextComponent(message));

        if (!items.isEmpty()) {
            int size = items.size();
            String str = size == 1 ? "totem.charm.preserving.item" : "totem.charm.preserving.items";
            tooltip.add(new TextComponent(I18n.get(str, size)));
        }

        super.appendHoverText(stack, level, tooltip, context);
    }

    public static void setMessage(ItemStack totem, String message) {
        NbtHelper.setString(totem, MESSAGE_TAG, message);
    }

    public static void setItems(ItemStack totem, CompoundTag items) {
        NbtHelper.setCompound(totem, ITEMS_TAG, items);
    }

    public static void setXp(ItemStack totem, int xp) {
        NbtHelper.setInt(totem, XP_TAG, xp);
    }

    public static String getMessage(ItemStack totem) {
        return NbtHelper.getString(totem, MESSAGE_TAG, "");
    }

    public static CompoundTag getItems(ItemStack totem) {
        return NbtHelper.getCompound(totem, ITEMS_TAG);
    }

    public static boolean hasItems(ItemStack totem) {
        return !getItems(totem).isEmpty();
    }

    public static int getXp(ItemStack totem) {
        return NbtHelper.getInt(totem, XP_TAG, 0);
    }
}
