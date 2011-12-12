package com.commsen.guicelet.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.ClassConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.URLConverter;

import com.commsen.guicelet.RequestParameter;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class RequestBeanProvider implements Provider<RequestBean> {

	@Inject
	Injector injector;

	static {
		ConvertUtils.register(new BigDecimalConverter(), BigDecimal.class);
		ConvertUtils.register(new BigIntegerConverter(), BigInteger.class);
		ConvertUtils.register(new BooleanConverter(), Boolean.class);
		ConvertUtils.register(new BooleanConverter(), Boolean.TYPE);
		ConvertUtils.register(new ByteConverter(), Byte.class);
		ConvertUtils.register(new ByteConverter(), Byte.TYPE);
		ConvertUtils.register(new CharacterConverter(), Character.class);
		ConvertUtils.register(new CharacterConverter(), Character.TYPE);
		ConvertUtils.register(new ClassConverter(), Class.class);
		ConvertUtils.register(new DoubleConverter(), Double.class);
		ConvertUtils.register(new DoubleConverter(), Double.TYPE);
		ConvertUtils.register(new FloatConverter(), Float.class);
		ConvertUtils.register(new FloatConverter(), Float.TYPE);
		ConvertUtils.register(new IntegerConverter(), Integer.class);
		ConvertUtils.register(new IntegerConverter(), Integer.TYPE);
		ConvertUtils.register(new LongConverter(), Long.class);
		ConvertUtils.register(new LongConverter(), Long.TYPE);
		ConvertUtils.register(new ShortConverter(), Short.class);
		ConvertUtils.register(new ShortConverter(), Short.TYPE);
		ConvertUtils.register(new URLConverter(), URL.class);
	}

	public <T extends RequestBean> T get(Class<T> clazz) {
		T t = injector.getInstance(clazz);
		List<Field> fields = Arrays.asList(t.getClass().getDeclaredFields());
		for (Field field : fields) {
			RequestParameter requestParameterAnnotation = field.getAnnotation(RequestParameter.class);
			if (requestParameterAnnotation == null) {
				continue;
			}
			String parameterName = requestParameterAnnotation.name();
			String parameterValue = t.request.getParameter(parameterName);
 
			if (parameterValue == null) {
				if (requestParameterAnnotation.required()) {
					t.addError(field.getName(), RequestBean.MISSING_REQUIRED_FIELD);
				}
				continue;
			}
			
			field.setAccessible(true);

			@SuppressWarnings("rawtypes")
			Class c = field.getType();

			try {
				if (String.class.equals(c)) {
					field.set(t, parameterValue);
				} else {
					field.set(t, ConvertUtils.convert(parameterValue, c));
				}
			} catch (Exception e) {
				t.addError(field.getName(), RequestBean.ERROR_CONVERTING_VALUE);
			}
		}

		return t;
	}

	public RequestBean get() {
		return injector.getInstance(RequestBean.class);
	}
}
