package simplepets.brainsynder.links;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.impl.PlotSquaredLink;
import simplepets.brainsynder.links.impl.PluginLink;
import simplepets.brainsynder.links.impl.WorldBorderLink;
import simplepets.brainsynder.links.impl.WorldGuardLink;
import simplepets.brainsynder.links.impl.economy.*;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.TimerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LinkRetriever {
    private List<IPluginLink> loaders;

    public void earlyInitiate() {
        if (loaders == null) loaders = new ArrayList<>();
        if (!loaders.isEmpty()) loaders.clear();
        WorldGuardLink link = new WorldGuardLink();
        loaders.add(link);
        if (!link.isHooked()) {
            try {
                Plugin dependency = Bukkit.getPluginManager().getPlugin(link.getDependencyName());
                if (dependency != null) {
                    link.setHooked(link.onHook());
                    if (link.isHooked()) {
                        PetCore.get().debug("WorldGuard Successfully linked");
                    }else{
                        PetCore.get().debug("WorldGuard Could not be linked");
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    public void initiate() {
        if (loaders == null) loaders = new ArrayList<>();
        // Changed to >1 because otherwise it gets rid of the WorldGuard link which must link earlier
        if (loaders.size() > 1) loaders.clear();
        loaders.add(new PlotSquaredLink());
        loaders.add(new VaultLink());
        loaders.add(new TokenManagerLink());
        loaders.add(new GemEconomyLink());
        loaders.add(new WorldBorderLink());

        loaders.forEach(pluginLink -> {
            if (pluginLink instanceof PluginLink) {
                PluginLink link = (PluginLink) pluginLink;
                if (!link.isHooked()) {
                    try {
                        Plugin dependency = Bukkit.getPluginManager().getPlugin(link.getDependencyName());
                        if (dependency != null) {
                            link.setHooked(link.onHook());
                            if (link.isHooked()) {
                                PetCore.get().debug(dependency.getName() + " Successfully linked");
                            }else{
                                PetCore.get().debug(link.getDependencyName() + " Could not be linked");
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
        });
        loaders.add(new EXPEconomy());
        loaders.add(new EXPLevelEconomy());
    }

    public void cleanup () {
        TimerUtil.findDelay(getClass(), "Unloading Plugin Links");
        if (loaders == null) return;
        if (loaders.isEmpty()) return;
        loaders.forEach(IPluginLink::onUnhook);
        loaders.clear();
        loaders = null;
        TimerUtil.findDelay(getClass(), "Unloading Plugin Links");
    }

    public <T extends IProtectionLink> Optional<T> getProtectionLink(Class<T> clazz) {
        return getLink(clazz);
    }

    public <T extends IPluginLink> Optional<T> getPluginLink(Class<T> clazz) {
        return getLink(clazz);
    }

    private <T extends IPluginLink> Optional<T> getLink(Class<T> c) {
        checkLoaders();
        for (IPluginLink loader : loaders) {
            if (c.isAssignableFrom(loader.getClass())) {
                return Optional.of((T) loader);
            }
        }
        return Optional.empty();
    }

    public boolean canSpawnPet(Location location) {
        return canSpawnPet (null, location);
    }
    public boolean canPetEnter(Location location) {
        return canPetEnter (null, location);
    }

    public boolean canPetEnter(PetOwner owner, Location location) {
        checkLoaders();
        List<Boolean> list = new ArrayList<>();
        for (IPluginLink link : loaders) {
            if (!link.isHooked()) continue;
            if (!(link instanceof IProtectionLink)) continue;
            if (owner == null) {
                list.add(((IProtectionLink) link).allowPetEntry(location));
                continue;
            }
            list.add(((IProtectionLink) link).allowPetEntry(owner, location));
        }
        return (!list.contains(false));
    }

    public boolean canRidePet(PetOwner owner, Location location) {
        checkLoaders();
        List<Boolean> list = new ArrayList<>();
        for (IPluginLink link : loaders) {
            if (!link.isHooked()) continue;
            if (!(link instanceof IProtectionLink)) continue;
            if (owner == null) {
                list.add(((IProtectionLink) link).allowPetRiding(location));
                continue;
            }
            list.add(((IProtectionLink) link).allowPetRiding(owner, location));
        }
        return (!list.contains(false));
    }

    public boolean canSpawnPet(PetOwner owner, Location location) {
        checkLoaders();
        List<Boolean> list = new ArrayList<>();
        for (IPluginLink link : loaders) {
            if (!link.isHooked()) continue;
            if (!(link instanceof IProtectionLink)) continue;
            if (owner == null) {
                list.add(((IProtectionLink) link).allowPetSpawn(location));
                continue;
            }
            list.add(((IProtectionLink) link).allowPetSpawn(owner, location));
        }
        return (!list.contains(false)) && canPetEnter(owner, location);
    }

    private void checkLoaders() {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
    }
}