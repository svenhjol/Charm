package svenhjol.charm.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;

import java.util.function.Predicate;

public class MusicCondition {
    private final SoundEvent sound;
    private final int minDelay;
    private final int maxDelay;
    private Predicate<MinecraftClient> condition;

    public MusicCondition(SoundEvent sound, int minDelay, int maxDelay, Predicate<MinecraftClient> condition) {
        this.sound = sound;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.condition = condition;
    }

    public MusicCondition(MusicSound music) {
        this.sound = music.getEvent();
        this.minDelay = music.method_27280();
        this.maxDelay = music.method_27281();
    }

    public boolean handle() {
        if (condition == null) return false;
        return condition.test(MinecraftClient.getInstance());
    }

    public SoundEvent getSound() {
        return sound;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public int getMinDelay() {
        return minDelay;
    }
}
