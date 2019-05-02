package name.dengchao.fx.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

@Slf4j
public class QiniuAuth {

    /**
     * 上传策略 参考文档：<a href="https://developer.qiniu.com/kodo/manual/put-policy">上传策略</a>
     */
    private static final String[] policyFields = new String[]{
        "callbackUrl",
        "callbackBody",
        "callbackHost",
        "callbackBodyType",
        "callbackFetchKey",

        "returnUrl",
        "returnBody",

        "endUser",
        "saveKey",
        "insertOnly",

        "detectMime",
        "mimeLimit",
        "fsizeLimit",
        "fsizeMin",

        "persistentOps",
        "persistentNotifyUrl",
        "persistentPipeline",

        "deleteAfterDays",
    };
    private static final String[] deprecatedPolicyFields = new String[]{
        "asyncOps",
    };
    public final String accessKey;
    private final SecretKeySpec secretKey;

    private QiniuAuth(String accessKey, SecretKeySpec secretKeySpec) {
        this.accessKey = accessKey;
        this.secretKey = secretKeySpec;
    }

    public static QiniuAuth create(String accessKey, String secretKey) {
        if (StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(secretKey)) {
            throw new IllegalArgumentException("empty key");
        }
        byte[] sk = utf8Bytes(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
        return new QiniuAuth(accessKey, secretKeySpec);
    }

    private static void copyPolicy(final StringMap policy, StringMap originPolicy, final boolean strict) {
        if (originPolicy == null) {
            return;
        }
        originPolicy.forEach(new StringMap.Consumer() {
            @Override
            public void accept(String key, Object value) {
                if (inStringArray(key, deprecatedPolicyFields)) {
                    throw new IllegalArgumentException(key + " is deprecated!");
                }
                if (!strict || inStringArray(key, policyFields)) {
                    policy.put(key, value);
                }
            }
        });
    }

    private Mac createMac() {
        Mac mac;
        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException(e);
        }
        return mac;
    }

    public String sign(byte[] data) {
        Mac mac = createMac();
        String encodedSign = encodeToString(mac.doFinal(data));
        return this.accessKey + ":" + encodedSign;
    }

    public String sign(String data) {
        return sign(utf8Bytes(data));
    }

    public String signWithData(byte[] data) {
        String s = encodeToString(data);
        return sign(utf8Bytes(s)) + ":" + s;
    }

    public String signWithData(String data) {
        return signWithData(utf8Bytes(data));
    }

    /**
     * 生成HTTP请求签名字符串
     */
    public String signRequest(String urlString, byte[] body, String contentType) {
        URI uri = URI.create(urlString);
        String path = uri.getRawPath();
        String query = uri.getRawQuery();

        Mac mac = createMac();

        mac.update(utf8Bytes(path));

        if (query != null && query.length() != 0) {
            mac.update((byte) ('?'));
            mac.update(utf8Bytes(query));
        }
        mac.update((byte) '\n');
        if (body != null && ContentType.APPLICATION_FORM_URLENCODED.getMimeType().equalsIgnoreCase(contentType)) {
            mac.update(body);
        }

        String digest = encodeToString(mac.doFinal());

        return this.accessKey + ":" + digest;
    }

    /**
     * 验证回调签名是否正确
     *
     * @param originAuthorization 待验证签名字符串，以 "QBox "作为起始字符
     * @param url 回调地址
     * @param body 回调请求体。原始请求体，不要解析后再封装成新的请求体--可能导致签名不一致。
     * @param contentType 回调ContentType
     */
    public boolean isValidCallback(String originAuthorization, String url, byte[] body, String contentType) {
        String authorization = "QBox " + signRequest(url, body, contentType);
        return authorization.equals(originAuthorization);
    }

    /**
     * 下载签名
     *
     * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、 http://img.domain.com/u/3.jpg?imageView2/1/w/120
     */
    public String privateDownloadUrl(String baseUrl) {
        return privateDownloadUrl(baseUrl, 3600);
    }

    /**
     * 下载签名
     *
     * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、 http://img.domain.com/u/3.jpg?imageView2/1/w/120
     * @param expires 有效时长，单位秒。默认3600s
     */
    public String privateDownloadUrl(String baseUrl, long expires) {
        long deadline = System.currentTimeMillis() / 1000 + expires;
        return privateDownloadUrlWithDeadline(baseUrl, deadline);
    }

