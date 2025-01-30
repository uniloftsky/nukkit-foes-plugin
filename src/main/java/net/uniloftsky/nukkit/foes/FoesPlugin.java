package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import net.uniloftsky.nukkit.foes.ai.zombie.MyZombie;

import java.util.List;

public class FoesPlugin extends PluginBase {

    private static FoesPlugin INSTANCE;

    public static FoesPlugin getInstance() {
        return INSTANCE;
    }

    private EventListener eventListener;

    /**
     * Invoked on plugin enabling, when server starts
     */
    @Override
    public void onEnable() {
        INSTANCE = this;
        eventListener = new EventListener();

        this.getServer().getPluginManager().registerEvents(eventListener, this);

        EntityZombie.registerEntity(String.valueOf(MyZombie.NETWORK_ID), MyZombie.class);

        // initializing foes spawn area
        SpawnArea spawnArea = new SpawnArea(List.of(new Position(54, 86, 243, this.getServer().getDefaultLevel()), new Position(59, 86, 243, this.getServer().getDefaultLevel())), 1);

        int entityId = MyZombie.NETWORK_ID;
        SpawnAreaProcessor spawnAreaProcessor = new SpawnAreaProcessor(entityId, spawnArea);
        this.getServer().getScheduler().scheduleRepeatingTask(spawnAreaProcessor, 200);

        SpawnAreaEventSubscriber spawnAreaEventSubscriber = new SpawnAreaEventSubscriber(entityId, spawnArea, this.getLogger());
        eventListener.subscribe(spawnAreaEventSubscriber);

        this.getLogger().info("FoesPlugin enabled!");
    }

    /**
     * Invoked on plugin disabling, when server stops
     */
    @Override
    public void onDisable() {
        this.getLogger().info("FoesPlugin disabled!");

        // shutdown executor in EntityEventPublisher
        eventListener.shutdown();
    }
}
