package svenhjol.charm.tweaks.feature;

import svenhjol.charm.base.CharmLoadingPlugin;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ConfigHelper;

public class GoldToolImprovements extends Feature
{
    public static int durability;
    public static double efficiency;
    public static double attackDamage;
    public static int harvestLevel;
    public static int enchantability;

    public static final String MATERIAL_NAME = "GOLD";
    public static final String CONFIG_FEATURE_CATEGORY = "CharmTweaks";
    public static final String CONFIG_FEATURE_NAME = "GoldToolImprovements";
    public static final String CONFIG_VALUES_CATEGORY = "CharmTweaks.GoldToolImprovements";
    public static final String CONFIG_DURABILITY = "Gold tool durability";
    public static final String CONFIG_EFFICIENCY = "Gold tool efficiency";
    public static final String CONFIG_ATTACK_DAMAGE = "Gold tool damage";
    public static final String CONFIG_HARVEST_LEVEL = "Gold tool harvest level";
    public static final String CONFIG_ENCHANTABILITY = "Gold tool enchantability";

    public static final int CUSTOM_DURABILITY = 512;
    public static final float CUSTOM_EFFICIENCY = 17.0f;
    public static final float CUSTOM_ATTACK_DAMAGE = 0.0f;
    public static final int CUSTOM_HARVEST_LEVEL = 0;
    public static final int CUSTOM_ENCHANTABILITY = 22;

    @Override
    public String getDescription()
    {
        return "Improves the base durability and efficiency of gold tools.";
    }

    @Override
    public void setupConfig()
    {
        durability = propInt(CONFIG_DURABILITY, "Default durability of gold tools. Vanilla is 32.", CUSTOM_DURABILITY);
        efficiency = propDouble(CONFIG_EFFICIENCY, "Default efficiency of gold tools. Vanilla is 12.0.", CUSTOM_EFFICIENCY);
        attackDamage = propDouble(CONFIG_ATTACK_DAMAGE, "Default attack of gold tools. Vanilla is 0.0.", CUSTOM_ATTACK_DAMAGE);
        harvestLevel = propInt(CONFIG_HARVEST_LEVEL, "Default harvest level of gold tools. Vanilla is 0.", CUSTOM_HARVEST_LEVEL);
        enchantability = propInt(CONFIG_ENCHANTABILITY, "Default enchantability of gold tools. Vanilla is 22.", CUSTOM_ENCHANTABILITY);
    }

    public static boolean isEnabled()
    {
        return ConfigHelper.propBoolean(CharmLoadingPlugin.config, CONFIG_FEATURE_NAME, CONFIG_FEATURE_CATEGORY, "", true);
    }

    public static int getMaxUses(int itemDefault)
    {
        if (!isEnabled()) return itemDefault;
        durability = ConfigHelper.propInt(CharmLoadingPlugin.config, CONFIG_DURABILITY, CONFIG_VALUES_CATEGORY, "", CUSTOM_DURABILITY);
        return durability;
    }

    public static double getEfficiency(double itemDefault)
    {
        if (!isEnabled()) return itemDefault;
        efficiency = ConfigHelper.propDouble(CharmLoadingPlugin.config, CONFIG_EFFICIENCY, CONFIG_VALUES_CATEGORY, "", CUSTOM_EFFICIENCY);
        return efficiency;
    }

    public static double getAttackDamage(double itemDefault)
    {
        if (!isEnabled()) return itemDefault;
        attackDamage = ConfigHelper.propDouble(CharmLoadingPlugin.config, CONFIG_ATTACK_DAMAGE, CONFIG_VALUES_CATEGORY, "", CUSTOM_ATTACK_DAMAGE);
        return attackDamage;
    }

    public static int getHarvestLevel(int itemDefault)
    {
        if (!isEnabled()) return itemDefault;
        harvestLevel = ConfigHelper.propInt(CharmLoadingPlugin.config, CONFIG_HARVEST_LEVEL, CONFIG_VALUES_CATEGORY, "", CUSTOM_HARVEST_LEVEL);
        return harvestLevel;
    }

    public static int getEnchantability(int itemDefault)
    {
        if (!isEnabled()) return itemDefault;
        enchantability = ConfigHelper.propInt(CharmLoadingPlugin.config, CONFIG_ENCHANTABILITY, CONFIG_VALUES_CATEGORY, "", CUSTOM_ENCHANTABILITY);
        return enchantability;
    }
}
