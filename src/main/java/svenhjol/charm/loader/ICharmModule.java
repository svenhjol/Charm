package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.helper.StringHelper;

import java.util.Locale;

public interface ICharmModule {
    String getModId();

    boolean isEnabled();

    int getPriority();

    void setModId(String modId);

    void setEnabled(boolean flag);

    void setPriority(int priority);

    default String getName() {
        return this.getClass().getSimpleName();
    }

    default boolean depends() {
        return true;
    }

    default void register() {
        // always run
    }

    default void init() {
        // run if module is enabled
    }

    default ResourceLocation getId() {
        return new ResourceLocation(getModId(), StringHelper.upperCamelToSnake(getName()).toLowerCase(Locale.ROOT));
    }
}
