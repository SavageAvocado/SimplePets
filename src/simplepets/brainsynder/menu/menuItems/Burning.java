package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;

public class Burning extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.BLAZE_ROD);

    public Burning(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet var = (IEntityBlazePet) entityPet;
            item.setName("&6Burning: &e" + var.isBurning());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet pet = (IEntityBlazePet) entityPet;
            if (pet.isBurning()) {
                pet.setBurning(false);
            } else {
                pet.setBurning(true);
            }
        }
    }
}
