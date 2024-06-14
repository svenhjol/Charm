package svenhjol.charm.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.network.chat.Component;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.Config;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.TextHelper;

import java.lang.reflect.Field;
import java.util.*;

public class CharmModMenuPlugin<F extends Feature> implements ModMenuApi {
    private static final Log LOGGER = new Log(Charm.ID, "ModMenuPlugin");
    
    public String id() {
        return Charm.ID;
    }

    @SuppressWarnings("unchecked")
    public List<F> getFeatures() {
        List<Feature> features = new ArrayList<>();
        
        features.addAll(Resolve.features(Side.COMMON, id()));
        features.addAll(Resolve.features(Side.CLIENT, id()));

        features.sort(Comparator.comparing(Feature::name));
        return (List<F>) features;
    }

    @SuppressWarnings("unchecked")
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("cloth." + id() + ".title"));

            var features = new LinkedList<>(getFeatures());
            features.sort(Comparator.comparing(F::name));

            // Serialise the config into the config file. This is called after all variables are updated.
            builder.setSavingRunnable(() -> {
                List<CommonFeature> commonFeatures = new LinkedList<>();
                List<ClientFeature> clientFeatures = new LinkedList<>();

                for (F feature : features) {
                    if (feature instanceof CommonFeature common) {
                        commonFeatures.add(common);
                    } else if (feature instanceof ClientFeature client) {
                        clientFeatures.add(client);
                    }
                }
                
                if (!commonFeatures.isEmpty()) {
                    var commonLoader = commonFeatures.getFirst().loader();
                    commonLoader.config().writeConfig(commonFeatures);
                }
                
                if (!clientFeatures.isEmpty()) {
                    var clientLoader = clientFeatures.getFirst().loader();
                    clientLoader.config().writeConfig(clientFeatures);
                }
            });

            var category = builder.getOrCreateCategory(Component.translatable("cloth.category." + id() + ".title"));

            for (var feature : features) {
                SubCategoryBuilder subcategory = null;
                var enabled = false;
                var name = feature.name();
                var description = feature.description();
                var properties = getFeatureConfigProperties(feature);

                if (feature.canBeDisabled()) {
                    subcategory = startSubcategory(builder, name, description);

                    var toggleFeatureName = Component.translatable("cloth.category." + id() + ".feature_enabled", name);
                    var defaultValue = feature.isEnabledByDefault();
                    enabled = feature.isEnabledInConfig();

                    var featureEntryBuilder = builder.entryBuilder()
                        .startBooleanToggle(toggleFeatureName, enabled)
                        .setDefaultValue(() -> defaultValue)
                        .setSaveConsumer(feature::setEnabledInConfig);

                    if (featureEntryBuilder != null) {
                        featureEntryBuilder.requireRestart();
                        subcategory.add(featureEntryBuilder.build());
                    }
                }

                for (Map.Entry<Field, Object> entry : properties.entrySet()) {
                    if (subcategory == null) {
                        subcategory = startSubcategory(builder, name, description);
                    }

                    var prop = entry.getKey();
                    var value = entry.getValue();

                    var annotation = prop.getDeclaredAnnotation(Configurable.class);
                    var propName = Component.literal(annotation.name());
                    var propDescription = Component.literal(TextHelper.splitOverLines(annotation.description()));
                    var requireRestart = annotation.requireRestart();

                    FieldBuilder<?, ?, ?> fieldBuilder = null;

                    if (value instanceof Boolean) {
                        fieldBuilder = builder.entryBuilder()
                            .startBooleanToggle(propName, (Boolean)value)
                            .setDefaultValue(() -> (Boolean)tryGetDefault(prop))
                            .setTooltip(propDescription)
                            .setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Integer) {
                        fieldBuilder = builder.entryBuilder()
                            .startIntField(propName, (Integer)value)
                            .setDefaultValue(() -> (Integer)tryGetDefault(prop))
                            .setTooltip(propDescription)
                            .setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Double) {
                        fieldBuilder = builder.entryBuilder()
                            .startDoubleField(propName, (Double)value)
                            .setDefaultValue(() -> (Double)tryGetDefault(prop))
                            .setTooltip(propDescription)
                            .setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Float) {
                        fieldBuilder = builder.entryBuilder()
                            .startFloatField(propName, (Float)value)
                            .setDefaultValue(() -> (Float)tryGetDefault(prop))
                            .setTooltip(propDescription)
                            .setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof String) {
                        fieldBuilder = builder.entryBuilder()
                            .startTextField(propName, (String)value)
                            .setDefaultValue(() -> (String)tryGetDefault(prop))
                            .setTooltip(propDescription)
                            .setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof List) {
                        fieldBuilder = builder.entryBuilder()
                            .startStrList(propName, (List<String>)value)
                            .setDefaultValue(() -> (List<String>)tryGetDefault(prop))
                            .setTooltip(propDescription)
                            .setSaveConsumer(val -> trySetProp(prop, val));
                    }

                    if (fieldBuilder != null) {
                        fieldBuilder.requireRestart(requireRestart);
                        subcategory.add(fieldBuilder.build());
                    }
                }

                if (subcategory != null) {
                    category.addEntry(subcategory.build());
                }
            }

            return builder.build();
        };
    }

    private Map<Field, Object> getFeatureConfigProperties(F feature) {
        Map<Field, Object> properties = new LinkedHashMap<>();

        // Get and set feature config options
        var classFields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
        classFields.forEach(prop -> {
            try {
                Configurable annotation = prop.getDeclaredAnnotation(Configurable.class);
                if (annotation == null) return;
                prop.setAccessible(true);

                Object value = prop.get(null);
                properties.put(prop, value);

            } catch (Exception e) {
                LOGGER.error("Failed to read config property " + prop.getName() + " in " + feature.name());
            }
        });

        return properties;
    }

    private void trySetProp(Field prop, Object val) {
        try {
            prop.set(null, val);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private Object tryGetDefault(Field prop) {
        return Config.defaultFieldValues().getOrDefault(prop, null);
    }

    private SubCategoryBuilder startSubcategory(ConfigBuilder builder, String name, String description) {
        var subcategory = builder.entryBuilder().startSubCategory(Component.literal(name));
        subcategory.add(builder.entryBuilder()
            .startTextDescription(Component.literal(description))
            .build());

        return subcategory;
    }
}
