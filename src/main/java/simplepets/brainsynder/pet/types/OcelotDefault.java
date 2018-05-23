package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class OcelotDefault extends PetDefault {
    public OcelotDefault(PetCore plugin) {
        super(plugin, "ocelot", SoundMaker.ENTITY_CAT_AMBIENT, EntityWrapper.OCELOT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1")
                .withName("&f&lOcelot Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityOcelotPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.OCELOT;
    }
}
