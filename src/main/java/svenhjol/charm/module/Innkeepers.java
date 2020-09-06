package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
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
import svenhjol.charm.block.BrewBottleBlock;
import svenhjol.meson.event.StructureSetupCallback;
import svenhjol.meson.event.StructureSetupCallback.VillageType;
import svenhjol.charm.item.SuspiciousBrewItem;
import svenhjol.meson.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.VillagerHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.Random;

import static svenhjol.meson.event.StructureSetupCallback.addVillageHouse;

@Module(description = "Innkeepers are villagers that trade potion items. Their job site is TBC.")
public class Innkeepers extends MesonModule {
    public static Identifier BLOCK_ID = new Identifier(Charm.MOD_ID, "brew_bottle");
    public static Identifier ITEM_ID = new Identifier(Charm.MOD_ID, "suspicious_brew");
    public static Identifier VILLAGER_ID = new Identifier(Charm.MOD_ID, "innkeeper");

    public static SuspiciousBrewItem SUSPICIOUS_BREW;
    public static BrewBottleBlock BREW_BOTTLE;
    public static PointOfInterestType POIT;

    public static VillagerProfession INNKEEPER;

    @Override
    public void init() {
        SUSPICIOUS_BREW = new SuspiciousBrewItem(this);
        BREW_BOTTLE = new BrewBottleBlock(this);

        // register innkeeper structures
        StructureSetupCallback.EVENT.register(() -> {

            // @Coranthes: register houses here, one per line
            addVillageHouse(VillageType.TAIGA, new Identifier("charm:village/taiga/houses/taiga_innkeeper_1"), 10);
//            addVillageHouse(VillageType.PLAINS, new Identifier("charm:village/plains/houses/plains_innkeeper_1"), 10);
//            addVillageHouse(VillageType.SAVANNA, new Identifier("charm:village/savanna/houses/savanna_innkeeper_1"), 10);

        });

        // TODO dedicated sounds for innkeeper and jobsite
        POIT = VillagerHelper.addPointOfInterestType(BLOCK_ID, BREW_BOTTLE, 1);
        INNKEEPER = VillagerHelper.addProfession(VILLAGER_ID, POIT, SoundEvents.ENTITY_VILLAGER_WORK_CLERIC);
        VillagerHelper.addTrade(INNKEEPER, 1, new SuspiciousBrewForEmeralds());
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(BREW_BOTTLE, RenderLayer.getCutout());
    }

    public static class SuspiciousBrewForEmeralds implements TradeOffers.Factory {
        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            int count = random.nextInt(2) + 1;

            ItemStack in1 = new ItemStack(Items.EMERALD, count);
            ItemStack out = SuspiciousBrewItem.makeBrew(Items.EGG, Items.EGG, 0.5F);
            return new TradeOffer(in1, out, 10, 5, 0.2F);
        }
    }
}
