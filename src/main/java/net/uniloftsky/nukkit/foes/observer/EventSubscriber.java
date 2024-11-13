package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;
import cn.nukkit.plugin.PluginLogger;

import java.util.*;

/**
 * Abstract class for event subscribers.
 * <p>
 * Subclasses can subscribe for specific Nukkit {@link Event} classes.
 * <p>
 * For example, they can subscribe to events like {@link cn.nukkit.event.player.PlayerJoinEvent}, {@link cn.nukkit.event.player.PlayerMoveEvent}
 */
public abstract class EventSubscriber {

    /**
     * Registry for event handlers, where each {@link Event} type is associated with a specific handler
     */
    private Map<Class<? extends Event>, EventHandler<? extends Event>> handlers = new HashMap<>();

    /**
     * Plugin logger instance
     */
    private PluginLogger logger;

    protected EventSubscriber(PluginLogger logger) {
        this.logger = logger;
        this.handlers = getHandlersMap();

        // subscribe to registered event types
        handlers.forEach((eventType, handler) -> {
            EventPublisher.getInstance().subscribe(this);
        });
    }

    /**
     * List of registered handlers. Each subclass should define the list of events it wants to handle.
     *
     * @return map of event types each associated with the corresponding handler
     */
    protected abstract Map<Class<? extends Event>, EventHandler<? extends Event>> getHandlersMap();

    /**
     * List of events the subscriber is subscribed to.
     *
     * @return list of event types
     */
    public List<Class<? extends Event>> getEventTypes() {
        return new ArrayList<>(handlers.keySet());
    }

    /**
     * Process the given event.
     *
     * @param event event to handle
     */
    @SuppressWarnings("unchecked")
    public void handleEvent(Event event) {
        Optional<EventHandler<Event>> optionalHandler = Optional.ofNullable((EventHandler<Event>) handlers.get(event.getClass()));
        optionalHandler.ifPresentOrElse(
                handler -> handler.handle(event),
                () -> logger.warning("Cannot find the handler for event: " + event.getEventName())
        );
    }

}
