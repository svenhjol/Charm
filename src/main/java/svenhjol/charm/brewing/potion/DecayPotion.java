package svenhjol.charm.brewing.potion;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmPotion;
import svenhjol.charm.brewing.feature.Decay;
import svenhjol.meson.registry.ProxyRegistry;

import java.util.ArrayList;
import java.util.List;

public class DecayPotion extends CharmPotion
{
    public static PotionType type;
    public static List<PotionEffect> effects = new ArrayList<>();

    public DecayPotion()
    {
        // set up the decay potion, effects, and type
        super("decay", true, 0x260120, 0);
        effects.add(new PotionEffect(MobEffects.WITHER, Decay.duration * 20, Decay.strength));

        PotionEffect[] e = effects.toArray(new PotionEffect[0]);
        type = new PotionType(name, e).setRegistryName(new ResourceLocation(getModId(), name));
        ProxyRegistry.register(type);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
