package live.lingting.sdk.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import live.lingting.sdk.enums.Currency;
import live.lingting.sdk.enums.Mode;
import live.lingting.sdk.enums.ThirdPart;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.exception.MixRequestParamsValidException;

/**
 * 查询
 *
 * @author lingting 2021/6/7 17:17
 */
@Getter
@Setter
public class MixRealPayModel extends MixModel {

	public static final BigDecimal MIN = new BigDecimal("0.01");

	private static final long serialVersionUID = 1L;

	/**
	 * 商品信息
	 */
	private String subject;

	private ThirdPart thirdPart;

	private Mode mode;

	private Currency currency;

	private BigDecimal amount;

	private String thirdPartTradeNo;

	@Override
	public void valid() throws MixException {
		validNotifyUrl();

		String msg = "";
		if (!StringUtils.hasText(getSubject())) {
			msg = "商品信息不能为空!";
		}
		else if (!Currency.REAL_LIST.contains(getCurrency())) {
			msg = "暂不支持该货币: " + (getCurrency() == null ? "" : getCurrency().name());
		}
		else if (!StringUtils.hasText(getProjectTradeNo())) {
			msg = "项目交易号不能为空";
		}
		else if (Mode.QR.equals(getMode())) {
			if (getAmount() == null || getAmount().compareTo(BigDecimal.ZERO) < 1) {
				msg = "支付金额异常";
			}
			else if (getAmount().compareTo(MIN) < 0) {
				msg = "支付金额最小值为" + MIN.toPlainString();
			}
			else if (!StringUtils.hasText(getSubject())) {
				msg = "商品信息不能为空!";
			}
		}
		else if (Mode.TRANSFER.equals(getMode())) {
			if (!StringUtils.hasText(thirdPartTradeNo)) {
				msg = "第三方交易号不能为空!";
			}
		}

		if (StringUtils.hasText(msg)) {
			throw new MixRequestParamsValidException(msg);
		}
	}

}
