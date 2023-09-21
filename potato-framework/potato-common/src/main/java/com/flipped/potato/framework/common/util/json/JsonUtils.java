package com.flipped.potato.framework.common.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2022-04-11 19:35:16
 */
@Slf4j
public class JsonUtils {
    private static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // 初始化
    static {
        // 设置java.util.Date时间类的序列化以及反序列化的格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(STANDARD_PATTERN));

        // 初始化JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        //处理LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        //处理LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // 处理LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        // 注册时间模块, 支持支持jsr310, 即新的时间类(java.time包下的时间类)
        OBJECT_MAPPER.registerModule(javaTimeModule);

        // 包含所有字段（为NULL字段也返回属性名）
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 在序列化一个空对象时时不抛出异常
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // 忽略反序列化时在json字符串中存在, 但在java对象中不存在的属性
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    /**
     * JavaBean转换为JSON字符串
     *
     * @param bean JavaBean对象
     * @return JSON字符串
     */
    @SneakyThrows
    public static <T> String bean2Json(T bean) {
        if (bean == null) {
            return null;
        }

        return bean instanceof String ? (String) bean : getInstance().writeValueAsString(bean);
    }

    /**
     * JavaBean转换为JSON字符串并美化
     *
     * @param bean JavaBean对象
     * @return JSON字符串
     */
    @SneakyThrows
    public static <T> String bean2JsonPretty(T bean) {
        if (bean == null) {
            return null;
        }

        return bean instanceof String ? (String) bean : getInstance().writerWithDefaultPrettyPrinter().writeValueAsString(bean);
    }

    /**
     * JSON字符串转换为JavaBean
     *
     * @param json  json字符串
     * @param clazz JavaBean类型
     * @return 泛型对象
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {
        if (json == null || json.isEmpty() || "nil".equals(json) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) json : getInstance().readValue(json, clazz);
        } catch (IOException e) {
            log.error("JSON字符串转换为JavaBean异常，异常原因：", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * JSON字符串转换为JavaBean，支持转为集合类型
     * 对象示例: JsonUtil.json2Bean(json, new TypeReference<T>() {})
     * 集合示例: JsonUtil.json2Bean(json, new TypeReference<List<T>>() {})
     * T: 标识JavaBean类型
     *
     * @param json          json字符串
     * @param typeReference JavaBean类型
     * @return 泛型对象
     */
    public static <T> T json2Bean(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty() || "nil".equals(json) || typeReference == null) {
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ? (T) json : getInstance().readValue(json, typeReference);
        } catch (IOException e) {
            log.error("JSON字符串转换为JavaBean异常，异常原因：", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * JSON字符串转换为集合
     * 示例: JsonUtil.json2BeanCollection(json, Set.class, T.class)
     *
     * @param json            json字符串
     * @param collectionClazz 集合类型
     * @param elementClazz    内部引用类型
     * @return 泛型对象
     */
    public static <T> Collection<T> json2BeanCollection(String json, Class<?> collectionClazz, Class<?>... elementClazz) {
        JavaType javaType = getInstance().getTypeFactory().constructParametricType(collectionClazz, elementClazz);
        try {
            return getInstance().readValue(json, javaType);
        } catch (IOException e) {
            log.error("JSON字符串转换为List JavaBean异常，异常原因：", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * JSON字符串转换为集合
     * 示例: JsonUtil.json2Bean(json, T.class)
     *
     * @param json  json字符串
     * @param clazz JavaBean类型
     * @return 泛型对象
     */
    public static <T> List<T> json2BeanList(String json, Class<?> clazz) {
        try {
            return getInstance().readValue(json, getInstance().getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("JSON字符串转换为List JavaBean异常，异常原因：", e);
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 亮点：模拟构造方法设计模式提供类似于阿里巴巴FastJSON的put方式构造JSON功能
     *
     * @return JsonBuilder
     */
    public static JsonBuilder builder() {
        return new JsonBuilder();
    }


    public static class JsonBuilder {
        private final Map<String, Object> map = new HashMap<>();

        JsonBuilder() {
        }

        public JsonBuilder put(String key, Object value) {
            map.put(key, value);
            return this;
        }

        public String build() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(this.map);
            } catch (JsonProcessingException e) {
                e.fillInStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

}
