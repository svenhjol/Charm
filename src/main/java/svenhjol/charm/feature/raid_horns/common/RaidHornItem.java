package svenhjol.charm.feature.raid_horns.common;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.raid_horns.RaidHorns;
import svenhjol.charm.foundation.item.CharmItem;

public class RaidHornItem extends CharmItem<RaidHorns> {
    private final static int DURABILITY = 4;

    public RaidHornItem() {
        super(new Properties().durability(DURABILITY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return feature().handlers.useHorn(this, level, player, hand)
            .asInteractionResultHolder();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return Handlers.DURATION;
    }

    @Override
    public Class<RaidHorns> typeForFeature() {
        return RaidHorns.class;
    }
}
