package svenhjol.charm.feature.raid_horns.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.raid_horns.RaidHornsClient;

public final class Handlers extends FeatureHolder<RaidHornsClient> {
    public Handlers(RaidHornsClient feature) {
        super(feature);
    }

    public float handleTooting(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return entity != null
            && entity.isUsingItem()
            && entity.getUseItem() == stack ? 1.0f : 0.0f;
    }
}
