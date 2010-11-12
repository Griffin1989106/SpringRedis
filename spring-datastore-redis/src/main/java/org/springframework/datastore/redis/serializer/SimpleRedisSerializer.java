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
package org.springframework.datastore.redis.serializer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.datastore.redis.UncategorizedRedisException;

/**
 * Simple Redis serializer delegating to the default serializer in Spring 3.
 * 
 * @author Mark Pollack
 * @author Costin Leau
 */
public class SimpleRedisSerializer implements RedisSerializer<Object> {

	private Converter<Object, byte[]> serializer = new SerializingConverter();
	private Converter<byte[], Object> deserializer = new DeserializingConverter();

	private sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	private sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

	@SuppressWarnings("unchecked")
	@Override
	public Object deserialize(byte[] bytes) {
		try {
			return deserializer.convert(bytes);
		} catch (Exception ex) {
			throw new UncategorizedRedisException("Cannot deserialize", ex);
		}
	}

	@Override
	public byte[] serialize(Object object) {
		try {
			return serializer.convert(object);
		} catch (Exception ex) {
			throw new UncategorizedRedisException("Cannot serialize", ex);
		}
	}
}