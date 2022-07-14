package svenhjol.charm.module.totem_of_preserving;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import svenhjol.charm.helper.ItemNbtHelper;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.helper.TotemHelper;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TotemOfPreservingItem extends CharmItem {
    public static final String MESSAGE_TAG = "message";
    public static final String ITEMS_TAG = "items";
    public static final String XP_TAG = "xp";
    public static final String GLINT_TAG = "glint";

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
        return hasGlint(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack totem = user.getItemInHand(hand);

        // Don't break totem if it's empty.
        if (!TotemOfPreservingItem.hasItems(totem)) {
            return InteractionResultHolder.pass(totem);
        }

        var items = getItems(totem);
        var xp = getXp(totem);
        if (xp > 0) {
            level.playSound(null, user.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 1.0F);
            user.giveExperiencePoints(xp);
        }

        TotemHelper.destroy(user, totem);

        if (!level.isClientSide) {
            for (ItemStack stack : items) {
                var pos = user.blockPosition();
                var itemEntity = new ItemEntity(level, pos.getX(), pos.getY() + 0.5D, pos.getZ(), stack);
                level.addFreshEntity(itemEntity);
            }
        }

        return super.use(level, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        var message = getMessage(stack);
        var items = getItems(stack);

        if (!message.isEmpty())
            tooltip.add(TextHelper.literal(message));

        if (!items.isEmpty()) {
            var size = items.size();
            var str = size == 1 ? "totem.charm.preserving.item" : "totem.charm.preserving.items";
            tooltip.add(TextHelper.literal(I18n.get(str, size)));
        }

        super.appendHoverText(stack, level, tooltip, context);
    }

    public static void setMessage(ItemStack totem, String message) {
        ItemNbtHelper.setString(totem, MESSAGE_TAG, message);
    }

    public static void setItems(ItemStack totem, List<ItemStack> items) {
        var serialized = new CompoundTag();

        for (int i = 0; i < items.size(); i++) {
            var stack = items.get(i);
            serialized.put(Integer.toString(i), stack.save(new CompoundTag()));
        }

        ItemNbtHelper.setCompound(totem, ITEMS_TAG, serialized);
    }

    public static void setGlint(ItemStack totem, boolean shiny) {
        ItemNbtHelper.setBoolean(totem, GLINT_TAG, shiny);
    }

    public static void setXp(ItemStack totem, int xp) {
        ItemNbtHelper.setInt(totem, XP_TAG, xp);
    }

    public static String getMessage(ItemStack totem) {
        return ItemNbtHelper.getString(totem, MESSAGE_TAG, "");
    }

    public static boolean hasGlint(ItemStack totem) {
        return ItemNbtHelper.getBoolean(totem, GLINT_TAG, hasItems(totem));
    }

    public static List<ItemStack> getItems(ItemStack totem) {
        List<ItemStack> items = new ArrayList<>();
        var itemsTag = ItemNbtHelper.getCompound(totem, ITEMS_TAG);
        var keys = itemsTag.getAllKeys();

        for (var key : keys) {
            var tag = itemsTag.get(key);
            if (tag == null) {
                LogHelper.warn(TotemOfPreservingItem.class, "Missing item with key " + key);
                continue;
            }
            var stack = ItemStack.of((CompoundTag)tag);
            items.add(stack);
        }

        return items;
    }

    public static boolean hasItems(ItemStack totem) {
        return !getItems(totem).isEmpty();
    }

    public static int getXp(ItemStack totem) {
        return ItemNbtHelper.getInt(totem, XP_TAG, 0);
    }
}
