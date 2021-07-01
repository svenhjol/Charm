package svenhjol.charm.module.cooking_pots;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.item.CharmItem;

import java.util.List;
import java.util.stream.Collectors;

public class MixedStewItem extends CharmItem {
    public static final String CONTENTS_NBT = "Contents";
    public static final String HUNGER_NBT = "Hunger";
    public static final String SATURATION_NBT = "Saturation";

    public MixedStewItem(CommonModule module) {
        super(module, "mixed_stew", (new Item.Properties())
            .stacksTo(64)
            .tab(CreativeModeTab.TAB_FOOD));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack held = user.getItemInHand(hand);
        if (user.canEat(false)) {
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(held);
        }
        return InteractionResultHolder.pass(held);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32; // vanilla non-snack ticks
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        world.gameEvent(user, GameEvent.EAT, user.eyeBlockPosition());
        world.playSound(null, user.getX(), user.getY(), user.getZ(), user.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);

        if (user instanceof Player) {
            Player player = (Player) user;
            player.getFoodData().eat((int)getHunger(stack), getSaturation(stack));
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

            if (!world.isClientSide) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
            }

            stack.shrink(1);
            player.addItem(new ItemStack(Items.BOWL));

            user.gameEvent(GameEvent.EAT);
        }

        return stack;
    }

    public static List<ResourceLocation> getContents(ItemStack stack) {
        ListTag list = stack.getOrCreateTag().getList(CONTENTS_NBT, 8);
        return list.stream().map(Tag::getAsString).map(ResourceLocation::new).collect(Collectors.toList());
    }

    public static float getHunger(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(HUNGER_NBT);
    }

    public static float getSaturation(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(SATURATION_NBT);
    }

    public static void setContents(ItemStack stack, List<ResourceLocation> contents) {
        ListTag list = new ListTag();
        list.addAll(contents.stream().map(ResourceLocation::toString).map(StringTag::valueOf).collect(Collectors.toList()));
        stack.getOrCreateTag().put(CONTENTS_NBT, list);
    }

    public static void setHunger(ItemStack stack, float hunger) {
        stack.getOrCreateTag().putFloat(HUNGER_NBT, hunger);
    }

    public static void setSaturation(ItemStack stack, float saturation) {
        stack.getOrCreateTag().putFloat(SATURATION_NBT, saturation);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(new TranslatableComponent("item.charm.mixed_stew.hunger", getHunger(stack)).withStyle(ChatFormatting.YELLOW));
        tooltip.add(new TranslatableComponent("item.charm.mixed_stew.saturation", getSaturation(stack)).withStyle(ChatFormatting.GOLD));
    }
}
