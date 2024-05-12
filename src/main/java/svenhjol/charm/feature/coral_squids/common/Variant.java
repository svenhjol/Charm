package svenhjol.charm.feature.coral_squids.common;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;

import java.util.function.IntFunction;

public enum Variant implements StringRepresentable {
    TUBE(0, "tube", Items.TUBE_CORAL, false),
    BRAIN(1, "brain", Items.BRAIN_CORAL, false),
    BUBBLE(2, "bubble", Items.BUBBLE_CORAL, false),
    FIRE(3, "fire", Items.FIRE_CORAL, false),
    HORN(4, "horn", Items.HORN_CORAL, false);

    private static final IntFunction<Variant> BY_ID;
    public static final Codec<Variant> CODEC;

    private final int id;
    private final String name;
    private final Item drop;
    private final boolean rare;

    Variant(int id, String name, Item drop, boolean rare) {
        this.id = id;
        this.name = name;
        this.drop = drop;
        this.rare = rare;
    }

    public static Variant byId(int id) {
        return BY_ID.apply(id);
    }

    public static Variant randomly(RandomSource random) {
        var max = values().length;
        var id = random.nextInt(max);
        return values()[id];
    }

    public int getId() {
        return id;
    }

    public Item getDrop() {
        return drop;
    }

    public boolean isRare() {
        return rare;
    }

    public ResourceLocation getTexture() {
        return Charm.id("textures/entity/coral_squid/" + name + ".png");
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    static {
        BY_ID = ByIdMap.continuous(Variant::getId, Variant.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        CODEC = StringRepresentable.fromEnum(Variant::values);
    }
}
