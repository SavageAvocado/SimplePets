package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class ZombiePet extends PetType {
    public ZombiePet(PetCore plugin) {
        super(plugin, "zombie", SoundMaker.ENTITY_ZOMBIE_AMBIENT, EntityWrapper.ZOMBIE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.ZOMBIE_HEAD).withName("&f&lZombie Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityZombiePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ZOMBIE;
    }
}
