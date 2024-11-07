package net.uniloftsky.nukkit.foes;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import net.uniloftsky.nukkit.foes.listener.EventListener;
import net.uniloftsky.nukkit.foes.listener.observer.EntityEventPublisher;

import java.util.List;
import java.util.Map;

public class FoesPlugin extends PluginBase {

    private static FoesPlugin INSTANCE;

    public static FoesPlugin getInstance() {
        return INSTANCE;
    }

    /**
     * Invoked on plugin enabling, when server starts
     */
    @Override
    public void onEnable() {
        INSTANCE = this;
        this.getLogger().info("FoesPlugin enabled!");
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

        // initializing foes spawn area
        SpawnArea zombieSpawnArea = new SpawnArea(EntityZombie.NETWORK_ID, List.of(new Position(54, 86, 243, this.getServer().getDefaultLevel()), new Position(59, 86, 243, this.getServer().getDefaultLevel())), 5);
        this.getServer().getScheduler().scheduleRepeatingTask(zombieSpawnArea, 200);
    }

    /**
     * Invoked on plugin disabling, when server stops
     */
    @Override
    public void onDisable() {
        this.getLogger().info("FoesPlugin disabled!");

        // kill all alive entities
        removeAliveFoes();

        // shutdown executor in EntityEventPublisher
        EntityEventPublisher.shutdownPublisher();
    }

    private void removeAliveFoes() {
        Map<Integer, Level> levels = Server.getInstance().getLevels();
        for (Level level : levels.values()) {
            for (Entity entity : level.getEntities()) {
                if (entity instanceof EntityLiving livingEntity) {
                    livingEntity.close();
                }
            }
        }
    }
}
