package svenhjol.charm.feature.casks.common;

import net.minecraft.world.item.ItemStack;
import svenhjol.charm.charmony.helper.ItemNbtHelper;

public record HomeBrewData(double fermentation) implements ItemNbtHelper.BackportedData {
    private static final String FERMENTATION_TAG = "fermentation";
    
    public static final HomeBrewData EMPTY = new HomeBrewData(CaskData.DEFAULT_FERMENTATION);

    public static HomeBrewData load(ItemStack stack) {
        var fermentation = ItemNbtHelper.getDouble(stack, FERMENTATION_TAG, CaskData.DEFAULT_FERMENTATION);
        return new HomeBrewData(fermentation);
    }
    
    @Override
    public void set(ItemStack stack) {
        ItemNbtHelper.setDouble(stack, FERMENTATION_TAG, fermentation());
    }

    @Override
    public HomeBrewData get(ItemStack stack) {
        return load(stack);
    }

    @Override
    public boolean has(ItemStack stack) {
        return ItemNbtHelper.hasTag(stack, FERMENTATION_TAG);
    }
}
