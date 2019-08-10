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
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.FlavoredCake;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.IMesonBlock;

import java.util.List;

public class FlavoredCakeBlock extends CakeBlock implements IMesonBlock
{
    public String baseName;
    public ItemStack flavor;
    public int duration;

    public FlavoredCakeBlock(String potionName)
    {
        // set up block properties
        super(Block.Properties
            .create(Material.CAKE)
            .hardnessAndResistance(0.5F)
            .sound(SoundType.CLOTH)
        );

        // get the mod and potion name from the fully qualified potion name
        String baseName, modName, longName, shortName;
        if (potionName.contains(":")) {
            String[] split = potionName.split(":");
            modName = split[0];
            baseName = split[1];
        } else {
            modName = "minecraft";
            baseName = potionName;
        }

        // set the potion type, return if fails
        Potion type = null;
        longName = modName + ":long_" + baseName;
        shortName = modName + ":short_" + baseName;

        String[] names = new String[] { longName, shortName, potionName };
        for (String name : names) {
            type = Potion.getPotionTypeForName(name);
            if (type != null && !type.getEffects().isEmpty()) break;
        }
        if (type == null || type.getEffects().isEmpty()) {
            Meson.log("Cannot find Potion for " + potionName + ", skipping");
            return;
        }
        setPotion(type);

        // set effect duration
        ItemStack potionItem = PotionHelper.getPotionItemStack(type, 1);
        List<EffectInstance> effects = PotionUtils.getEffectsFromStack(potionItem);

        int duration = 0;
        for (EffectInstance effect : effects) {
            duration = Math.max(duration, effect.getDuration());
        }
        if (duration == 0) duration = 10;
        setDuration(duration);

        // setup cake name and add to pool
        this.baseName = "cake_" + baseName;
        setRegistryName(new ResourceLocation(Charm.MOD_ID, this.baseName));

        FlavoredCake.cakes.put(potionName, this);
        FlavoredCake.types.put(getPotion(), this);
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.FOOD;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult block)
    {
        boolean eaten = false;

        if (!world.isRemote) {
            eaten = super.onBlockActivated(state, world, pos, player, hand, block);
            if (eaten) {
                PotionUtils.getEffectsFromStack(flavor).forEach(effectInstance -> {
                    EffectInstance effect = new EffectInstance(effectInstance.getPotion(), getDuration());
                    player.addPotionEffect(effect);
                });
            }
        }

        return eaten;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public Potion getPotion()
    {
        return PotionUtils.getPotionFromItem(this.flavor);
    }

    public void setDuration(int duration)
    {
        this.duration = (int)(duration * FlavoredCake.multiplier.get());
    }

    public void setPotion(Potion potion)
    {
        this.flavor = PotionHelper.getPotionItemStack(potion, 1);;
    }
}
