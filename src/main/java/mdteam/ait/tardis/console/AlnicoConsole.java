package mdteam.ait.tardis.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.control.impl.*;
import mdteam.ait.tardis.control.impl.pos.IncrementControl;
import mdteam.ait.tardis.control.impl.pos.XControl;
import mdteam.ait.tardis.control.impl.pos.YControl;
import mdteam.ait.tardis.control.impl.pos.ZControl;
import mdteam.ait.tardis.control.impl.waypoint.InsertWaypointControl;
import mdteam.ait.tardis.control.impl.waypoint.LoadWaypointControl;
import mdteam.ait.tardis.control.impl.waypoint.SetWaypointControl;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class AlnicoConsole extends ConsoleSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/alnico");
    private static final ControlTypes[] TYPES = new ControlTypes[]{
            new ControlTypes(new ThrottleControl(), EntityDimensions.changing(0.13749997f, 0.6499997f), new Vector3f(-0.7468750113621354f, 0.27499999292194843f, 1.2648437786847353f)),
            new ControlTypes(new HandBrakeControl(), EntityDimensions.changing(0.16250001f, 0.275f), new Vector3f(-7.812660187482834E-4f, 0.5249998131766915f, 0.7894531497731805f)),
            new ControlTypes(new AutoPilotControl(), EntityDimensions.changing(0.07499999f, 0.21250002f), new Vector3f(-0.14609374105930328f, 0.18750018533319235f, 1.6886718971654773f)),
            new ControlTypes(new FastReturnControl(), EntityDimensions.changing(0.10000001f, 0.33749995f), new Vector3f(-0.8632812732830644f, 0.4625001894310117f, 0.8238281402736902f)),
            new ControlTypes(new DoorControl(), EntityDimensions.changing(0.125f, 0.11249998f), new Vector3f(-1.1898437859490514f, 0.3374998103827238f, -0.9148437744006515f)),
            new ControlTypes(new DoorLockControl(), EntityDimensions.changing(0.125f, 0.11249998f), new Vector3f(-1.2890625027939677f, 0.3374992338940501f, -0.7488281447440386f)),
            new ControlTypes(new AntiGravsControl(), EntityDimensions.changing(0.1375f, 0.1375f), new Vector3f(0.1898437375202775f, 0.44999999832361937f, 0.9023437546566129f)),
            new ControlTypes(new MonitorControl(), EntityDimensions.changing(0.33749995f, 0.29999998f), new Vector3f(0.7742187557742f, 0.5375017123296857f, 0.4632812477648258f)),
            new ControlTypes(new TelepathicControl(), EntityDimensions.changing(0.17500004f, 0.43749985f), new Vector3f(0.9625000189989805f, 0.4124998087063432f, -0.5523437457159162f)),
            new ControlTypes(new LandTypeControl(), EntityDimensions.changing(0.1375f, 0.1375f), new Vector3f(-0.1847656387835741f, 0.4525001486763358f, 0.9207031447440386f)),
            new ControlTypes(new IncrementControl(), EntityDimensions.changing(0.07499999f, 0.29999998f), new Vector3f(-1.4000000208616257f, 0.11250056512653828f, -0.9734375337138772f)),
            new ControlTypes(new XControl(), EntityDimensions.changing(0.1375f, 0.125f), new Vector3f(0.19023436307907104f, 0.4499998092651367f, -0.8828125176951289f)),
            new ControlTypes(new YControl(), EntityDimensions.changing(0.1375f, 0.125f), new Vector3f(0.0019531091675162315f, 0.4500000001862645f, -0.885937511920929f)),
            new ControlTypes(new ZControl(),  EntityDimensions.changing(0.1375f, 0.125f), new Vector3f(-0.18632814288139343f, 0.4499998092651367f, -0.8734375145286322f)),
            new ControlTypes(new RandomiserControl(), EntityDimensions.changing(0.1375f, 0.15f), new Vector3f(-1.477343775331974f, 0.2750001857057214f, 0.001953132450580597f)),
            new ControlTypes(new DirectionControl(), EntityDimensions.changing(0.125f, 0.1125f), new Vector3f(-1.3894531456753612f, 0.33749999944120646f, -0.5746094034984708f)),
            new ControlTypes(new HailMaryControl(), EntityDimensions.changing(0.16250001f, 0.16250001f), new Vector3f(-1.4480468789115548f, 0.4499996192753315f, 0.20195311773568392f)),
            new ControlTypes(new DimensionControl(), EntityDimensions.changing(0.33749995f, 0.16250001f), new Vector3f(-7.812408730387688E-4f, 0.3200000748038292f, -1.272656281478703f)),
            new ControlTypes(new RefuelerControl(), EntityDimensions.changing(0.125f, 0.1375f), new Vector3f(-0.7375000109896064f, 0.27499980479478836f, -1.2796875014901161f)),
            new ControlTypes(new PowerControl(), EntityDimensions.changing(0.15f, 0.275f), new Vector3f(0.7660156516358256f, 0.325000187382102f, -1.335937511175871f)),
            new ControlTypes(new SiegeModeControl(), EntityDimensions.changing(0.08749999f, 0.25000003f), new Vector3f(1.3046875381842256f, 0.22499999962747097f, -1.1363281505182385f)),
            new ControlTypes(new HADSControl(), EntityDimensions.changing(0.18750001f, 0.18750001f), new Vector3f(0.6902343910187483f, 0.49999980814754963f, 1.7648437842726707f)),
            new ControlTypes(new SetWaypointControl(), EntityDimensions.changing(0.099999994f, 0.29999998f), new Vector3f(-1.0492187775671482f, 0.4625003803521395f, 0.5886718826368451f)),
            new ControlTypes(new LoadWaypointControl(), EntityDimensions.changing(0.099999994f, 0.29999998f), new Vector3f(-1.1242187786847353f, 0.4625003803521395f, 0.45156250707805157f)),
            new ControlTypes(new InsertWaypointControl(), EntityDimensions.changing(0.22500002f, 0.22500002f), new Vector3f(0.701562499627471f, 0.3625005688518286f, 1.2007812643423676f)),
            new ControlTypes(new CloakControl(), EntityDimensions.changing(0.15f, 0.15f), new Vector3f(0.7523437663912773f, 0.5250003803521395f, -0.9609375214204192f)),
    };
    public AlnicoConsole() {
        super(REFERENCE, "alnico");
    }

    @Override
    public ControlTypes[] getControlTypes() {
        return TYPES;
    }
}