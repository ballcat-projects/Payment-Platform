package live.lingting.sdk;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import live.lingting.sdk.enums.Chain;
import live.lingting.sdk.enums.SdkContract;
import live.lingting.sdk.response.MixVirtualPayResponse;
import live.lingting.sdk.response.MixVirtualSubmitResponse;

/**
 * @author lingting 2021/6/7 23:08
 */
@Slf4j
class MixPayTest {

	private static MixPay mixPay;

	private static final Snowflake SNOWFLAKE = IdUtil.createSnowflake(1, 1);

	private MixVirtualPayResponse mixVirtualPayResponse;

	private MixVirtualSubmitResponse mixVirtualSubmitResponse;

	@BeforeAll
	public static void init() {
		mixPay = new MixPay("http://127.0.0.1:23302", "h8u45pyloe8zmefy", "f528dc13cc87416e9734716221f67244",
				"https://www.baidu.com");
	}

	@SneakyThrows
	@Test
	void virtualPay() {
		mixVirtualPayResponse = mixPay.virtualPay(SNOWFLAKE.nextIdStr(), SdkContract.USDT, Chain.OMNI);
		log.info(mixVirtualPayResponse.toString());
	}

	@SneakyThrows
	@Test
	void virtualSubmit() {
		virtualPay();
		String hash = RandomUtil.randomString(64);
		if (mixVirtualPayResponse.isSuccess()) {
			mixVirtualSubmitResponse = mixPay.virtualSubmit(mixVirtualPayResponse.getTradeNo(), "", hash);
			log.info(mixVirtualSubmitResponse.toString());
		}
	}

}
