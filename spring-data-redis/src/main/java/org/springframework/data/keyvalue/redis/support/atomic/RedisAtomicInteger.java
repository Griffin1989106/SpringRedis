/*
 * Copyright 2010-2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.keyvalue.redis.support.atomic;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.Callable;

import org.springframework.data.keyvalue.redis.connection.RedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.KeyBound;
import org.springframework.data.keyvalue.redis.core.RedisOperations;
import org.springframework.data.keyvalue.redis.core.RedisTemplate;
import org.springframework.data.keyvalue.redis.core.SessionCallback;
import org.springframework.data.keyvalue.redis.core.ValueOperations;

/**
 * Atomic integer backed by Redis.
 * Uses Redis atomic increment/decrement and watch/multi/exec operations for CAS operations. 
 * 
 * @see java.util.concurrent.atomic.AtomicInteger
 * @author Costin Leau
 */
public class RedisAtomicInteger extends Number implements Serializable, KeyBound<String> {

	private final String key;
	private ValueOperations<String, Integer> operations;
	private RedisOperations<String, Integer> generalOps;


	/**
	 * Constructs a new <code>RedisAtomicInteger</code> instance.
	 *
	 * @param redisCounter redis counter
	 * @param factory connection factory
	 */
	public RedisAtomicInteger(String redisCounter, RedisConnectionFactory factory) {
		RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<String, Integer>(factory);
		redisTemplate.setExposeConnection(true);
		this.key = redisCounter;
		this.generalOps = redisTemplate;
		this.operations = generalOps.opsForValue();
		if (this.operations.get(redisCounter) == null) {
			set(0);
		}
	}

	/**
	 * Constructs a new <code>RedisAtomicInteger</code> instance.
	 *
	 * @param redisCounter
	 * @param factory
	 * @param initialValue
	 */
	public RedisAtomicInteger(String redisCounter, RedisConnectionFactory factory, int initialValue) {
		RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<String, Integer>(factory);
		redisTemplate.setExposeConnection(true);
		this.key = redisCounter;
		this.generalOps = redisTemplate;
		this.operations = generalOps.opsForValue();
		this.operations.set(redisCounter, initialValue);
	}

	/**
	 * Constructs a new <code>RedisAtomicInteger</code> instance. Uses as initial value
	 * the data from the backing store (sets the counter to 0 if no value is found).
	 * 
	 * Use {@link #RedisAtomicInteger(String, RedisOperations, int)} to set the counter to a certain value
	 * as an alternative constructor or {@link #set(int)}.
	 *
	 * @param redisCounter
	 * @param operations
	 */
	public RedisAtomicInteger(String redisCounter, RedisOperations<String, Integer> operations) {
		this.key = redisCounter;
		this.operations = operations.opsForValue();
		this.generalOps = operations;
		if (this.operations.get(redisCounter) == null) {
			set(0);
		}
	}

	/**
	 * Constructs a new <code>RedisAtomicInteger</code> instance with the given initial value.
	 *
	 * @param redisCounter
	 * @param operations
	 * @param initialValue
	 */
	public RedisAtomicInteger(String redisCounter, RedisOperations<String, Integer> operations, int initialValue) {
		this.key = redisCounter;
		this.operations = operations.opsForValue();
		this.generalOps = operations;
		this.operations.set(redisCounter, initialValue);
	}

	@Override
	public String getKey() {
		return key;
	}

	/**
	 * Get the current value.
	 *
	 * @return the current value
	 */
	public int get() {
		return Integer.valueOf(operations.get(key));
	}

	/**
	 * Set to the given value.
	 *
	 * @param newValue the new value
	 */
	public void set(int newValue) {
		operations.set(key, newValue);
	}

	/**
	 * Set to the give value and return the old value.
	 *
	 * @param newValue the new value
	 * @return the previous value
	 */
	public int getAndSet(int newValue) {
		return operations.getAndSet(key, newValue);
	}

	/**
	 * Atomically set the value to the given updated value
	 * if the current value <tt>==</tt> the expected value.
	 * @param expect the expected value
	 * @param update the new value
	 * @return true if successful. False return indicates that
	 * the actual value was not equal to the expected value.
	 */
	public boolean compareAndSet(final int expect, final int update) {
		return generalOps.execute(new SessionCallback<Boolean>() {

			@Override
			public Boolean execute(RedisOperations operations) {
				for (;;) {
					operations.watch(Collections.singleton(key));
					if (expect == get()) {
						generalOps.multi();
						set(update);
						if (operations.exec() != null) {
							return true;
						}
					}
					{
						return false;
					}
				}
			}
		});
	}

	/**
	 * Atomically increment by one the current value.
	 * @return the previous value
	 */
	public int getAndIncrement() {
		return CASUtils.execute(generalOps, key, new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int value = get();
				generalOps.multi();
				operations.increment(key, 1);
				return value;
			}
		});
	}


	/**
	 * Atomically decrement by one the current value.
	 * @return the previous value
	 */
	public int getAndDecrement() {
		return CASUtils.execute(generalOps, key, new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int value = get();
				generalOps.multi();
				operations.increment(key, -1);
				return value;
			}
		});
	}


	/**
	 * Atomically add the given value to current value.
	 * @param delta the value to add
	 * @return the previous value
	 */
	public int getAndAdd(final int delta) {
		return CASUtils.execute(generalOps, key, new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int value = get();
				generalOps.multi();
				set(value + delta);
				return value;
			}
		});
	}

	/**
	 * Atomically increment by one the current value.
	 * @return the updated value
	 */
	public int incrementAndGet() {
		return operations.increment(key, 1).intValue();
	}

	/**
	 * Atomically decrement by one the current value.
	 * @return the updated value
	 */
	public int decrementAndGet() {
		return operations.increment(key, -1).intValue();
	}


	/**
	 * Atomically add the given value to current value.
	 * @param delta the value to add
	 * @return the updated value
	 */
	public int addAndGet(int delta) {
		return operations.increment(key, delta).intValue();
	}

	/**
	 * Returns the String representation of the current value.
	 * @return the String representation of the current value.
	 */
	public String toString() {
		return Integer.toString(get());
	}


	public int intValue() {
		return get();
	}

	public long longValue() {
		return (long) get();
	}

	public float floatValue() {
		return (float) get();
	}

	public double doubleValue() {
		return (double) get();
	}
}