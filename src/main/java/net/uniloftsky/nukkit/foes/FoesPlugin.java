package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import net.uniloftsky.nukkit.foes.observer.EventPublisher;

import java.util.List;

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
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);

        // initializing foes spawn area
        SpawnArea spawnArea = new SpawnArea(List.of(new Position(54, 86, 243, this.getServer().getDefaultLevel()), new Position(59, 86, 243, this.getServer().getDefaultLevel())), 5);
        SpawnAreaProcessor spawnAreaProcessor = new SpawnAreaProcessor(EntityZombie.NETWORK_ID, spawnArea);
        this.getServer().getScheduler().scheduleRepeatingTask(spawnAreaProcessor, 200);

        SpawnAreaSubscriber spawnAreaSubscriber = new SpawnAreaSubscriber(EntityZombie.NETWORK_ID, spawnArea, this.getLogger());
        EventPublisher.getInstance().subscribe(spawnAreaSubscriber);
    }

    /**
     * Invoked on plugin disabling, when server stops
     */
    @Override
    public void onDisable() {
        this.getLogger().info("FoesPlugin disabled!");

        // shutdown executor in EntityEventPublisher
        EventPublisher.getInstance().shutdown();
    }
}
