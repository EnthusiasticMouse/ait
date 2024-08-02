package loqor.ait.tardis.exterior.variant.plinth.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PlinthExteriorModel;
import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientPlinthVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/plinth";
	protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID, CATEGORY_PATH + "/plinth.png");
	protected static final String TEXTURE_PATH = CATEGORY_PATH + "/plinth_";

	protected static final BiomeOverrides OVERRIDES = BiomeOverrides.of(
			type -> type.getTexture(CATEGORY_IDENTIFIER)
	);

	protected ClientPlinthVariant(String name) {
		super(new Identifier(AITMod.MOD_ID, "exterior/plinth/" + name));

		this.name = name;
	}

	@Override
	public ExteriorModel model() {
		return new PlinthExteriorModel(PlinthExteriorModel.getTexturedModelData().createModel());
	}

	@Override
	public Identifier texture() {
		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
	}

	@Override
	public Identifier emission() {
		return null;
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.5f, 1.2f, 1.05f);
	}

	@Override
	public BiomeOverrides overrides() {
		return OVERRIDES;
	}
}