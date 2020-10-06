package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class ZombieVillagerPet extends PetType {
    public ZombieVillagerPet(PetCore plugin) {
        super(plugin, "zombie_villager", SoundMaker.ENTITY_ZOMBIE_VILLAGER_AMBIENT, EntityWrapper.ZOMBIE_VILLAGER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/e5e08a8776c1764c3fe6a6ddd412dfcb87f41331dad479ac96c21df4bf3ac89c")
                .withName("&f&lZombie Villager Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityZombieVillagerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ZOMBIE_VILLAGER;
    }
}
