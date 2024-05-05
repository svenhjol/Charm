package svenhjol.charm.api.enums;

import net.minecraft.world.item.ItemStack;

public record EventResultWithItemStack(EventResult result, ItemStack stack) {
}
