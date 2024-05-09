package svenhjol.charm.feature.atlases.common;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.item.CharmItem;

public class Item extends CharmItem<Atlases> {
    public Item() {
        super(new Properties()
            .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return feature().handlers
            .useAtlasInHand(level, player, hand)
            .asInteractionResultHolder();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return feature().handlers
            .useAtlasOn(context)
            .asInteractionResult(context.getLevel().isClientSide);
    }

    @Override
    public Class<Atlases> type() {
        return Atlases.class;
    }
}
