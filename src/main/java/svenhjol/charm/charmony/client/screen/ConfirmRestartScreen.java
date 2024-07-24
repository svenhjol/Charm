package svenhjol.charm.charmony.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfirmRestartScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.charm.settings.confirm_restart.title");
    private static final Component DESC = Component.translatable("gui.charm.settings.confirm_restart.desc");
    private static final Component RESTART_NOW = Component.translatable("gui.charm.settings.restart_now");
    private static final Component RESTART_LATER = Component.translatable("gui.charm.settings.restart_later");

    private Button restartNow;
    private Button restartLater;

    public ConfirmRestartScreen() {
        super(TITLE);
    }

    private void quit() {
        if (minecraft != null) {
            minecraft.stop();
        }
    }

    @Override
    protected void init() {
        restartNow = Button.builder(RESTART_NOW, button -> quit()).width(180).build();
        restartLater = Button.builder(RESTART_LATER, button -> onClose()).width(180).build();

        this.addRenderableWidget(restartNow);
        this.addRenderableWidget(restartLater);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.render(guiGraphics, mouseX, mouseY, tickDelta);

        if (minecraft == null) {
            return;
        }

        int midX = width / 2;
        var font = minecraft.font;

        guiGraphics.drawCenteredString(font, TITLE, midX, 30, 0xffffff);
        guiGraphics.drawCenteredString(font, DESC, midX, 60, 0xa0a0a0);

        restartLater.setPosition(midX + 5, 150);
        restartNow.setPosition(midX - 185, 150);
    }
}
