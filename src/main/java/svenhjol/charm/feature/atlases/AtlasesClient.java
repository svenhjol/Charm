package svenhjol.charm.feature.atlases;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Networking;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class AtlasesClient extends ClientFeature {
    static final RenderType ATLAS_BACKGROUND = RenderType.text(Charm.id("textures/map/atlas.png"));
    static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));
    static final ResourceLocation CONTAINER_BACKGROUND = Charm.id("textures/gui/atlas.png");
    static final WidgetSprites UP_BUTTON = makeButton("up");
    static final WidgetSprites DOWN_BUTTON = makeButton("down");
    static final WidgetSprites LEFT_BUTTON = makeButton("left");
    static final WidgetSprites RIGHT_BUTTON = makeButton("right");
    static final WidgetSprites BACK_BUTTON = makeButton("back");
    static final WidgetSprites ZOOM_IN_BUTTON = makeButton("zoom_in");
    static final WidgetSprites ZOOM_OUT_BUTTON = makeButton("zoom_out");

    static Supplier<String> openAtlasKey;
    static int swappedSlot = -1;

    @Override
    public Class<? extends CommonFeature> commonClass() {
        return Atlases.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }

    @Override
    public Optional<Networking<? extends Feature>> networking() {
        return Optional.of(new ClientNetworking(this));
    }

    public static void handleUpdateInventory(Player player, CommonNetworking.S2CUpdateInventory packet) {
        var slot = packet.getSlot();
        ItemStack atlas = player.getInventory().getItem(slot);
        AtlasInventory.get(player.level(), atlas).reload(atlas);
    }

    @SuppressWarnings("unused")
    public static void handleSwappedSlot(Player player, CommonNetworking.S2CSwappedAtlasSlot packet) {
        swappedSlot = packet.getSlot();
    }

    private static WidgetSprites makeButton(String name) {
        var instance = Globals.client(Charm.ID);

        return new WidgetSprites(
            instance.id("widget/atlases/" + name + "_button"),
            instance.id("widget/atlases/" + name + "_button_disabled"),
            instance.id("widget/atlases/" + name + "_button_highlighted"));
    }
}
