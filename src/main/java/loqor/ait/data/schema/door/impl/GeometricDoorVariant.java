package loqor.ait.data.schema.door.impl;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import loqor.ait.AITMod;
import loqor.ait.data.schema.door.DoorSchema;

public class GeometricDoorVariant extends DoorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/geometric");

    public GeometricDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public SoundEvent openSound() {
        return SoundEvents.BLOCK_GRINDSTONE_USE;
    }

    @Override
    public SoundEvent closeSound() {
        return SoundEvents.BLOCK_GRINDSTONE_USE;
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0, 0.1, -0.42);
            case SOUTH -> pos.add(0, 0.1, 0.42);
            case WEST -> pos.add(-0.42, 0.1, 0);
            case EAST -> pos.add(0.42, 0.1, 0);
        };
    }
}
