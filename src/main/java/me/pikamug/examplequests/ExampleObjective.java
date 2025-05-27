package me.pikamug.examplequests;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.AbstractHandler;
import com.sk89q.worldguard.session.handler.Handler;
import com.sk89q.worldguard.session.handler.Handler.Factory;
import com.sk89q.worldguard.session.SessionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.quests.Quest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class RegionEnterObjective extends BukkitCustomObjective {

    public RegionEnterObjective() {
        setName("Enter Region Objective");
        setAuthor("MNIZ");
        setItem("ENDER_PEARL", (short) 0);
        setShowCount(false);
        addStringPrompt("Region Name", "Enter the exact WorldGuard region ID", "spawn_area");
        addStringPrompt("World Name", "Enter the world this region exists in", "world");
        setDisplay("Enter region: %Region Name%");

        // Register handler at runtime
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(RegionTracker.FACTORY, null);
    }

    @Override
    public String getModuleName() {
        return ExampleModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return ExampleModule.getModuleItem();
    }

    public static class RegionTracker extends AbstractHandler {
        private final UUID uuid;

        public static final Factory<RegionTracker> FACTORY = new Factory<RegionTracker>() {
            @Override
            public RegionTracker create(Session session) {
                return new RegionTracker(session);
            }
        };

        protected RegionTracker(Session session) {
            super(session);
            this.uuid = BukkitAdapter.adapt(session.getPlayer()).getUniqueId();
        }

        @Override
        public void onRegionChange(LocalPlayer localPlayer, ApplicableRegionSet from, ApplicableRegionSet to, com.sk89q.worldguard.session.MoveType moveType) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            String regionId = ExampleModule.getObjectiveData().getString(uuid, "Region Name");
            String worldName = ExampleModule.getObjectiveData().getString(uuid, "World Name");

            if (!player.getWorld().getName().equals(worldName)) return;

            for (ProtectedRegion region : to) {
                if (region.getId().equalsIgnoreCase(regionId)) {
                    for (Quest quest : ExampleModule.getQuests().getQuester(uuid).getCurrentQuests().keySet()) {
                        ExampleModule.getObjectiveData().completeObjective(uuid, new RegionEnterObjective(), quest);
                        return;
                    }
                }
            }
        }
    }
}
