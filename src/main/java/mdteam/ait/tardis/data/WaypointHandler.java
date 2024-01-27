package mdteam.ait.tardis.data;

import mdteam.ait.core.item.WaypointItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.Waypoint;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class WaypointHandler extends TardisLink {
    public static final String HAS_CARTRIDGE = "has_cartridge";
    private Waypoint current; // The current waypoint in the slot ( tried to make it optional, but that caused a gson crash )
    public WaypointHandler(Tardis tardis) {
        super(tardis, "waypoint");
    }

    public boolean hasCartridge() {
        if (this.getTardis().isEmpty()) return false;
        return PropertiesHandler.getBool(this.getTardis().get().getHandlers().getProperties(), HAS_CARTRIDGE);
    }
    public void markHasCartridge() {
        if (this.getTardis().isEmpty()) return;
        PropertiesHandler.set(this.getTardis().get(), HAS_CARTRIDGE, true);
    }
    private void clearCartridge() {
        if (this.getTardis().isEmpty()) return;
        PropertiesHandler.set(this.getTardis().get(), HAS_CARTRIDGE, false);
    }

    // todo summon a new waypoint item at the console if spawnItem is true
    /**
     * Sets the new waypoint
     * @param var
     * @return The optional of the previous waypoiint
     */
    public Optional<Waypoint> set(Waypoint var, boolean spawnItem) {
        Optional<Waypoint> prev = Optional.ofNullable(this.current);
        // System.out.println(var);
        // System.out.println(this.current);
        this.current = var;

        if (spawnItem && prev.isPresent()) {
            this.spawnItem(prev.get());
        }

        return prev;
    }

    public Waypoint get() {
        return this.current;
    }
    public boolean hasWaypoint() {
        return this.current != null;
    }
    public void clear(boolean spawnItem) {
        this.set(null, spawnItem);
    }

    public void gotoWaypoint() {
        if (getTardis().isEmpty() || !this.hasWaypoint()) return; // todo move this check to the DEMAT event so the fail to takeoff happens

        PropertiesHandler.setAutoPilot(this.getTardis().get().getHandlers().getProperties(), true);
        FlightUtil.travelTo(getTardis().get(), this.get());
    }
    public void setDestination() {
        if (getTardis().isEmpty() || !this.hasWaypoint()) return;

        this.getTardis().get().getTravel().setDestination(this.get(), true);
    }

    public void spawnItem() {
        if (!this.hasWaypoint()) return;

        spawnItem(this.get());
        this.clear(false);
    }

    public void spawnItem(Waypoint waypoint) {
        if (getTardis().isEmpty() ||!this.hasCartridge()) return;

        spawnItem(waypoint, this.getTardis().get().getDesktop().getConsolePos());
        this.clearCartridge();
    }

    public static ItemStack createWaypointItem(Waypoint waypoint) {
        return WaypointItem.create(waypoint);
    }
    public static ItemEntity spawnItem(Waypoint waypoint, AbsoluteBlockPos pos) {
        ItemEntity entity = new ItemEntity(pos.getWorld(), pos.getX(), pos.getY(), pos.getZ(), createWaypointItem(waypoint));
        pos.getWorld().spawnEntity(entity);
        return entity;
    }
}
