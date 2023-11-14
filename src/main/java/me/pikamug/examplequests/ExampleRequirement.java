package me.pikamug.examplequests;

import me.pikamug.quests.module.BukkitCustomRequirement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ExampleRequirement extends BukkitCustomRequirement {

    public ExampleRequirement() {
        setName("Example Requirement");
        setAuthor("PikaMug");
        setItem("STONE", (short)0);
        addStringPrompt("Example Hunger Level", "Minimum level of player hunger", 5);
        setDisplay("Player is too hungry. Grab a bite then try again!");
    }

    @Override
    public String getModuleName() {
        return ExampleModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return ExampleModule.getModuleItem();
    }

    @Override
    public boolean testRequirement(UUID uuid, Map<String, Object> data) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.getLogger().severe("[" + getModuleName() + "] Player was null for UUID " + uuid);
            return false;
        }
        if (data != null && data.containsKey("Example Hunger Level")) {
            final int level = Integer.parseInt((String) data.get("Example Hunger Level"));
            return player.getFoodLevel() > level;
        }
        return false;
    }
}
