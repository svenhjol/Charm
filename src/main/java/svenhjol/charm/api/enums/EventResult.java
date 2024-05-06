package svenhjol.charm.api.enums;

import net.minecraft.world.InteractionResult;

public enum EventResult {
    NONE,
    PASS,
    CANCEL,
    SUCCESS,
    CONSUME;

    public InteractionResult asInteractionResult() {
        return asInteractionResult(false);
    }

    public InteractionResult asInteractionResult(boolean isClient) {
        return switch (this) {
            case CONSUME -> InteractionResult.CONSUME;
            case CANCEL -> InteractionResult.FAIL;
            case SUCCESS -> InteractionResult.sidedSuccess(isClient);
            case NONE, PASS -> InteractionResult.PASS;
        };
    }
}
