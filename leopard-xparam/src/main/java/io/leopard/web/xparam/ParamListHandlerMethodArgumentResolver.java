package io.leopard.web.xparam;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
//import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import io.leopard.json.Json;
import io.leopard.json.JsonException;

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

	@Value("${xparam.underline}")
	private String underline;
	private static ObjectMapper mapper; // can reuse, share

	@PostConstruct
	public void init() {
		boolean enable = !"false".equals(underline);
		mapper = new ObjectMapper();
		if (enable) {
			mapper = mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		}
	}

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
			// StringUtils.split("", "");
			// return StringUtils.split(values[0], ", ");// commons的StringUtils.split有问题，需要使用spring的StringUtils
			// 20160610 spring和commons的StringUtils.split都有问题
			if (StringUtils.isEmpty(values[0])) {
				return null;
			}
			// TODO 暂时删除
			// return values[0].split(", ");
		}
		return values;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List toList(Class<?> clazz, String[] values) {
		// System.out.println("toList clazz:" + clazz + " values:" + values);
		if (values == null || values.length == 0) {
			return null;
		}

		// boolean underline = UnderlineHandlerMethodArgumentResolver.isEnable();
		if (values.length == 1) {
			// TODO 这里是否要判断传递的是不是数据组?
			return toListObject(values[0], clazz);
		}
		List list = new ArrayList();
		for (String value : values) {
			Object bean = Json.toObject(value, clazz);
			list.add(bean);
		}
		return list;
	}

	public static <T> T toObject(String json, Class<T> clazz, boolean ignoreUnknownField) {
		if (json == null || json.length() == 0) {
			return null;
		}

		try {
			return mapper.readValue(json, clazz);
		}
		catch (Exception e) {
			throw new JsonException(e.getMessage(), e);
		}
	}

	public static <T> List<T> toListObject(String json, Class<T> clazz) {
		if (json == null || json.length() == 0) {
			return null;
		}

		JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, clazz);
		try {
			return mapper.readValue(json, javaType);
		}
		catch (Exception e) {
			System.out.println("toListObjectjson:" + json);
			System.out.println("toListObject clazz:" + clazz.getName());
			throw new JsonException(e.getMessage(), e);
		}
	}
}
