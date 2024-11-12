package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;
import cn.nukkit.event.player.PlayerJoinEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventPublisherTest {

    @Mock
    private ExecutorService executorService;

    @Spy
    private Map<Class<? extends Event>, List<EventSubscriber>> subscribers = new HashMap<>();

    @InjectMocks
    private EventPublisher testPublisher = new EventPublisher(executorService);

    @Test
    public void testSubscribe() {

        // given
        EventSubscriber subscriber = mock(EventSubscriber.class);
        Class<? extends Event> eventType = PlayerJoinEvent.class;

        // when
        testPublisher.subscribe(subscriber, eventType);

        // then
        int subscribersSize = subscribers.size();
        assertTrue(subscribersSize > 0);
        List<EventSubscriber> actualSubscribers = subscribers.get(eventType);
        assertNotNull(actualSubscribers);
        assertTrue(actualSubscribers.contains(subscriber));

        // test that the same subscriber can be registered only once
        testPublisher.subscribe(subscriber, eventType);
        assertEquals(subscribersSize, subscribers.size());
        assertTrue(actualSubscribers.contains(subscriber));
    }

    @Test
    public void testPushEvent() {

        // given
        PlayerJoinEvent event = mock(PlayerJoinEvent.class);
        EventSubscriber subscriber = mock(EventSubscriber.class);
        Class<PlayerJoinEvent> eventType = PlayerJoinEvent.class;
        testPublisher.subscribe(subscriber, eventType);

        // when
        testPublisher.pushEvent(event);

        // then
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(executorService).submit(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(subscriber).handleEvent(event);
    }

    @Test
    public void testShutdown() {

        // given
        given(executorService.isShutdown()).willReturn(false);

        // when
        testPublisher.shutdown();

        // then
        then(executorService).should().shutdown();
    }

    @Test
    public void testShutdownAlreadyStopped() {

        // given
        given(executorService.isShutdown()).willReturn(true);

        // when
        testPublisher.shutdown();

        // then
        then(executorService).should(times(0)).shutdown();
    }

}
