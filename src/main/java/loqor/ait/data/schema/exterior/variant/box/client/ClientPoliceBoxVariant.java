package loqor.ait.data.schema.exterior.variant.box.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PoliceBoxModel;
import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.data.schema.exterior.ClientExteriorVariantSchema;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientPoliceBoxVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/police_box";
    protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID,
            CATEGORY_PATH + "/police_box.png");
    protected static final String TEXTURE_PATH = CATEGORY_PATH + "/police_box_";

    protected static final BiomeOverrides OVERRIDES = BiomeOverrides.of(type -> type.getTexture(CATEGORY_IDENTIFIER));

    protected ClientPoliceBoxVariant(String name) {
        super(AITMod.id("exterior/police_box/" + name));

        this.name = name;
    }

    @Override
    public ExteriorModel model() {
        return new PoliceBoxModel(PoliceBoxModel.getTexturedModelData().createModel());
    }

    @Override
    public Identifier texture() {
        return AITMod.id(TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return AITMod.id(TEXTURE_PATH + name + "_emission" + ".png");
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.56f, 1.2f, 1.2f);
    }

    @Override
    public BiomeOverrides overrides() {
        return OVERRIDES;
    }
}
