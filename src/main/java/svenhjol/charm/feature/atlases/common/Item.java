package svenhjol.charm.feature.atlases.common;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.atlases.Atlases;

public class Item extends net.minecraft.world.item.Item {
    public Item() {
        super(new Properties()
            .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return Atlases.handlers
            .useAtlasInHand(level, player, hand)
            .asInteractionResultHolder();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return Atlases.handlers
            .useAtlasOn(context)
            .asInteractionResult(context.getLevel().isClientSide);
    }
}
