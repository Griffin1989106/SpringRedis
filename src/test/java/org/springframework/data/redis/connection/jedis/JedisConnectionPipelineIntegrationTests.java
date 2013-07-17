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
package org.springframework.data.redis.connection.jedis;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.connection.AbstractConnectionPipelineIntegrationTests;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test of {@link JedisConnection} pipeline functionality
 *
 * @author Jennifer Hickey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("JedisConnectionIntegrationTests-context.xml")
public class JedisConnectionPipelineIntegrationTests extends
		AbstractConnectionPipelineIntegrationTests {

	@After
	public void tearDown() {
		try {
			connection.flushDb();
			connection.close();
		} catch (Exception e) {
			// Jedis leaves some incomplete data in OutputStream on NPE caused
			// by null key/value tests
			// Attempting to close the connection will result in error on
			// sending QUIT to Redis
		}
		connection = null;
	}

	@Ignore("DATAREDIS-143 Pipeline tries to return String instead of List<String>")
	public void testGetConfig() {
	}

	@Ignore("DATAREDIS-143 MultiResponseBuilder trying to cast QUEUED to a List")
	public void testWatch() {
	}

	@Ignore("DATAREDIS-143  MultiResponseBuilder trying to cast QUEUED to a List")
	public void testUnwatch() {
	}

	@Ignore("DATAREDIS-143 Pipeline tries to return List<String> instead of Long on sort")
	public void testSortStore() {
	}

	@Ignore("DATAREDIS-143 Pipeline tries to return Long instead of List<String> on sort with no params")
	public void testSortNullParams() {
	}

	@Ignore("DATAREDIS-143 Pipeline tries to return Long instead of List<String> on sort with no params")
	public void testSortStoreNullParams() {
	}

	@Ignore("DATAREDIS-143 Jedis ClassCastExceptions closing pipeline on certain ops")
	public void testMultiExec() {
	}

	@Ignore("DATAREDIS-143 Jedis NPE closing pipeline on certain ops")
	public void testMultiDiscard() {
	}

	// Unsupported Ops
	@Test(expected=UnsupportedOperationException.class)
	public void testPExpire() {
		super.testPExpire();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPExpireKeyNotExists() {
		super.testPExpireKeyNotExists();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPExpireAt() {
		super.testPExpireAt();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPExpireAtKeyNotExists() {
		super.testPExpireAtKeyNotExists();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPTtl() {
		super.testPTtl();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPTtlNoExpire() {
		super.testPTtlNoExpire();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testDumpAndRestore() {
		super.testDumpAndRestore();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testDumpNonExistentKey() {
		super.testDumpNonExistentKey();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRestoreBadData() {
		super.testRestoreBadData();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRestoreExistingKey() {
		super.testRestoreExistingKey();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRestoreTtl() {
		super.testRestoreTtl();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testBitSet() throws Exception {
		super.testBitSet();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testBitGet() {
		connection.getBit("bitly", 1l);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitCount() {
		connection.bitCount("foo");
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitCountInterval() {
		super.testBitCountInterval();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitCountNonExistentKey() {
		super.testBitCountNonExistentKey();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitOpAnd() {
		super.testBitOpAnd();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitOpOr() {
		super.testBitOpOr();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitOpXOr() {
		super.testBitOpXOr();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitOpNot() {
		super.testBitOpNot();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testBitOpNotMultipleSources() {
		super.testBitOpNotMultipleSources();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRandomKey() {
		super.testRandomKey();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetRangeSetRange() {
		super.testGetRangeSetRange();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPingPong() throws Exception {
		super.testPingPong();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testInfo() throws Exception {
		super.testInfo();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScore() {
		super.testZRevRangeByScore();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScoreOffsetCount() {
		super.testZRevRangeByScoreOffsetCount();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScoreWithScores() {
		super.testZRevRangeByScoreWithScores();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScoreWithScoresOffsetCount() {
		super.testZRevRangeByScoreWithScoresOffsetCount();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSelect() {
		super.testSelect();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testHIncrByDouble() {
		super.testHIncrByDouble();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testIncrByDouble() {
		super.testIncrByDouble();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testScriptLoadEvalSha() {
		super.testScriptLoadEvalSha();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalShaArrayStrings() {
		super.testEvalShaArrayStrings();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalShaNotFound() {
		super.testEvalShaNotFound();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnString() {
		super.testEvalReturnString();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnNumber() {
		super.testEvalReturnNumber();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnSingleOK() {
		super.testEvalReturnSingleOK();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnSingleError() {
		super.testEvalReturnSingleError();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnFalse() {
		super.testEvalReturnFalse();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnTrue() {
		super.testEvalReturnTrue();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnArrayStrings() {
		super.testEvalReturnArrayStrings();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnArrayNumbers() {
		super.testEvalReturnArrayNumbers();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnArrayOKs() {
		super.testEvalReturnArrayOKs();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnArrayFalses() {
		super.testEvalReturnArrayFalses();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testEvalReturnArrayTrues() {
		super.testEvalReturnArrayTrues();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testScriptExists() {
		super.testScriptExists();
	}

	@IfProfileValue(name = "redisVersion", value = "2.6")
	@Test(expected=UnsupportedOperationException.class)
	public void testScriptKill() throws Exception{
		connection.scriptKill();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testScriptFlush() {
		connection.scriptFlush();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testSRandMemberCount() {
		super.testSRandMemberCount();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testSRandMemberCountKeyNotExists() {
		super.testSRandMemberCountKeyNotExists();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testSRandMemberCountNegative() {
		super.testSRandMemberCountNegative();
	}

	@Test(expected=UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6")
	public void testInfoBySection() throws Exception {
		super.testInfoBySection();
	}
}
