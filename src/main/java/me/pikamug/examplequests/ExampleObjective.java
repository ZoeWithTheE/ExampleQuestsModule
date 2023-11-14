package me.pikamug.examplequests;

import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class ExampleObjective extends BukkitCustomObjective {

    public ExampleObjective() {
        setName("Example Objective");
        setAuthor("PikaMug");
        setItem("GRAVEL", (short)0);
        setShowCount(true);
        addStringPrompt("Example Objective Name", "Set a name for the objective", "Break ANY block");
        setCountPrompt("Set the amount of blocks to break");
        setDisplay("%Example Objective Name%: %count%");
    }

    @Override
    public String getModuleName() {
        return ExampleModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return ExampleModule.getModuleItem();
    }

    @EventHandler(priority = EventPriority.LOW) // TODO - Always consider how priority will affect server plugins!
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        for (Quest quest : ExampleModule.getQuests().getQuester(player.getUniqueId()).getCurrentQuests().keySet()) {
            incrementObjective(player.getUniqueId(), this, quest, 1);
            return;
        }
    }
}
