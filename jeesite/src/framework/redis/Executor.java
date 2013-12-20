package framework.redis;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public /**
 * 执行器，{@link com.futurefleet.framework.base.redis.RedisUtil}的辅助类，
 * 它保证在执行操作之后释放数据源returnResource(jedis)
 * @version V1.0
 * @author fengjc
 * @param <T>
 */
abstract class Executor<T> {

	ShardedJedis jedis;
	ShardedJedisPool shardedJedisPool;

	public Executor(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
		jedis = this.shardedJedisPool.getResource();
	}

	/**
	 * 回调
	 * @return 执行结果
	 */
	abstract T execute();

	/**
	 * 调用{@link #execute()}并返回执行结果
	 * 它保证在执行{@link #execute()}之后释放数据源returnResource(jedis)
	 * @return 执行结果
	 */
	public T getResult() {
		T result = null;
		try {
			result = execute();
		} catch (Throwable e) {
			throw new RuntimeException("Redis execute exception", e);
		} finally {
			if (jedis != null) {
				shardedJedisPool.returnResource(jedis);
			}
		}
		return result;
	}
}