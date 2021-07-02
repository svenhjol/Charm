package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.helper.StringHelper;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public interface ICharmModule {
    boolean isEnabled();

    boolean isEnabledInConfig();

    boolean isEnabledByDefault();

    boolean isAlwaysEnabled();

    String getModId();

    int getPriority();

    List<Predicate<ICharmModule>> getDependencies();

    void setModId(String modId);

    void setEnabled(boolean flag);

    void setEnabledInConfig(boolean flag);

    void setPriority(int priority);

    void addDependencyCheck(Predicate<ICharmModule> test);

    default String getName() {
        return this.getClass().getSimpleName();
    }

    default void register() {
        // always run
    }

    default void run() {
        // run if module is enabled
    }

    default ResourceLocation getId() {
        return new ResourceLocation(getModId(), StringHelper.upperCamelToSnake(getName()).toLowerCase(Locale.ROOT));
    }
}
