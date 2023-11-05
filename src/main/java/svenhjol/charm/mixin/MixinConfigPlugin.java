package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmonyMixinConfigPlugin;
import svenhjol.charmony.helper.ConfigHelper;

import java.util.List;
import java.util.function.Predicate;

public class MixinConfigPlugin extends CharmonyMixinConfigPlugin {
    @Override
    protected String modId() {
        return Charm.ID;
    }

    @Override
    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of(
            // Disable NoChatUnverifiedMessage if chatsigninghider mod is present.
            feature -> feature.equals("NoChatUnverifiedMessage") && ConfigHelper.isModLoaded("chatsigninghider")
        );
    }
}
