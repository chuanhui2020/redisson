/**
 * Copyright (c) 2013-2024 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.api;

import org.redisson.api.fanout.MessageFilter;
import org.redisson.api.fanout.FanoutPublishArgs;

import java.util.List;

/**
 * Reliable fanout implementation that ensures message delivery to subscribed queues.
 *
 * @param <V> The type of message payload
 *
 * @author Nikita Koksharov
 *
 */
public interface RReliableFanoutAsync<V> extends RExpirableAsync {

    /**
     * Publishes a message to all subscribed queues based on the provided arguments.
     *
     * @param args arguments defining the message and publishing parameters
     * @return The published message, or null if the message hasn't been added to all
     *         subscribed queues. The message may not be added to a subscribed queue if
     *         the queue has size limit and is full, if message size exceeds defined queue message size limit
     *         or message rejected due to deduplication.
     */
    RFuture<Message<V>> publishAsync(FanoutPublishArgs<V> args);

    /**
     * Publishes multiple messages to all subscribed queues based on the provided arguments.
     *
     * @param args arguments defining the messages and publishing parameters
     * @return A list containing only messages that were added to at least a single
     *         subscribed queue. Messages may not be added to a subscribed queue if
     *         the queue has size limit and is full, if message size exceeds defined queue message size limit
     *         or message rejected due to deduplication.
     */
    RFuture<List<Message<V>>> publishManyAsync(FanoutPublishArgs<V> args);

    /**
     * Removes a filter for the specified queue name .
     *
     * @param name the queue name
     */
    RFuture<Void> removeFilterAsync(String name);

    /**
     * Sets a filter that is applied to all messages published to the queue through
     * this fanout.
     * <p>
     * The FanoutFilter object is replicated among all ReliableFanout objects
     * and applied on each of them during message publishing.
     *
     * @param name the queue name
     * @param filter applied to messages
     */
    RFuture<Void> setFilterAsync(String name, MessageFilter<V> filter);

    /**
     * Checks if a queue with the specified name is subscribed to this fanout.
     *
     * @param name the queue name
     * @return <code>true</code> if the queue is subscribed, <code>false</code> otherwise
     */
    RFuture<Boolean> isSubscribedAsync(String name);

    /**
     * Subscribes a queue with the specified name to this fanout.
     *
     * @param name the queue name
     * @return <code>true</code> if the queue was subscribed,
     *          <code>false</code> if queue is already subscribed
     */
    RFuture<Boolean> subscribeQueueAsync(String name);

    /**
     * Subscribes a queue with the specified name to this fanout with a filter.
     *
     * @param name the queue name
     * @param filter the filter that is applied to all messages published through this fanout
     * @return <code>true</code> if the queue was subscribed,
     *          <code>false</code> if queue is already subscribed
     */
    RFuture<Boolean> subscribeQueueAsync(String name, MessageFilter<V> filter);

    /**
     * Unsubscribes a queue with the specified name from this fanout.
     *
     * @param name the queue name
     * @return <code>true</code> if the queue was unsubscribed,
     *          <code>false</code> if the queue isn't subscribed
     */
    RFuture<Boolean> unsubscribeAsync(String name);

    /**
     * Returns a list of the names of all subscribers to this fanout.
     *
     * @return subscriber names
     */
    RFuture<List<String>> getSubscribersAsync();

    /**
     * Returns amount of subscribers to this fanout.
     *
     * @return amount of subscribers
     */
    RFuture<Integer> countSubscribersAsync();

}
