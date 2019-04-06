package svenhjol.charm.crafting.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.ITextComponent;
import svenhjol.charm.crafting.tile.TileCrate;

public class RenderTileCrate extends TileEntitySpecialRenderer<TileCrate>
{
    public void render(TileCrate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        ITextComponent itextcomponent = te.getDisplayName();

        if (itextcomponent != null
            && !itextcomponent.getFormattedText().isEmpty()
            && this.rendererDispatcher.cameraHitResult != null
            && te.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())
        )
        {
            this.setLightmapDisabled(true);
            this.drawNameplate(te, itextcomponent.getFormattedText(), x, y, z, 12);
            this.setLightmapDisabled(false);
        }
    }
}