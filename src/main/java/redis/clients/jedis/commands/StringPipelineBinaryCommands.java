package redis.clients.jedis.commands;

import java.util.List;

import redis.clients.jedis.Response;
import redis.clients.jedis.params.GetExParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.StrAlgoLCSParams;
import redis.clients.jedis.params.LCSParams;
import redis.clients.jedis.resps.LCSMatchResult;

public interface StringPipelineBinaryCommands extends BitPipelineBinaryCommands {

  Response<String> set(byte[] key, byte[] value);

  Response<String> set(byte[] key, byte[] value, SetParams params);

  Response<byte[]> get(byte[] key);

  /**
   * WARNING: {@link SetParams#get()} MUST NOT be used with this method.
   */
  Response<byte[]> setGet(byte[] key, byte[] value, SetParams params);

  Response<byte[]> getDel(byte[] key);

  Response<byte[]> getEx(byte[] key, GetExParams params);

  Response<Long> setrange(byte[] key, long offset, byte[] value);

  Response<byte[]> getrange(byte[] key, long startOffset, long endOffset);

  Response<byte[]> getSet(byte[] key, byte[] value);

  Response<Long> setnx(byte[] key, byte[] value);

  Response<String> setex(byte[] key, long seconds, byte[] value);

  Response<String> psetex(byte[] key, long milliseconds, byte[] value);

  Response<List<byte[]>> mget(byte[]... keys);

  Response<String> mset(byte[]... keysvalues);

  Response<Long> msetnx(byte[]... keysvalues);

  Response<Long> incr(byte[] key);

  Response<Long> incrBy(byte[] key, long increment);

  Response<Double> incrByFloat(byte[] key, double increment);

  Response<Long> decr(byte[] key);

  Response<Long> decrBy(byte[] key, long decrement);

  Response<Long> append(byte[] key, byte[] value);

  Response<byte[]> substr(byte[] key, int start, int end);

  Response<Long> strlen(byte[] key);

  /**
   * @deprecated STRALGO LCS command will be removed from Redis 7.
   * {@link StringPipelineBinaryCommands#lcs(byte[], byte[], LCSParams) LCS} can be used instead of this method.
   */
  @Deprecated
  Response<LCSMatchResult> strAlgoLCSKeys(byte[] keyA, byte[] keyB, StrAlgoLCSParams params);

  Response<LCSMatchResult> lcs(byte[] keyA, byte[] keyB, LCSParams params);
}
