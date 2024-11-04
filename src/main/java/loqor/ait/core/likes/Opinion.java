package loqor.ait.core.likes;

import java.util.Optional;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import loqor.ait.api.Identifiable;
import loqor.ait.core.tardis.ServerTardis;

public interface Opinion extends Identifiable {
    int loyalty();
    Type type();

    default boolean likes() {
        return loyalty() > 0;
    }
    default void apply(ServerTardis tardis, ServerPlayerEntity target) {
        tardis.loyalty().addLevel(target, loyalty());
    }

    enum Type {
        ITEM {
            @Override
            public Opinion get(Identifier id) {
                return ItemOpinionRegistry.getInstance().get(id);
            }
        },
        ;

        public abstract Opinion get(Identifier id);
    };

    static Optional<Opinion> find(Identifier id) {
        for (Type type : Type.values()) {
            Opinion opinion = type.get(id);
            if (opinion != null) {
                return Optional.of(opinion);
            }
        }

        return Optional.empty();
    }
}