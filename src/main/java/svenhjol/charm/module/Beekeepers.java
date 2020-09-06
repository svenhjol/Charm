package svenhjol.charm.module;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.meson.event.StructureSetupCallback;
import svenhjol.meson.event.StructureSetupCallback.VillageType;
import svenhjol.charm.mixin.accessor.PointOfInterestTypeAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.VillagerHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.Random;

import static svenhjol.meson.event.StructureSetupCallback.addVillageHouse;

@Module(description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.")
public class Beekeepers extends MesonModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "beekeeper");
    public static VillagerProfession BEEKEEPER;

    @Override
    public void init() {
        BEEKEEPER = VillagerHelper.addProfession(ID, PointOfInterestType.BEEHIVE, SoundEvents.BLOCK_BEEHIVE_WORK);

        // HACK: set ticketCount so that villager can use it as job site
        ((PointOfInterestTypeAccessor)PointOfInterestType.BEEHIVE).setTicketCount(1);

        VillagerHelper.addTrade(BEEKEEPER, 1, new HoneyBottlesForEmeralds());
        VillagerHelper.addTrade(BEEKEEPER, 1, new PopulatedBeehiveForEmeralds());

        // register beekeeper structures
        StructureSetupCallback.EVENT.register(() -> {

            addVillageHouse(VillageType.PLAINS, new Identifier("charm:village/plains/houses/plains_beekeeper_1"), 10);
            addVillageHouse(VillageType.SAVANNA, new Identifier("charm:village/savanna/houses/savanna_beekeeper_1"), 10);
            addVillageHouse(VillageType.TAIGA, new Identifier("charm:village/taiga/houses/taiga_beekeeper_1"), 10);

        });
    }

    public static class HoneyBottlesForEmeralds implements TradeOffers.Factory {
        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            int count = random.nextInt(2) + 1;
            ItemStack in1 = new ItemStack(Items.EMERALD, count);
            ItemStack out = new ItemStack(Items.HONEY_BOTTLE, count);
            return new TradeOffer(in1, out, 10, 5, 0.2F);
        }
    }

    public static class PopulatedBeehiveForEmeralds implements TradeOffers.Factory {
        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            int count = random.nextInt(5) + 5;
            ItemStack in1 = new ItemStack(Items.EMERALD, count);
            ItemStack out = new ItemStack(Items.BEEHIVE);

            BeehiveBlockEntity blockEntity = new BeehiveBlockEntity();

            for (int i = 0; i < 3; i++) {
                BeeEntity bee = new BeeEntity(EntityType.BEE, entity.world);
                blockEntity.tryEnterHive(bee, false, 0);
            }

            CompoundTag beesTag = new CompoundTag();
            CompoundTag honeyTag = new CompoundTag();
            beesTag.put("Bees", blockEntity.getBees());
            honeyTag.putInt("honey_level", 0);
            out.putSubTag("BlockEntityTag", beesTag);
            out.putSubTag("BlockStateTag", honeyTag);
            out.setCustomName(new TranslatableText("item.charm.populated_beehive"));

            return new TradeOffer(in1, out, 1, 10, 0.2F);
        }
    }
}
