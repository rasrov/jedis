package redis.clients.jedis;

import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.providers.ShardedConnectionProvider;
import redis.clients.jedis.util.Hashing;

public class JedisSharding extends UnifiedJedis {

  public static final Pattern DEFAULT_KEY_TAG_PATTERN = Pattern.compile("\\{(.+?)\\}");

  public JedisSharding(List<HostAndPort> shards) {
    this(new ShardedConnectionProvider(shards));
  }

  public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig) {
    this(new ShardedConnectionProvider(shards, clientConfig));
  }

  public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig,
      GenericObjectPoolConfig<Connection> poolConfig) {
    this(new ShardedConnectionProvider(shards, clientConfig, poolConfig));
  }

  public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig, Hashing algo) {
    this(new ShardedConnectionProvider(shards, clientConfig, algo));
  }

  public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig,
      GenericObjectPoolConfig<Connection> poolConfig, Hashing algo) {
    this(new ShardedConnectionProvider(shards, clientConfig, poolConfig, algo));
  }

  public JedisSharding(ShardedConnectionProvider provider) {
    super(provider);
  }

  public JedisSharding(ShardedConnectionProvider provider, Pattern tagPattern) {
    super(provider, tagPattern);
  }

  @Override
  public ShardedPipeline pipelined() {
    return new ShardedPipeline((ShardedConnectionProvider) provider);
  }

  /**
   * @return nothing
   * @throws UnsupportedOperationException
   */
  @Override
  public Transaction multi() {
    throw new UnsupportedOperationException();
  }
}
