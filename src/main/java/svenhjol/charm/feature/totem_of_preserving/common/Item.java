package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import svenhjol.charm.api.enums.EventResult;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.totem_of_preserving.client.Tooltip;

import java.util.List;
import java.util.Optional;

public class Item extends net.minecraft.world.item.Item {
    private static final Handlers HANDLERS = TotemOfPreserving.handlers;

    public Item() {
        super(new Properties()
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
        return ColoredGlints.has(stack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack item, ItemStack repair) {
        return TotemOfPreserving.durability > 1 && !TotemOfPreserving.graveMode
            && (repair.is(Items.ECHO_SHARD) || super.isValidRepairItem(item, repair));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var result = HANDLERS.useTotemInHand(level, player, hand);

        if (result.result() == EventResult.PASS) {
            return result.asInteractionResultHolder();
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack totem, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        var data = Data.get(totem);

        if (!data.message().isEmpty()) {
            tooltip.add(Component.literal(data.message()));
        }

        if (!data.items().isEmpty()) {
            var size = data.items().size();
            var str = size == 1 ? "totem_of_preserving.charm.item" : "totem_of_preserving.charm.items";
            tooltip.add(Component.literal(I18n.get(str, size)));
        }

        super.appendHoverText(totem, context, tooltip, flag);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack totem) {
        var data = Data.get(totem);
        if (data.items().isEmpty()) {
            return Optional.empty();
        }
        NonNullList<ItemStack> items = NonNullList.create();
        items.addAll(data.items());
        return Optional.of(new Tooltip(items));
    }

    public static void setGlint(ItemStack totem) {
        ColoredGlints.apply(totem, DyeColor.CYAN);
    }
}
