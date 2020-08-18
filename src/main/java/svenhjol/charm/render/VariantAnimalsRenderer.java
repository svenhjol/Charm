package svenhjol.charm.render;

import net.minecraft.client.render.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantAnimalsRenderer {
    public static class Chicken extends ChickenEntityRenderer {
        public Chicken(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public Identifier getTexture(ChickenEntity entity) {
            return VariantAnimalTextures.getChickenTexture(entity);
        }
    }

    public static class Cow extends CowEntityRenderer {
        public Cow(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public Identifier getTexture(CowEntity entity) {
            return VariantAnimalTextures.getCowTexture(entity);
        }
    }

    public static class Pig extends PigEntityRenderer {
        public Pig(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public Identifier getTexture(PigEntity entity) {
            return VariantAnimalTextures.getPigTexture(entity);
        }
    }

    public static class Squid extends SquidEntityRenderer {
        public Squid(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public Identifier getTexture(SquidEntity entity) {
            return VariantAnimalTextures.getSquidTexture(entity);
        }
    }

    public static class Wolf extends WolfEntityRenderer {
        public Wolf(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public Identifier getTexture(WolfEntity entity) {
            return VariantAnimalTextures.getWolfTexture(entity);
        }
    }
}
