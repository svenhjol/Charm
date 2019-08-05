package svenhjol.charm.world.client.model;

import net.minecraft.client.model.ModelBiped;

public class ModelSpectre extends ModelBiped
{
    public ModelSpectre() {
        this(0.0F, false);
    }

    public ModelSpectre(float modelSize, boolean p_i1168_2_) {
        super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
    }
}
