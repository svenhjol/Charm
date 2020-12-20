package svenhjol.charm.render;

import net.minecraft.client.render.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.Identifier;
import svenhjol.charm.client.VariantMobTexturesClient;

public class VariantMobRenderer {
    public static class Chicken extends ChickenEntityRenderer {
        public Chicken(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(ChickenEntity entity) {
            return VariantMobTexturesClient.getChickenTexture(entity);
        }
    }

    public static class Cow extends CowEntityRenderer {
        public Cow(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(CowEntity entity) {
            return VariantMobTexturesClient.getCowTexture(entity);
        }
    }

    public static class Pig extends PigEntityRenderer {
        public Pig(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(PigEntity entity) {
            return VariantMobTexturesClient.getPigTexture(entity);
        }
    }

    public static class Sheep extends SheepEntityRenderer {
        public Sheep(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(SheepEntity entity) {
            return VariantMobTexturesClient.getSheepTexture(entity);
        }
    }

    public static class SnowGolem extends SnowGolemEntityRenderer {
        public SnowGolem(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(SnowGolemEntity entity) {
            return VariantMobTexturesClient.getSnowGolemTexture(entity);
        }
    }

    public static class Squid extends SquidEntityRenderer {
        public Squid(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(SquidEntity entity) {
            return VariantMobTexturesClient.getSquidTexture(entity);
        }
    }

    public static class Wolf extends WolfEntityRenderer {
        public Wolf(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(WolfEntity entity) {
            return VariantMobTexturesClient.getWolfTexture(entity);
        }
    }
}
