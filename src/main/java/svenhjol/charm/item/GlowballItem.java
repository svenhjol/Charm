package svenhjol.charm.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.item.ICharmItem;
import svenhjol.charm.entity.GlowballEntity;

public class GlowballItem extends EnderPearlItem implements ICharmItem {
    protected CharmModule module;

    public GlowballItem(CharmModule module) {
        super(new Item.Settings().maxCount(16).group(ItemGroup.MISC));
        this.module = module;
        this.register(module, "glowball");
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (enabled())
            super.appendStacks(group, stacks);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

        if (!world.isClient) {
            GlowballEntity entity = new GlowballEntity(world, user);
            entity.setItem(itemStack);
            entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(entity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!PlayerHelper.getAbilities(user).creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
