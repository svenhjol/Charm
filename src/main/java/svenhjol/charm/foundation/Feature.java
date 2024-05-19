package svenhjol.charm.foundation;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.foundation.feature.ChildFeature;
import svenhjol.charm.foundation.feature.Conditional;
import svenhjol.charm.foundation.feature.Metadata;
import svenhjol.charm.foundation.helper.TextHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

public abstract class Feature implements Conditional {
    private final Loader<? extends Feature> loader;
    private final Log log;
    private final Metadata metadata;
    private boolean enabled = true;
    private boolean enabledInConfig = true;

    public Feature(Loader<? extends Feature> loader) {
        this.loader = loader;
        this.log = new Log(loader.id(), name());
        this.log.dev(name() + " is alive");
        this.metadata = Metadata.create(this);
    }

    public Optional<Metadata> metadata() {
        return Optional.ofNullable(this.metadata);
    }

    public final String description() {
        return metadata()
            .map(m -> m.description)
            .orElse("");
    }

    public final int priority() {
        return metadata()
            .map(m -> m.priority)
            .orElse(0);
    }

    public final boolean isEnabledByDefault() {
        return metadata()
            .map(m -> m.enabledByDefault)
            .orElse(true);
    }

    public boolean canBeDisabled() {
        return true;
    }

    public Loader<? extends Feature> loader() {
        return loader;
    }

    public abstract Registry registry();

    public Log log() {
        return log;
    }

    public ResourceLocation id(String id) {
        return loader.id(id);
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    public String snakeCaseName() {
        return TextHelper.upperCamelToSnake(name());
    }

    @Override
    public boolean isEnabled() {
        return enabled && enabledInConfig;
    }

    public boolean isEnabledInConfig() {
        return enabledInConfig;
    }

    public List<BooleanSupplier> checks() {
        return List.of();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabledInConfig(boolean enabledInConfig) {
        this.enabledInConfig = enabledInConfig;
    }

    public List<? extends ChildFeature<? extends Feature>> children() {
        return List.of();
    }
}
