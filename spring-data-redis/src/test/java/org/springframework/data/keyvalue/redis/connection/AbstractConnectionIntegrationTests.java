/*
 * Copyright 2010 the original author or authors.
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

package org.springframework.data.keyvalue.redis.connection;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.keyvalue.redis.Person;

public abstract class AbstractConnectionIntegrationTests {

	protected RedisConnection connection;
	private static final String listName = "test-list";

	@Before
	public void setUp() {
		connection = getConnectionFactory().getConnection();
	}

	protected abstract RedisConnectionFactory getConnectionFactory();

	@After
	public void tearDown() {
		connection.close();
		connection = null;
	}

	@Test
	public void testLPush() throws Exception {
		Long index = connection.lPush(listName.getBytes(), "bar".getBytes());
		if (index != null) {
			assertEquals((Long) (index + 1), connection.lPush(listName.getBytes(), "bar".getBytes()));
		}
	}

	@Test
	public void testSetAndGet() {
		assumeTrue(!isJredis());
		connection.set("foo".getBytes(), "blahblah".getBytes());
		assertEquals("blahblah", new String(connection.get("foo".getBytes())));
	}


	private boolean isJredis() {
		return connection.getClass().getSimpleName().startsWith("Jredis");
	}

	public void conversions() {
		Person p = new Person("Joe", "Trader", 33);
	}
}