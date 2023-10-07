package svenhjol.charm.feature.extra_mob_drops;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony_api.event.EntityKilledDropEvent;
import svenhjol.charmony.base.CharmonyFeature;

@Feature(mod = Charm.MOD_ID, description = "Some mobs have a chance to drop additional items.")
public class ExtraMobDrops extends CharmonyFeature {
    private static final double LOOTING_BOOST = 0.1D;

    @Configurable(name = "Maximum sand drops", description = "Maximum sand blocks dropped when a husk is killed.")
    public static int maxSandDrops = 2;

    @Configurable(name = "Maximum mutton drops", description = "Maximum pieces of mutton dropped when a goat is killed.")
    public static int maxMuttonDrops = 2;

    @Configurable(name = "Maximum cobweb drops", description = "Maximum cobwebs dropped when a cave spider is killed.")
    public static int maxCobwebDrops = 2;

    @Configurable(name = "Luck drop chance", description = "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed.")
    public static double luckDropChance = 0.05D;

    @Configurable(name = "Feather drop chance", description = "Chance (out of 1.0) of a chicken dropping a feather at the same time as an egg.")
    public static double featherDropChance = 0.33D;

    @Override
    public void runWhenEnabled() {
        EntityKilledDropEvent.INSTANCE.handle(this::handleEntityDrop);
    }

    private InteractionResult handleEntityDrop(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.level().isClientSide()) {
            ItemStack stack = null;

            if (entity instanceof Witch witch) {
                stack = new WitchDrops().getItemStack(witch, lootingLevel);
            } else if (entity instanceof Husk husk) {
                stack = new HuskDrops().getItemStack(husk, lootingLevel);
            } else if (entity instanceof Goat goat) {
                stack = new GoatDrops().getItemStack(goat, lootingLevel);
            } else if (entity instanceof CaveSpider caveSpider) {
                stack = new CaveSpiderDrops().getItemStack(caveSpider, lootingLevel);
            }

            if (stack != null && !stack.isEmpty() && stack.getCount() > 0) {
                var level = entity.level();
                var pos = entity.blockPosition();
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack));
            }
        }

        return InteractionResult.PASS;
    }

    public static void tryDropFeather(Chicken chicken) {
        if (chicken.isAlive()
            && !chicken.isBaby()
            && !chicken.level().isClientSide
            && !chicken.isChickenJockey()
            && chicken.level().random.nextFloat() < featherDropChance
        ) {
            chicken.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (chicken.level().random.nextFloat() - chicken.level().random.nextFloat()) * 0.2F + 1.0F);
            chicken.spawnAtLocation(Items.FEATHER);
            chicken.eggTime = chicken.level().random.nextInt(3000) + 3000;
        }
    }

    private static abstract class TryGetDrops<T extends LivingEntity> {
        abstract ItemStack getItemStack(T entity, int lootingLevel);

        int getAmountWithLooting(RandomSource random, int max, int lootingLevel) {
            var amount = random.nextInt(Math.max(1, max + 1));

            if (random.nextDouble() < (lootingLevel * LOOTING_BOOST)) {
                amount++;
            }

            return amount;
        }
    }

    static class WitchDrops extends TryGetDrops<Witch> {
        @Override
        ItemStack getItemStack(Witch entity, int lootingLevel) {
            if (entity.getRandom().nextDouble() < (luckDropChance + (lootingLevel * LOOTING_BOOST))) {
                var potion = new ItemStack(Items.POTION);
                PotionUtils.setPotion(potion, Potions.LUCK);
                return potion;
            }
            return ItemStack.EMPTY;
        }
    }

    static class HuskDrops extends TryGetDrops<Husk> {
        @Override
        ItemStack getItemStack(Husk entity, int lootingLevel) {
            var amount = getAmountWithLooting(entity.getRandom(), maxSandDrops, lootingLevel);
            return amount == 0 ? ItemStack.EMPTY : new ItemStack(Items.SAND, amount);
        }
    }

    static class GoatDrops extends TryGetDrops<Goat> {
        @Override
        ItemStack getItemStack(Goat entity, int lootingLevel) {
            var amount = getAmountWithLooting(entity.getRandom(), maxMuttonDrops, lootingLevel);
            var item = entity.isOnFire() ? Items.COOKED_MUTTON : Items.MUTTON;
            return amount == 0 ? ItemStack.EMPTY : new ItemStack(item, amount);
        }
    }

    static class CaveSpiderDrops extends TryGetDrops<CaveSpider> {
        @Override
        ItemStack getItemStack(CaveSpider entity, int lootingLevel) {
            var amount = getAmountWithLooting(entity.getRandom(), maxCobwebDrops, lootingLevel);
            return amount == 0 ? ItemStack.EMPTY : new ItemStack(Items.COBWEB, amount);
        }
    }
}
