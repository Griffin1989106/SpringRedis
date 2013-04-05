/*
 * Copyright 2011-2013 the original author or authors.
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
package org.springframework.data.redis.connection.srp;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.redis.RedisSystemException;

import redis.client.RedisClientBase;

/**
 * Integration test of {@link SrpConnection} functionality within a transaction
 *
 * @author Jennifer Hickey
 *
 */
public class SrpConnectionTransactionIntegrationTests extends
		SrpConnectionPipelineIntegrationTests {

	@Ignore
	public void testMultiDiscard() {
	}

	@Ignore
	public void testMultiExec() {
	}

	@Ignore
	public void testUnwatch() {
	}

	@Ignore
	public void testWatch() {
	}

	/*
	 * Using blocking ops inside a tx does not make a lot of sense as it would
	 * require blocking the entire server in order to execute the block
	 * atomically, which in turn does not allow other clients to perform a push
	 * operation.
	 */

	@Ignore
	public void testBLPop() {
	}

	@Ignore
	public void testBRPop() {
	}

	@Ignore
	public void testBRPopLPush() {
	}

	@Ignore
	public void testBLPopTimeout() {
	}

	@Ignore
	public void testBRPopTimeout() {
	}

	@Ignore
	public void testBRPopLPushTimeout() {
	}

	@Test(expected = RedisSystemException.class)
	public void exceptionExecuteNative() throws Exception {
		connection.execute("ZadD", getClass() + "#foo\t0.90\titem");
		getResults();
	}

	protected void initConnection() {
		connection.multi();
	}

	protected List<Object> getResults() {
		return connection.exec();
	}

	protected int getRedisVersion() {
		connection.exec();
		int version = RedisClientBase.parseVersion((String) connection.info()
				.get("redis_version"));
		connection.multi();
		return version;
	}
}
