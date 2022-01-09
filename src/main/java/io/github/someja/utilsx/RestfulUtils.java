package io.github.someja.utilsx;

import org.apache.hc.client5.http.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestfulUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulUtils.class);

    /**
     * @param url
     * @param params 参数，会添加到url后面
     * @return
     */
    public static String get(String url, Map<String, String> params) {
        try {
            return Request.get(url).execute().returnContent().asString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("request encounter error!", e);
            return null;
        }
    }

}
