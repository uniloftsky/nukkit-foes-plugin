package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class EventSubscriberTest {

    private static final Map<Class<? extends Event>, EventHandler<? extends Event>> EVENT_HANDLERS = new HashMap<>();

    @Mock
    private PluginLogger logger;

    @InjectMocks
    private EventSubscriber testSubscriber = new TestSubscriber(logger);

    @BeforeEach
    void setUp() {
        EVENT_HANDLERS.clear();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetHandlersMap() {

        // given
        Class<? extends Event> eventType = PlayerJoinEvent.class;
        EventHandler<? extends Event> handler = mock(EventHandler.class);
        EVENT_HANDLERS.put(eventType, handler);

        // when
        Map<Class<? extends Event>, EventHandler<? extends Event>> result = testSubscriber.getHandlersMap();

        // then
        assertNotNull(result);
        EventHandler<? extends Event> actualHandler = result.get(eventType);
        assertEquals(handler, actualHandler);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetEventTypes() {

        // given
        Class<? extends Event> eventType = PlayerJoinEvent.class;
        EventHandler<? extends Event> handler = mock(EventHandler.class);
        EVENT_HANDLERS.put(eventType, handler);

        // when
        List<Class<? extends Event>> result = testSubscriber.getEventTypes();

        // then
        assertNotNull(result);
        assertEquals(eventType, result.getFirst());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHandleEvent() {

        // given
        Class<? extends Event> eventType = TestEvent.class;
        Event event = new TestEvent();
        EventHandler<Event> handler = mock(EventHandler.class);
        EVENT_HANDLERS.put(eventType, handler);

        // when
        testSubscriber.handleEvent(event);

        // then
        then(handler).should().handle(event);
    }

    @Test
    public void testHandleEventNoHandler() {

        // given
        Event event = new TestEvent();

        // when
        testSubscriber.handleEvent(event);

        // then
        then(logger).should().warning(anyString());
    }

    private static class TestSubscriber extends EventSubscriber {

        public TestSubscriber(PluginLogger logger) {
            super(logger);
        }

        @Override
        protected Map<Class<? extends Event>, EventHandler<? extends Event>> getHandlersMap() {
            return EVENT_HANDLERS;
        }
    }

    private static class TestEvent extends Event {
    }

}
