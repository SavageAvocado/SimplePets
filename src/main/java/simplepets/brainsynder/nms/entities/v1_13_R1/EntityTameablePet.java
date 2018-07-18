package simplepets.brainsynder.nms.entities.v1_13_R1;

import net.minecraft.server.v1_13_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.api.pet.IPet;

import java.util.Optional;
import java.util.UUID;

public class EntityTameablePet extends AgeableEntityPet implements ITameable {
    protected static final DataWatcherObject<Byte> TAME_SIT;
    protected static final DataWatcherObject<Optional<UUID>> OWNER;

    static {
        TAME_SIT = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.a);
        OWNER = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.o);
    }

    public EntityTameablePet(EntityTypes<?> type, World world) {
        super(type, world);
    }

    public EntityTameablePet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TAME_SIT, (byte) 0);
        this.datawatcher.register(OWNER, Optional.empty());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("tamed", isTamed());
        object.setBoolean("sitting", isSitting());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("tamed")) setTamed(object.getBoolean("tamed"));
        if (object.hasKey("sitting")) setSitting(object.getBoolean("sitting"));
        super.applyCompound(object);
    }

    public boolean isTamed() {
        return (this.datawatcher.get(TAME_SIT) & 4) != 0;
    }

    public void setTamed(boolean paramBoolean) {
        byte i = this.datawatcher.get(TAME_SIT);
        if (paramBoolean) {
            this.datawatcher.set(TAME_SIT, (byte) (i | 4));
        } else {
            this.datawatcher.set(TAME_SIT, (byte) (i & -5));
        }

    }

    public boolean isSitting() {
        return (this.datawatcher.get(TAME_SIT) & 1) != 0;
    }

    public void setSitting(boolean paramBoolean) {
        byte i = this.datawatcher.get(TAME_SIT);
        if (paramBoolean) {
            this.datawatcher.set(TAME_SIT, (byte) (i | 1));
        } else {
            this.datawatcher.set(TAME_SIT, (byte) (i & -2));
        }
    }
}
