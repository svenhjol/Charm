package svenhjol.charm.api.enums;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;

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

    public ItemInteractionResult asItemInteractionResult(boolean isClient) {
        return switch (this) {
            case CONSUME -> ItemInteractionResult.CONSUME;
            case CANCEL -> ItemInteractionResult.FAIL;
            case SUCCESS -> ItemInteractionResult.sidedSuccess(isClient);
            case NONE, PASS -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        };
    }
}
