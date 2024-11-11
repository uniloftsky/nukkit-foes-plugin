package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;
import cn.nukkit.plugin.PluginLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private final Map<Class<? extends Event>, EventHandler<? extends Event>> handlers = new HashMap<>();

    private final PluginLogger logger;

    protected EventSubscriber(PluginLogger logger) {
        this.logger = logger;
    }

    /**
     * Register an event handler for a specific event type
     *
     * @param eventType class of event type to handle
     * @param handler   event handler for the specific event type
     * @param <T>       type of event
     */
    protected <T extends Event> void registerHandler(Class<T> eventType, EventHandler<T> handler) {
        EventPublisher.subscribe(this, eventType);
        handlers.put(eventType, handler);
    }

    /**
     * Process the given event using its corresponding handler.
     * <p>
     * The handler for an event must be registered before the event can be handled.
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
