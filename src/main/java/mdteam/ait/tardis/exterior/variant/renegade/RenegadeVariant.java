package mdteam.ait.tardis.exterior.variant.renegade;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.animation.PulsatingAnimation;
import mdteam.ait.tardis.exterior.category.PlinthCategory;
import mdteam.ait.tardis.exterior.category.RenegadeCategory;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.PlinthDoorVariant;
import mdteam.ait.tardis.variant.door.RenegadeDoorVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class RenegadeVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/renegade/renegade_";

    protected RenegadeVariant(String name, String modId) {
        super(RenegadeCategory.REFERENCE, new Identifier(modId, "exterior/renegade/" + name));

        this.name = name;
    }
    protected RenegadeVariant(String name) {
        this(name, AITMod.MOD_ID);
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(RenegadeDoorVariant.REFERENCE);
    }

    @Override
    public boolean hasPortals() {
        return true;
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0,0.4875,-0.4);
            case SOUTH -> pos.add(0,0.4875,0.4);
            case WEST -> pos.add(-0.4,0.4875,0);
            case EAST -> pos.add(0.4,0.4875,0);
        };
    }

    @Override
    public double portalHeight() {
        return 2.7d;
    }

    @Override
    public double portalWidth() {
        return 1d;
    }
}