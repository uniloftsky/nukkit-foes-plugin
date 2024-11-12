package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class EventSubscriberTest {

    @Spy
    private Map<Class<? extends Event>, EventHandler<? extends Event>> handlers = new HashMap<>();

    @Mock
    private PluginLogger logger;

    @InjectMocks
    private EventSubscriber testSubscriber = new TestSubscriber(logger);

    @Test
    @SuppressWarnings("unchecked")
    public void testSubscribeOnEvent() {

        // given
        Class<PlayerJoinEvent> eventType = PlayerJoinEvent.class;
        EventHandler<PlayerJoinEvent> handler = mock(EventHandler.class);

        EventPublisher publisher = mock(EventPublisher.class);
        try (MockedStatic<EventPublisher> mockedStaticPublisher = mockStatic(EventPublisher.class)) {
            mockedStaticPublisher.when(EventPublisher::getInstance).thenReturn(publisher);

            // when
            testSubscriber.subscribeOnEvent(eventType, handler);

            // then
            then(publisher).should().subscribe(testSubscriber, eventType);

            assertFalse(handlers.isEmpty());
            EventHandler<? extends Event> actualHandler = handlers.get(eventType);
            assertEquals(handler, actualHandler);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHandleEvent() {

        // given
        Class<? extends Event> eventType = TestEvent.class;
        Event event = new TestEvent();
        EventHandler<Event> handler = mock(EventHandler.class);
        handlers.put(eventType, handler);

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
    }

    private static class TestEvent extends Event { }

}
