package svenhjol.charm.feature.endermite_powder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;
import svenhjol.charm.Charm;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.EntityKilledDropEvent;
import svenhjol.charmony_api.iface.IWandererTrade;
import svenhjol.charmony_api.iface.IWandererTradeProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Endermites drop endermite powder that can be used to locate an End City.")
public class EndermitePowder extends CharmonyFeature implements IWandererTradeProvider {
    static final String ID = "endermite_powder";
    static Supplier<EntityType<EndermitePowderEntity>> entity;
    static Supplier<EndermitePowderItem> item;
    static Supplier<SoundEvent> launchSound;
    public static final TagKey<Structure> ENDERMITE_POWDER_LOCATED = TagKey.create(Registries.STRUCTURE,
        Charm.instance().makeId("endermite_powder_located"));

    static final ResourceLocation TRIGGER_USED_ENDERMITE_POWER = Charm.instance().makeId("used_endermite_powder");

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        item = registry.item(ID, () -> new EndermitePowderItem(this));
        launchSound = registry.soundEvent("endermite_powder_launch");

        entity = registry.entity(ID, () -> EntityType.Builder
            .<EndermitePowderEntity>of(EndermitePowderEntity::new, MobCategory.MISC)
            .clientTrackingRange(80)
            .updateInterval(10)
            .sized(2.0F, 2.0F));

        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        EntityKilledDropEvent.INSTANCE.handle(this::handleEntityKilledDrop);
    }

    private InteractionResult handleEntityKilledDrop(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.level().isClientSide() && entity instanceof Endermite) {
            var random = entity.getRandom();
            var level = entity.getCommandSenderWorld();
            var pos = entity.blockPosition();
            var amount = random.nextInt(2) + random.nextInt(lootingLevel + 1);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
                new ItemStack(item.get(), amount)));
        }
        return InteractionResult.PASS;
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return item.get();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public int getCost() {
                return 20;
            }
        });
    }

    public static void triggerAdvancement(ServerPlayer playerEntity) {
        Advancements.trigger(TRIGGER_USED_ENDERMITE_POWER, playerEntity);
    }
}
