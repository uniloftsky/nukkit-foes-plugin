package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;
import cn.nukkit.event.player.PlayerJoinEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class EventPublisherTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testSubscribe() throws NoSuchFieldException, IllegalAccessException {

        // given
        EventSubscriber subscriber = mock(EventSubscriber.class);
        Class<? extends Event> testEvent = PlayerJoinEvent.class;

        // when
        EventPublisher.subscribe(subscriber, testEvent);

        // then
        EventPublisher instance = getPublisherInstance();

        // test publisher subscribers
        Field subscribersField = EventPublisher.class.getDeclaredField("subscribers");
        subscribersField.setAccessible(true);
        Map<Class<? extends Event>, List<EventSubscriber>> subscribers = (Map<Class<? extends Event>, List<EventSubscriber>>) subscribersField.get(instance);

        assertFalse(subscribers.isEmpty());
        List<EventSubscriber> subscribersByType = subscribers.get(testEvent);
        assertNotNull(subscribersByType);
        assertTrue(subscribersByType.contains(subscriber));
    }

    @Test
    public void testPushEvent() {

        // given
        Class<? extends Event> eventClass = PlayerJoinEvent.class;
        Event event = mock(eventClass);
        EventSubscriber subscriber = mock(EventSubscriber.class);
        EventPublisher.subscribe(subscriber, eventClass);

        // when
        EventPublisher.pushEvent(event);

        // then
        then(subscriber).should().handleEvent(event);
    }

    @Test
    public void testShutdown() throws NoSuchFieldException, IllegalAccessException {

        // given
        // get executor
        Field executorField = EventPublisher.class.getDeclaredField("executor");
        executorField.setAccessible(true);
        ExecutorService executor = (ExecutorService) executorField.get(null);

        // when
        EventPublisher.shutdown();

        // then
        assertTrue(executor.isShutdown());
    }

    private EventPublisher getPublisherInstance() throws NoSuchFieldException, IllegalAccessException {
        Field instanceField = EventPublisher.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);

        // get the singleton publisher instance from the publisher instance field
        return (EventPublisher) instanceField.get(null);
    }

}
