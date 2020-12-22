package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPigZombiePet;
import simplepets.brainsynder.api.pet.IPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityPigZombie}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityPigZombiePet extends EntityZombiePet implements IEntityPigZombiePet {
    public EntityPigZombiePet(World world, IPet pet) {
        super(EntityTypes.ZOMBIFIED_PIGLIN, world, pet);
    }
    public EntityPigZombiePet(World world) {
        super(EntityTypes.ZOMBIFIED_PIGLIN, world);
    }
}