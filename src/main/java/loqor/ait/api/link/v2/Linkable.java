package loqor.ait.api.link.v2;

import java.util.UUID;

import loqor.ait.api.link.v2.block.InteriorLinkableBlockEntity;
import loqor.ait.core.tardis.Tardis;

public interface Linkable {

    void link(Tardis tardis);

    void link(UUID id);

    TardisRef tardis();

    /**
     * @implNote This method is called when the TardsRef instance gets created. This
     *           means that the ref is no longer null BUT the TARDIS instance still
     *           could be missing. Primarily this is true for
     *           {@link InteriorLinkableBlockEntity}s
     */
    default void onLinked() {
    }
}
