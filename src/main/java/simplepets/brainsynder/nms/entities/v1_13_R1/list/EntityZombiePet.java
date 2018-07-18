package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.AgeableEntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.6F, length = 1.8F)
public class EntityZombiePet extends AgeableEntityPet implements IEntityZombiePet {
    private static final DataWatcherObject<Integer> VILLAGER_TYPE;
    private static final DataWatcherObject<Boolean> ARMS_RAISED;

    static {
        VILLAGER_TYPE = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.b);
        ARMS_RAISED = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.i);
    }

    public EntityZombiePet(World world) {
        super(Types.ZOMBIE, world);
    }
    public EntityZombiePet(World world, IPet pet) {
        super(Types.ZOMBIE, world, pet);
    }
    public EntityZombiePet(EntityTypes<?> type, World world) {
        super(type, world);
    }
    public EntityZombiePet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.getDataWatcher().register(VILLAGER_TYPE, 0);
        this.getDataWatcher().register(ARMS_RAISED, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("raised", isArmsRaised());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("raised"))
            setArmsRaised(object.getBoolean("raised"));
        super.applyCompound(object);
    }

    @Override
    public boolean isArmsRaised() {
        return datawatcher.get(ARMS_RAISED);
    }

    @Override
    public void setArmsRaised(boolean flag) {
        datawatcher.set(ARMS_RAISED, flag);
    }
}