    String privateDownloadUrlWithDeadline(String baseUrl, long deadline) {
        StringBuilder b = new StringBuilder();
        b.append(baseUrl);
        int pos = baseUrl.indexOf("?");
        if (pos > 0) {
            b.append("&e=");
        } else {
            b.append("?e=");
        }
        b.append(deadline);
        String token = sign(utf8Bytes(b.toString()));
        b.append("&token=");
        b.append(token);
        return b.toString();
    }

    /**
     * scope = bucket 一般情况下可通过此方法获取token
     *
     * @param bucket 空间名
     * @return 生成的上传token
     */
    public String uploadToken(String bucket) {
        return uploadToken(bucket, null, 3600, null, true);
    }

    /**
     * scope = bucket:key 同名文件覆盖操作、只能上传指定key的文件可以可通过此方法获取token
     *
     * @param bucket 空间名
     * @param key key，可为 null
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key) {
        return uploadToken(bucket, key, 3600, null, true);
    }

    /**
     * 生成上传token
     *
     * @param bucket 空间名
     * @param key key，可为 null
     * @param expires 有效时长，单位秒
     * @param policy 上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。 scope通过
     * bucket、key间接设置，deadline 通过 expires 间接设置
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key, long expires, StringMap policy) {
        return uploadToken(bucket, key, expires, policy, true);
    }

    /**
     * 生成上传token
     *
     * @param bucket 空间名
     * @param key key，可为 null
     * @param expires 有效时长，单位秒。默认3600s
     * @param policy 上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。 scope通过
     * bucket、key间接设置，deadline 通过 expires 间接设置
     * @param strict 是否去除非限定的策略字段，默认true
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key, long expires, StringMap policy, boolean strict) {
        long deadline = System.currentTimeMillis() / 1000 + expires;
        return uploadTokenWithDeadline(bucket, key, deadline, policy, strict);
    }

    public String uploadTokenWithDeadline(String bucket, String key, long deadline, StringMap policy, boolean strict) {
        // TODO   UpHosts Global
        String scope = bucket;
        if (key != null) {
            scope = bucket + ":" + key;
        }
        StringMap x = new StringMap();
        copyPolicy(x, policy, strict);
        x.put("scope", scope);
        x.put("deadline", deadline);

        String s = JSON.toJSONString(x.map());
        return signWithData(utf8Bytes(s));
    }

    public String uploadTokenWithPolicy(Object obj) {
        String s = JSON.toJSONString(obj);
        return signWithData(utf8Bytes(s));
    }

    public StringMap authorization(String url, byte[] body, String contentType) {
        String authorization = "QBox " + signRequest(url, body, contentType);
        return new StringMap().put("Authorization", authorization);
    }

    public StringMap authorization(String url) {
        return authorization(url, null, null);
    }

    /**
     * 生成HTTP请求签名字符串
     */
    public String signRequestV2(String urlString, String method, byte[] body, String contentType) {
        URI uri = URI.create(urlString);

        Mac mac = createMac();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s", method, uri.getPath()));
        if (uri.getQuery() != null) {
            sb.append(String.format("?%s", uri.getQuery()));
        }

        sb.append(String.format("\nHost: %s", uri.getHost()));
        if (uri.getPort() > 0) {
            sb.append(String.format(":%d", uri.getPort()));
        }

        if (contentType != null) {
            sb.append(String.format("\nContent-Type: %s", contentType));
        }

        // body
        sb.append("\n\n");
        if (body != null && body.length > 0 && !StringUtils.isEmpty(contentType)) {
            if (contentType.equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                || contentType.equals(ContentType.APPLICATION_JSON.getMimeType())) {
                sb.append(new String(body));
            }
        }

        log.info(sb.toString());
        mac.update(utf8Bytes(sb.toString()));

        String digest = encodeToString(mac.doFinal());

        return this.accessKey + ":" + digest;
    }

    public StringMap authorizationV2(String url, String method, byte[] body, String contentType) {
        String authorization = "Qiniu " + signRequestV2(url, method, body, contentType);
        return new StringMap().put("Authorization", authorization);
    }

    public StringMap authorizationV2(String url) {
        return authorizationV2(url, "GET", null, null);
    }


    public static byte[] utf8Bytes(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    private static boolean inStringArray(String s, String[] array) {
        for (String x : array) {
            if (x.equals(s)) {
                return true;
            }
        }
        return false;
    }


    public static String encodeToString(byte[] input) {
        return encodeToString(input, 8 | 2);
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(Base64.encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            // US-ASCII is guaranteed to be available.
            throw new AssertionError(e);
        }
    }
}
