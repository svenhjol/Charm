package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.base.CharmonyItem;
import svenhjol.charmony.feature.colored_glints.ColoredGlints;
import svenhjol.charmony.helper.ClientEffectHelper;
import svenhjol.charmony.helper.ItemNbtHelper;
import svenhjol.charmony.helper.TextHelper;
import svenhjol.charmony.helper.TotemHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TotemItem extends CharmonyItem {
    static final String MESSAGE_TAG = "message";
    static final String ITEMS_TAG = "items";
    static final String GLINT_TAG = "glint";

    public TotemItem(CharmonyFeature feature) {
        super(feature, new Item.Properties()
            .stacksTo(1)
            .durability(TotemOfPreserving.durability)
            .rarity(Rarity.UNCOMMON));
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
    public boolean isValidRepairItem(ItemStack item, ItemStack repair) {
        return TotemOfPreserving.durability > 1 && !TotemOfPreserving.graveMode
            && (repair.is(Items.ECHO_SHARD) || super.isValidRepairItem(item, repair));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var totem = player.getItemInHand(hand);
        var pos = player.blockPosition();
        var destroyTotem = false;

        // Don't break totem if it's empty.
        if (!hasItems(totem)) {
            var newTotem = givePlayerCleanTotem(player, hand);
            newTotem.setDamageValue(totem.getDamageValue());
            return InteractionResultHolder.pass(newTotem);
        }

        var items = getItems(totem);
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6f, 1.0f);

        if (TotemOfPreserving.graveMode) {

            // Always destroy totem in Grave Mode.
            destroyTotem = true;

        } else if (TotemOfPreserving.durability <= 0) {

            givePlayerCleanTotem(player, hand);

        } else {

            // Durability config of 1 or more causes damage to the totem.
            var damage = totem.getDamageValue();
            if (damage < totem.getMaxDamage() && (totem.getMaxDamage() - damage > 1)) {

                var newTotem = givePlayerCleanTotem(player, hand);
                newTotem.setDamageValue(damage + 1);

            } else {
                destroyTotem = true;
            }
        }

        if (!destroyTotem) {
            level.playSound(null, pos, TotemOfPreserving.sound.get(), SoundSource.PLAYERS, 0.8f, 1.0f);
        } else {
            destroyTotem(totem, player);
        }

        if (!level.isClientSide) {

            // Add totem items to the world.
            for (var stack : items) {
                var itemEntity = new ItemEntity(level, pos.getX(), pos.getY() + 0.5d, pos.getZ(), stack);
                level.addFreshEntity(itemEntity);
            }

        }

        return super.use(level, player, hand);
    }

    private void destroyTotem(ItemStack stack, Player player) {
        if (!player.level().isClientSide) {
            TotemHelper.destroy(player, stack);
        } else {
            ClientEffectHelper.destroyTotem(player.blockPosition());
        }
    }

    private ItemStack givePlayerCleanTotem(Player player, InteractionHand hand) {
        var stack = new ItemStack(TotemOfPreserving.item.get());
        player.setItemInHand(hand, stack);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag context) {
        var message = getMessage(stack);
        var items = getItems(stack);

        if (!message.isEmpty())
            tooltip.add(TextHelper.literal(message));

        if (!items.isEmpty()) {
            var size = items.size();
            var str = size == 1 ? "totem_of_preserving.charm.item" : "totem_of_preserving.charm.items";
            tooltip.add(TextHelper.literal(I18n.get(str, size)));
        }

        super.appendHoverText(stack, level, tooltip, context);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        var heldItems = TotemItem.getItems(stack);
        if (heldItems.isEmpty()) {
            return Optional.empty();
        }
        NonNullList<ItemStack> items = NonNullList.create();
        items.addAll(heldItems);
        return Optional.of(new TotemOfPreservingTooltip(items));
    }

    public static void setMessage(ItemStack totem, String message) {
        ItemNbtHelper.setString(totem, MESSAGE_TAG, message);
    }

    public static void setItems(ItemStack totem, List<ItemStack> items) {
        var serialized = new CompoundTag();

        for (int i = 0; i < items.size(); i++) {
            var stack = items.get(i);
            if (stack.isEmpty()) continue;
            serialized.put(Integer.toString(i), stack.save(new CompoundTag()));
        }

        ItemNbtHelper.setCompound(totem, ITEMS_TAG, serialized);
    }

    public static void setGlint(ItemStack totem, boolean flag) {
        ItemNbtHelper.setBoolean(totem, GLINT_TAG, flag);
        ColoredGlints.applyColoredGlint(totem, DyeColor.CYAN);
    }

    public static void setDamage(ItemStack totem, int damage) {
        totem.setDamageValue(damage);
    }

    public static String getMessage(ItemStack totem) {
        return ItemNbtHelper.getString(totem, MESSAGE_TAG, "");
    }

    public static List<ItemStack> getItems(ItemStack totem) {
        List<ItemStack> items = new ArrayList<>();
        var log = Charm.instance().log();
        var itemsTag = ItemNbtHelper.getCompound(totem, ITEMS_TAG);
        var keys = itemsTag.getAllKeys();

        for (var key : keys) {
            var tag = itemsTag.get(key);
            if (tag == null) {
                log.warn(TotemItem.class, "Missing item with key " + key);
                continue;
            }
            var stack = ItemStack.of((CompoundTag)tag);
            if (stack.isEmpty()) {
                continue;
            }

            items.add(stack);
        }

        return items;
    }

    public static boolean hasItems(ItemStack totem) {
        return !getItems(totem).isEmpty();
    }

    public static boolean hasGlint(ItemStack totem) {
        return ItemNbtHelper.getBoolean(totem, GLINT_TAG, hasItems(totem));
    }
}
