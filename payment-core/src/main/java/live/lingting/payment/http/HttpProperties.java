package live.lingting.payment.http;

import lombok.Getter;
import lombok.Setter;

/**
 * http 配置
 *
 * @author lingting 2021/6/7 20:42
 */
@Getter
@Setter
public class HttpProperties {

	public static final Long UNLIMITED_TIMEOUT = -1L;

	/**
	 * 连接超时, 单位: 毫秒
	 */
	private Long connectTimeout = 10000L;

	/**
	 * 读取超时, 单位: 毫秒
	 */
	private Long readTimeout = 30000L;

	public static HttpProperties unlimited() {
		HttpProperties properties = new HttpProperties();
		properties.setConnectTimeout(UNLIMITED_TIMEOUT);
		properties.setReadTimeout(UNLIMITED_TIMEOUT);
		return properties;
	}

}