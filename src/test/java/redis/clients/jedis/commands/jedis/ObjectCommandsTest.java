package redis.clients.jedis.commands.jedis;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.HostAndPorts;
import redis.clients.jedis.util.SafeEncoder;

public class ObjectCommandsTest extends JedisCommandsTestBase {

  private final String key = "mylist";
  private final byte[] binaryKey = SafeEncoder.encode(key);
  private final HostAndPort lfuHnp = HostAndPorts.getRedisServers().get(7);
  private Jedis lfuJedis;

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();

    lfuJedis = new Jedis(lfuHnp.getHost(), lfuHnp.getPort(), 500);
    lfuJedis.connect();
    lfuJedis.flushAll();
  }

  @After
  @Override
  public void tearDown() throws Exception {
    lfuJedis.disconnect();
    super.tearDown();
  }

  @Test
  public void objectRefcount() {
    jedis.lpush(key, "hello world");
    Long refcount = jedis.objectRefcount(key);
    assertEquals(Long.valueOf(1), refcount);

    // Binary
    refcount = jedis.objectRefcount(binaryKey);
    assertEquals(Long.valueOf(1), refcount);

  }

  @Test
  public void objectEncodingString() {
    jedis.set(key, "hello world");
    assertThat(jedis.objectEncoding(key), containsString("str"));

    // Binary
    assertThat(SafeEncoder.encode(jedis.objectEncoding(binaryKey)), containsString("str"));
  }

  @Test
  public void objectEncodingList() {
    jedis.lpush(key, "hello world");
    assertThat(jedis.objectEncoding(key), containsString("list"));

    // Binary
    assertThat(SafeEncoder.encode(jedis.objectEncoding(binaryKey)), containsString("list"));
  }

  @Test
  public void objectIdletime() throws InterruptedException {
    jedis.lpush(key, "hello world");

    Long time = jedis.objectIdletime(key);
    assertEquals(Long.valueOf(0), time);

    // Binary
    time = jedis.objectIdletime(binaryKey);
    assertEquals(Long.valueOf(0), time);
  }

  @Test
  public void objectHelp() {
    // String
    List<String> helpTexts = jedis.objectHelp();
    Assert.assertNotNull(helpTexts);

    // Binary
    List<byte[]> helpBinaryTexts = jedis.objectHelpBinary();
    Assert.assertNotNull(helpBinaryTexts);
  }

  @Test
  public void objectFreq() {
    lfuJedis.set(key, "test1");
    lfuJedis.get(key);
    // String
    assertThat(lfuJedis.objectFreq(key), Matchers.greaterThan(0L));
    // Binary
    assertThat(lfuJedis.objectFreq(binaryKey), Matchers.greaterThan(0L));

    Assert.assertNull(lfuJedis.objectFreq("no_such_key"));

    jedis.set(key, "test2");
    Assert.assertThrows("Freq is only allowed with LFU policy", JedisDataException.class, () -> jedis.objectFreq(key));
  }
}
