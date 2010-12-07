/*
 * Copyright 2006-2009 the original author or authors.
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
package org.springframework.data.keyvalue.redis.support.collections;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;

/**
 * Redis extension for the {@link List} contract. Supports {@link List} and {@link Queue} specific
 * operations backed by Redis operations.
 * 
 * @author Costin Leau
 */
public interface RedisList<E> extends RedisCollection<E>, List<E>, BlockingDeque<E> {

	List<E> range(long start, long end);

	RedisList<E> trim(int start, int end);
}
