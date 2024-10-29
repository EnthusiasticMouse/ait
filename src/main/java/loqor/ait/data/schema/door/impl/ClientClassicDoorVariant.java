package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.ClassicDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientClassicDoorVariant extends ClientDoorSchema {
    public ClientClassicDoorVariant() {
        super(ClassicDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new ClassicDoorModel(ClassicDoorModel.getTexturedModelData().createModel());
    }
}
