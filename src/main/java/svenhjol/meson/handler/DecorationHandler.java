package svenhjol.meson.handler;

import net.minecraft.item.Items;
import svenhjol.meson.helper.DecorationHelper;

import java.util.Arrays;

public class DecorationHandler {
    public static void init() {
        DecorationHelper.FLOWERS.addAll(Arrays.asList(
            Items.DANDELION,
            Items.POPPY,
            Items.BLUE_ORCHID,
            Items.ALLIUM,
            Items.AZURE_BLUET,
            Items.RED_TULIP,
            Items.ORANGE_TULIP,
            Items.WHITE_TULIP,
            Items.PINK_TULIP,
            Items.OXEYE_DAISY,
            Items.CORNFLOWER,
            Items.LILY_OF_THE_VALLEY
        ));
    }
}
