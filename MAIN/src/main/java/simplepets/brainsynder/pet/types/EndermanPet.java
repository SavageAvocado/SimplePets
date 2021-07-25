package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class EndermanPet extends PetType {
    public EndermanPet(PetCore plugin) {
        super(plugin, "enderman", SoundMaker.ENTITY_ENDERMEN_AMBIENT, EntityWrapper.ENDERMAN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.ENDER_PEARL).withName("&f&lEnderman Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityEndermanPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SCREAM;
    }
}