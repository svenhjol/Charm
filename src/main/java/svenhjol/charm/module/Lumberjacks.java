package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.VillagerHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.Random;

@Module(description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public class Lumberjacks extends MesonModule {
    public static Identifier VILLAGER_ID = new Identifier(Charm.MOD_ID, "lumberjack");
    public static VillagerProfession LUMBERJACK;
    public static PointOfInterestType POIT;

    @Override
    public void init() {
        // TODO dedicated sounds for woodcutter and jobsite
        POIT = VillagerHelper.addPointOfInterestType(Woodcutters.BLOCK_ID, Woodcutters.WOODCUTTER, 1);
        LUMBERJACK = VillagerHelper.addProfession(VILLAGER_ID, POIT, SoundEvents.ENTITY_VILLAGER_WORK_MASON);
        VillagerHelper.addTrade(LUMBERJACK, 1, new EmeraldsForWoodenPlanks());
    }

    public static class EmeraldsForWoodenPlanks implements TradeOffers.Factory {
        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            int count = random.nextInt(2) + 1;
            ItemStack in1 = new ItemStack(Items.OAK_PLANKS, count * 6);
            ItemStack out = new ItemStack(Items.EMERALD, count);
            return new TradeOffer(in1, out, 10, 1, 0.2F);
        }
    }
}
