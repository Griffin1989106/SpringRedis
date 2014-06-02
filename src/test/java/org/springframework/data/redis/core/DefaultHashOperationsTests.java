/*
 * Copyright 2013-2014 the original author or authors.
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
package org.springframework.data.redis.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.data.redis.ObjectFactory;
import org.springframework.data.redis.RawObjectFactory;
import org.springframework.data.redis.SettingsUtils;
import org.springframework.data.redis.StringObjectFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.annotation.IfProfileValue;

/**
 * Integration test of {@link DefaultHashOperations}
 * 
 * @author Jennifer Hickey
 * @author Christoph Strobl
 * @param <K> Key type
 * @param <HK> Hash key type
 * @param <HV> Hash value type
 */
@RunWith(Parameterized.class)
public class DefaultHashOperationsTests<K, HK, HV> {
	private RedisTemplate<K, ?> redisTemplate;

	private ObjectFactory<K> keyFactory;

	private ObjectFactory<HK> hashKeyFactory;

	private ObjectFactory<HV> hashValueFactory;

	private HashOperations<K, HK, HV> hashOps;

	public DefaultHashOperationsTests(RedisTemplate<K, ?> redisTemplate, ObjectFactory<K> keyFactory,
			ObjectFactory<HK> hashKeyFactory, ObjectFactory<HV> hashValueFactory) {
		this.redisTemplate = redisTemplate;
		this.keyFactory = keyFactory;
		this.hashKeyFactory = hashKeyFactory;
		this.hashValueFactory = hashValueFactory;
	}

	@Parameters
	public static Collection<Object[]> testParams() {
		ObjectFactory<String> stringFactory = new StringObjectFactory();
		ObjectFactory<byte[]> rawFactory = new RawObjectFactory();

		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setPort(SettingsUtils.getPort());
		jedisConnectionFactory.setHostName(SettingsUtils.getHost());
		jedisConnectionFactory.afterPropertiesSet();

		RedisTemplate<String, String> stringTemplate = new StringRedisTemplate();
		stringTemplate.setConnectionFactory(jedisConnectionFactory);
		stringTemplate.afterPropertiesSet();

		RedisTemplate<byte[], byte[]> rawTemplate = new RedisTemplate<byte[], byte[]>();
		rawTemplate.setConnectionFactory(jedisConnectionFactory);
		rawTemplate.setEnableDefaultSerializer(false);
		rawTemplate.afterPropertiesSet();

		return Arrays.asList(new Object[][] { { stringTemplate, stringFactory, stringFactory, stringFactory },
				{ rawTemplate, rawFactory, rawFactory, rawFactory } });
	}

	@Before
	public void setUp() {
		hashOps = redisTemplate.opsForHash();
	}

	@After
	public void tearDown() {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) {
				connection.flushDb();
				return null;
			}
		});
	}

	@Test
	public void testEntries() {
		K key = keyFactory.instance();
		HK key1 = hashKeyFactory.instance();
		HV val1 = hashValueFactory.instance();
		HK key2 = hashKeyFactory.instance();
		HV val2 = hashValueFactory.instance();
		hashOps.put(key, key1, val1);
		hashOps.put(key, key2, val2);

		for (Map.Entry<HK, HV> entry : hashOps.entries(key).entrySet()) {
			assertThat(entry.getKey(), anyOf(equalTo(key1), equalTo(key2)));
			assertThat(entry.getValue(), anyOf(equalTo(val1), equalTo(val2)));
		}
	}

	@Test
	public void testDelete() {
		K key = keyFactory.instance();
		HK key1 = hashKeyFactory.instance();
		HV val1 = hashValueFactory.instance();
		HK key2 = hashKeyFactory.instance();
		HV val2 = hashValueFactory.instance();
		hashOps.put(key, key1, val1);
		hashOps.put(key, key2, val2);
		hashOps.delete(key, key1, key2);
		assertTrue(hashOps.keys(key).isEmpty());
	}

	/**
	 * @see DATAREDIS-305
	 */
	@Test
	@IfProfileValue(name = "redisVersion", value = "2.8+")
	public void testHScanReadsValuesFully() {

		K key = keyFactory.instance();
		HK key1 = hashKeyFactory.instance();
		HV val1 = hashValueFactory.instance();
		HK key2 = hashKeyFactory.instance();
		HV val2 = hashValueFactory.instance();
		hashOps.put(key, key1, val1);
		hashOps.put(key, key2, val2);

		Iterator<Map.Entry<HK, HV>> it = hashOps.scan(key, ScanOptions.scanOptions().count(1).build());

		long count = 0;
		while (it.hasNext()) {
			Map.Entry<HK, HV> entry = it.next();
			assertThat(entry.getKey(), anyOf(equalTo(key1), equalTo(key2)));
			assertThat(entry.getValue(), anyOf(equalTo(val1), equalTo(val2)));
			count++;
		}

		assertThat(count, is(hashOps.size(key)));
	}

}
