package svenhjol.charm.module;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.PointOfInterestTypeAccessor;
import svenhjol.charm.mixin.accessor.StructurePoolAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.VillagerHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Module(description = "")
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


        StructurePool houses = BuiltinRegistries.STRUCTURE_POOL.get(new Identifier("village/plains/houses"));
        // HACK: set the structure pool elementCounts to mutable - move to helper method asap!

        List<Pair<StructurePoolElement, Integer>> elementCounts = ((StructurePoolAccessor) houses).getElementCounts();
        elementCounts = new ArrayList<>(elementCounts);

        // strip off the empty thing
        Pair<StructurePoolElement, Integer> emptyElement = elementCounts.get(elementCounts.size() - 1);
        elementCounts.remove( elementCounts.get(elementCounts.size() - 1) );

        if (false) { // DELETES ALL HOUSES, DO NOT USE!
            ((StructurePoolAccessor) houses).setElementCounts(new ArrayList<>());
        }

        Pair<Function<StructurePool.Projection, LegacySinglePoolElement>, Integer> pair =
            Pair.of(StructurePoolElement.method_30426("charm:village/plains/houses/plains_beekeeper_1", StructureProcessorLists.MOSSIFY_10_PERCENT), 10);

        StructurePool.Projection projection = StructurePool.Projection.RIGID;
        Integer count = pair.getSecond();
        StructurePoolElement structurePoolElement = (StructurePoolElement)((Function)pair.getFirst()).apply(projection);

        // add our custom one
        ((StructurePoolAccessor)houses).getElementCounts().add(Pair.of(structurePoolElement, count));

        // add back the empty thing
        ((StructurePoolAccessor)houses).getElementCounts().add(emptyElement);

        for (int i = 0; i < count; i++) {
            ((StructurePoolAccessor)houses).getElements().add(structurePoolElement);
        }
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
