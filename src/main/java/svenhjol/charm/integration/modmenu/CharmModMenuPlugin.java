package svenhjol.charm.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.base.CharmonyConfig;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.helper.TextHelper;
import svenhjol.charmony.iface.ILog;

import java.lang.reflect.Field;
import java.util.*;

public class CharmModMenuPlugin implements ModMenuApi {
    public String getModId() {
        return Charm.MOD_ID;
    }

    public ILog getLog() {
        return Charm.instance().log();
    }

    public CharmonyConfig getConfig() {
        return Charm.instance().config();
    }

    public List<CharmonyFeature> getFeatures() {
        return Charm.instance().loader().getFeatures();
    }

    @SuppressWarnings("unchecked")
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(TextHelper.translatable("cloth." + getModId() + ".title"));

            var features = new LinkedList<>(getFeatures());
            features.sort(Comparator.comparing(CharmonyFeature::getName));

            builder.setSavingRunnable(() -> {
                // Serialise the config into the config file. This is called after all variables are updated.
                getConfig().writeConfig(features);
            });

            ConfigCategory mainCategory = builder.getOrCreateCategory(TextHelper.translatable("cloth.category." + getModId() + ".title"));

            for (var feature : features) {
                var properties = getFeatureConfigProperties(feature);
                var subcategory = builder.entryBuilder()
                    .startSubCategory(TextHelper.literal(feature.getName()));

                if (feature.canBeDisabled()) {
                    var enabledValue = builder.entryBuilder()
                        .startBooleanToggle(TextHelper.translatable("cloth.category." + getModId() + ".feature_enabled"), feature.isEnabledInConfig())
                        .setDefaultValue(feature.isEnabledByDefault()) // Used when user click "Reset"
                        .setTooltip(TextHelper.translatable(TextHelper.splitOverLines(feature.getDescription()))) // Shown when the user hover over this option
                        .setSaveConsumer(feature::setEnabledInConfig)
                        .requireRestart();

                    if (enabledValue != null) {
                        enabledValue.requireRestart(true);
                        subcategory.add(enabledValue.build());
                    }
                }

                properties.forEach((prop, value) -> {
                    var propBuilder = builder.entryBuilder();
                    var annotation = prop.getDeclaredAnnotation(Configurable.class);
                    var name = TextHelper.literal(annotation.name());
                    var desc = TextHelper.literal(TextHelper.splitOverLines(annotation.description()));

                    FieldBuilder<?, ?, ?> propValue = null;

                    if (value instanceof Boolean) {
                        propValue = propBuilder
                            .startBooleanToggle(name, (Boolean)value).setDefaultValue(() -> (Boolean)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Integer) {
                        propValue = propBuilder
                            .startIntField(name, (Integer)value).setDefaultValue(() -> (Integer)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Double) {
                        propValue = propBuilder
                            .startDoubleField(name, (Double)value).setDefaultValue(() -> (Double)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Float) {
                        propValue = propBuilder
                            .startFloatField(name, (Float)value).setDefaultValue(() -> (Float)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof String) {
                        propValue = propBuilder
                            .startTextField(name, (String)value).setDefaultValue(() -> (String)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof List) {
                        propValue = propBuilder
                            .startStrList(name, (List<String>)value).setDefaultValue(() -> (List<String>)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    }

                    if (propValue != null) {
                        propValue.requireRestart(annotation.requireRestart());
                        subcategory.add(propValue.build());
                    }
                });

                if (!subcategory.isEmpty())
                    mainCategory.addEntry(subcategory.build());
            }

            return builder.build();
        };
    }

    private Map<Field, Object> getFeatureConfigProperties(CharmonyFeature feature) {
        Map<Field, Object> properties = new LinkedHashMap<>();

        // Get and set feature config options
        ArrayList<Field> classFields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
        classFields.forEach(prop -> {
            try {
                Configurable annotation = prop.getDeclaredAnnotation(Configurable.class);
                if (annotation == null) return;

                Object value = prop.get(null);
                properties.put(prop, value);

            } catch (Exception e) {
                getLog().error(getClass(), "Failed to read config property " + prop.getName() + " in " + feature.getName());
            }
        });

        return properties;
    }

    private void trySetProp(Field prop, Object val) {
        try {
            prop.set(null, val);
        } catch (IllegalAccessException e) {
            getLog().error(getClass(), e.getMessage());
        }
    }

    private Object tryGetDefault(Field prop) {
        return CharmonyConfig.
            getDefaultFieldValues()
            .getOrDefault(prop, null);
    }
}
