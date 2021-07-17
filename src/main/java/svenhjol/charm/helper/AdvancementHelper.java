package svenhjol.charm.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.loader.ModuleLoader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0.0-charm
 */
public class AdvancementHelper {
    private static final List<ResourceLocation> ADVANCEMENTS_TO_REMOVE = new ArrayList<>();

    @Nullable
    public static Component tryTranslateTitle(Component title) {
        List<String> modIds = ModuleLoader.getModIds();

        // the title is a translate component of `chat.square_brackets`; don't try and work with a non-translatable component
        if (!(title instanceof TranslatableComponent trans))
            return null;

        // don't do translation on client side
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            return null;

        // the text we are interested in is the only argument of the square_brackets component
        TranslatableComponent first;
        Object[] args = trans.getArgs();
        if (args.length == 0 || !(args[0] instanceof TranslatableComponent)) {
            first = trans;
        } else {
            first = (TranslatableComponent)args[0];
        }

        for (String modId : modIds) {
            // we are only interested in translating charm and charm-based mods
            if (first.getKey().contains(modId)) {
                String resolved = StringHelper.tryResolveLanguageKey(modId, first.getKey());
                if (resolved != null) {
                    LogHelper.debug(AdvancementHelper.class, "Resolved key `" + first.getKey() + "` to `" + resolved + "`");
                    return ComponentUtils.wrapInSquareBrackets(new TranslatableComponent(resolved));
                }
            }
        }

        return null;
    }

    public static void removeAdvancement(ResourceLocation id) {
        LogHelper.debug(AdvancementHelper.class, "Adding `" + id + "` to list of advancements to remove");
        ADVANCEMENTS_TO_REMOVE.add(id);
    }

    public static void filterAdvancements(Map<ResourceLocation, Advancement.Builder> map) {
        if (ADVANCEMENTS_TO_REMOVE.isEmpty() || map.isEmpty()) return;

        ADVANCEMENTS_TO_REMOVE.forEach(toRemove -> {
            List<ResourceLocation> keys = new ArrayList<>(map.keySet());

            // remove exact matches
            AtomicInteger exactMatches = new AtomicInteger();
            keys.stream().filter(a -> a.equals(toRemove)).forEach(a -> {
                LogHelper.debug(AdvancementHelper.class, "> Filtering out exact match `" + a + "`");
                exactMatches.getAndIncrement();
                map.remove(a);
            });
            if (exactMatches.intValue() > 0)
                return;

            // remove all advancements for disabled modules
            keys.stream()
                .filter(a -> a.getNamespace().equals(toRemove.getNamespace()))
                .filter(a -> a.getPath().startsWith(toRemove.getPath()))
                .forEach(a -> {
                    LogHelper.debug(AdvancementHelper.class, "> Filtering out fuzzy match `" + a + "`");
                    map.remove(a);
                });
        });
    }

    public static <T extends CharmModule> void removeAdvancements(List<T> modules) {
        modules.forEach(module -> removeAdvancement(module.getId()));
    }
}
