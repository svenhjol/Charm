package svenhjol.charm.feature.variant_mob_textures;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.jetbrains.annotations.NotNull;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.client.ClientFeature;

import javax.annotation.Nullable;
import java.util.*;

public class VariantMobTextures extends ClientFeature {
    static final Map<UUID, ResourceLocation> CACHED_TEXTURE = new WeakHashMap<>();
    static final String TEXTURE_TAG = "charm_texture";

    @Configurable(name = "Cows", description = "If true, cows may spawn with different textures.")
    public static boolean cows = true;

    @Configurable(name = "Chickens", description = "If true, chickens may spawn with different textures.")
    public static boolean chickens = true;

    @Configurable(name = "Dolphins", description = "If true, dolphins may spawn with different textures.")
    public static boolean dolphins = true;

    @Configurable(name = "Pigs", description = "If true, pigs may spawn with different textures.")
    public static boolean pigs = true;

    @Configurable(name = "Sheep", description = "If true, sheep face and 'shorn' textures match their wool color.")
    public static boolean sheep = true;

    @Configurable(name = "Snow golems", description = "If true, snow golems may spawn with different derp faces.")
    public static boolean snowGolems = true;

    @Configurable(name = "Squids", description = "If true, squids may spawn with different textures.")
    public static boolean squids = true;

    @Configurable(name = "Turtles", description = "If true, turtles may spawn with different textures.")
    public static boolean turtles = true;

    @Configurable(name = "Wandering traders", description = "If true, wandering traders may spawn with different textures.")
    public static boolean wanderingTraders = true;

    @Configurable(name = "Chance of rare variants", description = "Approximately 1 in X chance of a mob spawning as a rare variant.\n" +
        "Set to zero to disable rare variants.")
    public static int rareVariantChance = 1000;

    @Override
    public boolean canBeDisabled() {
        return true;
    }

    @Override
    public String description() {
        return "Mobs may spawn with different textures.";
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }

    @Nullable
    public static ResourceLocation getChickenTexture(Chicken entity) {
        return getTexture(entity, ClientRegistration.CHICKENS, ClientRegistration.RARE_CHICKENS);
    }

    @Nullable
    public static ResourceLocation getCowTexture(Cow entity) {
        return getTexture(entity, ClientRegistration.COWS, ClientRegistration.RARE_COWS);
    }

    @Nullable
    public static ResourceLocation getDolphinTexture(Dolphin entity) {
        return getTexture(entity, ClientRegistration.DOLPHINS, ClientRegistration.RARE_DOLPHINS);
    }

    @Nullable
    public static ResourceLocation getPigTexture(Pig entity) {
        return getTexture(entity, ClientRegistration.PIGS, ClientRegistration.RARE_PIGS);
    }

    public static ResourceLocation getSheepTexture(Sheep entity) {
        var fleeceColor = entity.getColor();
        return ClientRegistration.SHEEP.getOrDefault(fleeceColor, ClientRegistration.DEFAULT_SHEEP);
    }

    @Nullable
    public static ResourceLocation getSnowGolemTexture(SnowGolem entity) {
        return getTexture(entity, ClientRegistration.SNOW_GOLEMS, ImmutableList.of());
    }

    @Nullable
    public static ResourceLocation getWanderingTraderTexture(WanderingTrader entity) {
        return getTexture(entity, ClientRegistration.WANDERING_TRADERS, ImmutableList.of());
    }

    @Nullable
    public static ResourceLocation getSquidTexture(Squid entity) {
        return getTexture(entity, ClientRegistration.SQUIDS, ClientRegistration.RARE_SQUIDS);
    }

    @Nullable
    public static ResourceLocation getTurtleTexture(Turtle entity) {
        return getTexture(entity, ClientRegistration.TURTLES, ClientRegistration.RARE_TURTLES);
    }

    @Nullable
    public static ResourceLocation getTexture(Entity entity, List<ResourceLocation> normalSet, List<ResourceLocation> rareSet) {
        var id = entity.getUUID();

        if (CACHED_TEXTURE.containsKey(id)) {
            var val = CACHED_TEXTURE.get(id);
            if (val != null) {
                return val;
            }
        } else {
            var defined = "";
            var tags = entity.getTags();

            for (var tag : tags) {
                if (tag.startsWith(TEXTURE_TAG)) {
                    defined = tag.split("=")[1];
                }
            }

            if (defined.isEmpty()) {
                CACHED_TEXTURE.put(id, null);
            } else {
                ResourceLocation res;
                if (defined.contains(":")) {
                    var s = defined.split(":");
                    res = new ResourceLocation(s[0], ClientRegistration.TEXTURES + "/" + s[1]);
                } else {
                    res = Charm.id(ClientRegistration.TEXTURES + "/" + defined);
                }
                CACHED_TEXTURE.put(id, res);
                return res;
            }
        }

        var isRare = rareVariantChance > 0
            && !rareSet.isEmpty()
            && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % rareVariantChance == 0;

        var set = isRare ? rareSet : normalSet;
        if (set.isEmpty()) {
            return null;
        }

        var choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    public enum MobType implements StringRepresentable {
        CHICKEN,
        COW,
        DOLPHIN,
        PIG,
        SHEEP,
        SNOW_GOLEM,
        SQUID,
        TURTLE,
        WANDERING_TRADER;

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
