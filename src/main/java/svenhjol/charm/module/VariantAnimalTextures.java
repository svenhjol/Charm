package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.client.VariantAnimalTexturesClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IMesonEnum;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.*;

@Module(description = "Animals may spawn with different textures.")
public class VariantAnimalTextures extends MesonModule {
    private static final String PREFIX = "textures/entity/";
    private static VariantAnimalTexturesClient client;

    public static List<Identifier> wolves = new ArrayList<>();
    public static List<Identifier> cows = new ArrayList<>();
    public static List<Identifier> squids = new ArrayList<>();
    public static List<Identifier> chickens = new ArrayList<>();
    public static List<Identifier> pigs = new ArrayList<>();

    public static List<Identifier> rareWolves = new ArrayList<>();
    public static List<Identifier> rareCows = new ArrayList<>();
    public static List<Identifier> rareSquids = new ArrayList<>();
    public static List<Identifier> rareChickens = new ArrayList<>();
    public static List<Identifier> rarePigs = new ArrayList<>();

    public static Map<Identifier, Identifier> wolvesTame = new HashMap<>();
    public static Map<Identifier, Identifier> wolvesAngry = new HashMap<>();

    @Config(name = "Variant wolves", description = "If true, wolves may spawn with different textures.")
    public static boolean variantWolves = true;

    @Config(name = "Variant squids", description = "If true, squids may spawn with different textures.")
    public static boolean variantSquids = true;

    @Config(name = "Variant cows", description = "If true, cows may spawn with different textures.")
    public static boolean variantCows = true;

    @Config(name = "Variant chickens", description = "If true, chickens may spawn with different textures.")
    public static boolean variantChickens = true;

    @Config(name = "Variant pigs", description = "If true, pigs may spawn with different textures.")
    public static boolean variantPigs = true;

    @Config(name = "Rare variants", description = "If true, all animals have a chance to spawn as a rare variant.")
    public static boolean rareVariants = true;

    @Override
    public void initClient() {
        client = new VariantAnimalTexturesClient(this);
    }

    @Override
    public void setup() {
        // add vanilla textures
        chickens.add(new Identifier(PREFIX + "chicken.png"));

        for (int i = 1; i <= 5; i++)
            addCustomTextures(chickens, MobType.CHICKEN, "chicken" + i);

        for (int i = 1; i <= 2; i++)
            addCustomTextures(rareChickens, MobType.CHICKEN, "rare_chicken" + i);
    }

    public void addCustomTextures(List<Identifier> set, MobType type, String... names) {
        ArrayList<String> textures = new ArrayList<>(Arrays.asList(names));

        textures.forEach(texture -> {
            Identifier res = createResource(type, texture);
            set.add(res);

            if (type == MobType.WOLF) {
                wolvesTame.put(res, createResource(type, texture + "_tame"));
                wolvesAngry.put(res, createResource(type, texture + "_angry"));
            }
        });
    }

    public static Identifier getChickenTexture(ChickenEntity entity) {
        return getRandomTexture(entity, chickens, rareChickens);
    }

    public static Identifier getRandomTexture(Entity entity, List<Identifier> normalSet, List<Identifier> rareSet) {
        UUID id = entity.getUuid();
        boolean isRare = rareVariants && !rareSet.isEmpty() && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % 50 == 0;

        List<Identifier> set = isRare ? rareSet : normalSet;
        int choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    private Identifier createResource(MobType type, String texture) {
        return new Identifier(Charm.MOD_ID, PREFIX + type.asString() + "/" + texture + ".png");
    }

    public enum MobType implements IMesonEnum { WOLF, COW, PIG, CHICKEN, SQUID }
}
