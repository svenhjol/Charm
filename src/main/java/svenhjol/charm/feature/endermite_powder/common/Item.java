package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.foundation.item.CharmItem;

public class Item extends CharmItem<EndermitePowder> {
    public Item() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return feature().handlers.useItem(this, level, player, hand)
            .asInteractionResultHolder();
    }

    @Override
    public Class<EndermitePowder> typeForFeature() {
        return EndermitePowder.class;
    }
}
