package me.pikamug.examplequests;

import me.pikamug.quests.module.BukkitCustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class ExampleReward extends BukkitCustomReward {

    public ExampleReward() {
        setName("Example Reward");
        setAuthor("PikaMug");
        setItem("CHEST", (short)0);
        addStringPrompt("Example Diamond Amount", "Enter the amount of Diamond to give", 1);
        setDisplay("You got %Example Diamond Amount% Diamond(s)!");
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
    public void giveReward(UUID uuid, Map<String, Object> data) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.getLogger().severe("[" + getModuleName() + "] Player was null for UUID " + uuid);
            return;
        }
        if (data != null && data.containsKey("Example Diamond Amount")) {
            final int amount = Integer.parseInt((String) data.get("Example Diamond Amount"));
            player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, amount));
        }
    }
}
