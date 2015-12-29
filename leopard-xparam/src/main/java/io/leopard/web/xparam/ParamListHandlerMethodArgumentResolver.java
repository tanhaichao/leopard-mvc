package io.leopard.web.xparam;

import io.leopard.json.Json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

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
			return support;
		}

		{
			Type[] args = ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments();
			Class<?> clazz = (Class<?>) args[0];
			boolean isModel = isModelClass(clazz);
			if (isModel) {
				modelMap.put(parameter.hashCode(), clazz);
			}
			System.out.println("name:" + name + " typeName:" + args[0] + " isModel:" + isModel);
		}

		// System.err.println("ParamListHandlerMethodArgumentResolver supportsParameter name:" + name + " support:" + support + " type:" + type.getName());
		return support;
	}

	private boolean isModelClass(Class<?> clazz) {
		return !simpleClassSet.contains(clazz.getName());
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
		name = name.replaceFirst("List$", "");
		// System.out.println("names:" + StringUtils.collectionToDelimitedString(req.getParameterNames(), ","));

		// {
		// Enumeration<String> e = req.getParameterNames();
		// while (e.hasMoreElements()) {
		// String name2 = e.nextElement();
		// // System.out.println("resolveName name2:" + name2);
		// }
		// }
		String[] values = req.getParameterValues(name);
		{
			int hashCode = parameter.hashCode();
			Class<?> clazz = modelMap.get(hashCode);
			// System.out.println("resolveName name:" + name + " clazz:" + clazz);
			if (clazz != null) {
				return this.toList(clazz, values);
			}
		}
		// String value = req.getParameter(name);
		return values;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List toList(Class<?> clazz, String[] values) {
		// System.out.println("toList clazz:" + clazz + " values:" + values);
		List list = new ArrayList();
		if (values != null) {
			for (String json : values) {
				// System.out.println("toList json:" + json);
				Object obj = Json.toObject(json, clazz);
				list.add(obj);
			}
		}
		return list;
	}
}
