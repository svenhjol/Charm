package svenhjol.charm.module.glowballs;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.item.ICharmItem;
import svenhjol.charm.loader.CommonModule;

public class GlowballItem extends EnderpearlItem implements ICharmItem {
    protected CommonModule module;

    public GlowballItem(CommonModule module) {
        super(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MISC));
        this.module = module;
        this.register(module, "glowball");
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (enabled())
            super.fillItemCategory(group, stacks);
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

        if (!world.isClientSide) {
            svenhjol.charm.module.glowballs.GlowballEntity entity = new GlowballEntity(world, user);
            entity.setItem(itemStack);
            entity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(entity);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        if (!PlayerHelper.getAbilities(user).instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
