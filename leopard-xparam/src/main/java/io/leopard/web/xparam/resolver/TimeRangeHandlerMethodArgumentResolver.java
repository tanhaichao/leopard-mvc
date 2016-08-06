package io.leopard.web.xparam.resolver;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import io.leopard.burrow.util.DateUtil;
import io.leopard.lang.TimeRange;

/**
 * 下划线参数名称解析.
 * 
 * @author 阿海
 *
 */
@Component
public class TimeRangeHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

	protected Log logger = LogFactory.getLog(this.getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType();
		return type.equals(TimeRange.class);
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
		boolean enable = UnderlineHandlerMethodArgumentResolver.isEnable();
		String startTimeName = "startTime";
		String endTimeName = "endTime";

		if (enable) {
			startTimeName = UnderlineHandlerMethodArgumentResolver.camelToUnderline(startTimeName);
			endTimeName = UnderlineHandlerMethodArgumentResolver.camelToUnderline(endTimeName);
		}

		String startTime = req.getParameter(startTimeName);
		String endTime = req.getParameter(endTimeName);

		TimeRange range = new TimeRange();
		if (StringUtils.isNotEmpty(startTime)) {
			range.setStartTime(toDate(startTime));
			range.setEndTime(toDate(endTime));
		}
		if (StringUtils.isNotEmpty(endTime)) {
			range.setEndTime(toDate(endTime));
		}
		return range;
	}

	private static String regex = "1[0-9]{12}";

	protected static Date toDate(String datetime) {
		if (datetime.matches(regex)) {
			long time = Long.parseLong(datetime);
			return new Date(time);
		}
		return DateUtil.toDate(datetime);
	}

}
