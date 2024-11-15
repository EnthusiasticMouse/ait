package loqor.ait.core.sounds.flight;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.Identifiable;
import loqor.ait.core.AITSounds;

public record FlightSound(Identifier id, Identifier soundId, int length) implements Identifiable {
    public static final Codec<FlightSound> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(FlightSound::id),
                    Identifier.CODEC.fieldOf("sound").forGetter(FlightSound::soundId),
                    Codec.INT.fieldOf("length").forGetter(FlightSound::length)
            ).apply(instance, FlightSound::new)
    );

    @Override
    public Identifier id() {
        return this.id;
    }

    public SoundEvent sound() {
        SoundEvent sfx = Registries.SOUND_EVENT.get(this.soundId());

        if (sfx == null) {
            AITMod.LOGGER.error("Unknown sound event: {} in flight sfx {}", this.soundId(), this.id());
            sfx = AITSounds.ERROR;
        }

        return sfx;
    }

    public static FlightSound fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }

    public static FlightSound fromJson(JsonObject json) {
        AtomicReference<FlightSound> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json).get().ifLeft(var -> created.set(var.getFirst())).ifRight(err -> {
            created.set(null);
            AITMod.LOGGER.error("Error decoding datapack flight sfx: {}", err);
        });

        return created.get();
    }
}