package svenhjol.charm.brewing.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import svenhjol.charm.brewing.module.FlavoredCake;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.IMesonBlock;

import javax.annotation.Nullable;
import java.util.List;

public class FlavoredCakeBlock extends CakeBlock implements IMesonBlock {
    public String baseName;
    public String modName;
    public String potionName;
    private MesonModule module;

    public FlavoredCakeBlock(MesonModule module, String modName, String potionName) {
        // init block
        super(Block.Properties
            .create(Material.CAKE)
            .hardnessAndResistance(0.5F)
            .sound(SoundType.CLOTH)
        );
        this.module = module;
        this.modName = modName;
        this.potionName = potionName;

        // register cake block, add to pool
        this.baseName = "cake_" + potionName;
        register(module, this.baseName);
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.FOOD;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isEnabled() && group == ItemGroup.SEARCH) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public boolean isEnabled() {
        return module.enabled;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult block) {
        if (!world.isRemote) {
            if (super.onBlockActivated(state, world, pos, player, hand, block).equals(ActionResultType.SUCCESS)) {
                this.onEaten(player);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    public void onEaten(PlayerEntity player) {
        // get the potion
        Potion potion = getPotion();
        ItemStack potionItem = PotionHelper.getPotionItemStack(potion, 1);

        // get effect duration
        List<EffectInstance> effects = PotionUtils.getEffectsFromStack(potionItem);

        int duration = 0;
        for (EffectInstance effect : effects) {
            duration = Math.max(duration, effect.getDuration());
        }
        if (duration == 0) duration = 10;

        for (EffectInstance effectInstance : PotionUtils.getEffectsFromStack(potionItem)) {
            EffectInstance effect = new EffectInstance(effectInstance.getPotion(), (int) (duration * FlavoredCake.multiplier));
            player.addPotionEffect(effect);
        }
    }

    @Nullable
    public Potion getPotion() {
        Potion potion = null;
        String longName = modName + ":long_" + potionName;
        String shortName = modName + ":short_" + potionName;

        String[] names = new String[]{longName, shortName, potionName};
        for (String name : names) {
            potion = Potion.getPotionTypeForName(name);
            if (!potion.getEffects().isEmpty()) break;
        }
        if (potion.getEffects().isEmpty()) {
            Meson.LOG.debug("No potion registered for " + potionName);
            return null; // don't apply an effect
        }
        return potion;
    }
}
