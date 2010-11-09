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
package org.springframework.datastore.redis.util;

import java.util.List;
import java.util.SortedSet;

/**
 * Redis extension for the {@link SortedSet} contract. Supports {@link SortedSet} specific
 * operations backed by Redis commands.
 * 
 * @author Costin Leau
 */
public interface RedisSortedSet extends RedisCollection, SortedSet<String> {

	RedisSortedSet intersectAndStore(String destKey, RedisSet... sets);

	RedisSortedSet unionAndStore(String destKey, RedisSet... sets);

	List<String> range(int start, int end);

	List<String> rangeByScore(int start, int end);

	RedisSortedSet trim(int start, int end);

	RedisSortedSet trimByScore(int start, int end);
}
