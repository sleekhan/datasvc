package org.ryan.services.datasvc.data.redis

import org.ryan.services.datasvc.model.User
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.{ReactiveRedisOperations, ReactiveRedisTemplate}
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder
import org.springframework.data.redis.serializer.{Jackson2JsonRedisSerializer, RedisSerializationContext, StringRedisSerializer}

@Configuration
class RedisConfiguration(factory: ReactiveRedisConnectionFactory) {

  @Bean
  def reactiveRedisTemplate: ReactiveRedisOperations[String, User] = {
    val keySerializer = new StringRedisSerializer
    val valueSerializer = new Jackson2JsonRedisSerializer[User](classOf[User])
    val builder = RedisSerializationContext.newSerializationContext(keySerializer).asInstanceOf[RedisSerializationContextBuilder[String, User]]
    val context = builder.value(valueSerializer).build
    new ReactiveRedisTemplate(factory, context)
  }

}
