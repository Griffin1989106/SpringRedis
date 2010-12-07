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
package org.springframework.data.keyvalue.redis.core;


/**
 * Default {@link KeyBound} implementation.
 * Meant for internal usage.
 * 
 * @author Costin Leau
 */
class DefaultKeyBound<K> implements KeyBound<K> {

	private K key;

	public DefaultKeyBound(K key) {
		setKey(key);
	}

	@Override
	public K getKey() {
		return key;
	}

	protected void setKey(K key) {
		this.key = key;
	}
}
