package io.leopard.web.xparam;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

//import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import io.leopard.json.Json;

/**
 * List<?>参数解析.
 * 
 * @author 阿海
 *
 */
@Component
public class ParamListHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

	private Set<String> simpleClassSet = new HashSet<String>();

	public ParamListHandlerMethodArgumentResolver() {
		simpleClassSet.add(String.class.getName());
		simpleClassSet.add(Long.class.getName());
		simpleClassSet.add(Float.class.getName());
		simpleClassSet.add(Integer.class.getName());
		simpleClassSet.add(Double.class.getName());
		simpleClassSet.add(Date.class.getName());
		simpleClassSet.add(MultipartFile.class.getName());
	}

	private Map<Integer, Class<?>> modelMap = new ConcurrentHashMap<Integer, Class<?>>();

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType();
		if (!type.equals(List.class)) {
			return false;
		}
		String name = parameter.getParameterName();
		boolean support = name.endsWith("List");

		if (!support) {
			return false;
		}

		{
			Type[] args = ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments();
			Class<?> clazz = (Class<?>) args[0];
			boolean isModel = isModelClass(clazz);
			if (isModel) {
				modelMap.put(parameter.hashCode(), clazz);
				System.err.println("name:" + name + " typeName:" + args[0] + " isModel:" + isModel);
			}
		}

		return support;
	}

	private boolean isModelClass(Class<?> clazz) {
		return !simpleClassSet.contains(clazz.getName());
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
		name = name.replaceFirst("List$", "");
		name = UnderlineHandlerMethodArgumentResolver.camelToUnderline(name);

		String[] values = req.getParameterValues(name);

		int hashCode = parameter.hashCode();
		Class<?> clazz = modelMap.get(hashCode);
		if (clazz != null) {
			return this.toList(clazz, values);
		}

		if (values != null && values.length == 1) {
			return StringUtils.split(values[0], ", ");// commons的StringUtils.split有问题?
		}
		return values;
	}

	@SuppressWarnings({ "rawtypes" })
	protected List toList(Class<?> clazz, String[] values) {
		System.out.println("toList clazz:" + clazz + " values:" + values);
		if (values == null || values.length == 0) {
			return null;
		}
		return Json.toListObject(values[0], clazz);
	}
}
