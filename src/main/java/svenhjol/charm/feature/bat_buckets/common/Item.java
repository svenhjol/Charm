package svenhjol.charm.feature.bat_buckets.common;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.foundation.item.CharmItem;

public class Item extends CharmItem<BatBuckets> {
    public Item() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return feature().handlers
            .useItemInHand(level, player, hand)
            .asInteractionResultHolder();
    }

    @Override
    public Class<BatBuckets> typeForFeature() {
        return BatBuckets.class;
    }
}
