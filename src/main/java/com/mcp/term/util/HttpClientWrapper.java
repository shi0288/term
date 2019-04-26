package com.mcp.term.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;


public class HttpClientWrapper {

    /* 请求超时时间 */
    private static int Request_TimeOut = 10000;
    /* 当前连接池中链接 */
    private static CloseableHttpClient httpClientCur = null;

    private synchronized static CloseableHttpClient getHttpClient() {
        if (httpClientCur == null) {

            SSLContext sslContext = null;
            try {
                sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                    //信任所有
                    public boolean isTrusted(X509Certificate[] chain,
                                             String authType) throws CertificateException {
                        return true;
                    }
                }).build();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext))
                    .build();
             /* Http连接池只需要创建一个*/
            PoolingHttpClientConnectionManager httpPoolManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                /* 连接池最大生成连接数200 */
            httpPoolManager.setMaxTotal(200);
              /* 连接池默认路由最大连接数,默认为20 */
            httpPoolManager.setDefaultMaxPerRoute(20);

            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(Request_TimeOut)
                    .setConnectionRequestTimeout(Request_TimeOut)
                    .setSocketTimeout(Request_TimeOut)
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(
                            Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                    .setRedirectsEnabled(false)
                    .build();
            httpClientCur = HttpClients.custom()
                    .setConnectionManager(httpPoolManager)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .build();
        }
        return httpClientCur;
    }


    public static HttpResult sendPost(String url, Map<String, String> headers, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                reqParams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(reqParams, Consts.UTF_8));
        return HttpClientWrapper.execute(httpPost);
    }

    public static HttpResult sendPost(String url, Map<String, String> headers, JSONObject jsonObject) {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        httpPost.setEntity(new StringEntity(jsonObject.toString(), "utf-8"));
        return HttpClientWrapper.execute(httpPost);
    }


    public static HttpResult sendGet(String url, Map<String, String> headers, Map<String, String> params) {
        url = transGetUrl(url, params);
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpGet.addHeader(key, headers.get(key));
            }
        }
        return HttpClientWrapper.execute(httpGet);
    }

    public static String transGetUrl(String url, Map<String, String> params) {
        if (params != null && params.size()>0) {
            String getParam = "";
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    getParam += (key + "=" + params.get(key));
                } else {
                    getParam += ("&" + key + "=" + params.get(key));
                }
                i++;
            }
            if (url.indexOf("?") > -1) {
                url += ("&" + getParam);
            } else {
                url += ("?" + getParam);
            }
        }
        return url;
    }


    public static HttpResult sendGetForBase64(String url, Map<String, String> headers, Map<String, String> params) {
        url = transGetUrl(url, params);
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpGet.addHeader(key, headers.get(key));
            }
        }
        return HttpClientWrapper.executeResource(httpGet);
    }


    public static String orcValid(String base64) {
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> params = new HashMap<>();
        params.put("user", "ugbc1234");
        params.put("pass", "abc.123");
        params.put("softid", "8c4b6409cf36e712a531da0fb58ed279");
        params.put("codetype", "4004");
        params.put("file_base64", base64);
        HttpResult httpResult = HttpClientWrapper.sendPost("http://upload.chaojiying.net/Upload/Processing.php", header, params);
        return httpResult.getResult();
    }


    public static String orcError(String id) {
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> params = new HashMap<>();
        params.put("user", "ugbc1234");
        params.put("pass", "abc.123");
        params.put("softid", "8c4b6409cf36e712a531da0fb58ed279");
        params.put("id", id);
        HttpResult httpResult = HttpClientWrapper.sendPost("http://upload.chaojiying.net/Upload/ReportError.php", header, params);
        return httpResult.getResult();
    }


    public static HttpResult execute(HttpGet httpGet) {
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpGet);
            HttpResult httpResult = new HttpResult();
            for (Header header : response.getAllHeaders()) {
                if (header.getName().toLowerCase().equals("Set-Cookie".toLowerCase())) {
                    httpResult.addCookies(header.getValue().split(";")[0]+";");
                }
            }
            String retString = EntityUtils.toString(response.getEntity());
            httpResult.setResult(retString);
            return httpResult;
        } catch (Exception e) {
           // e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static HttpResult executeResource(HttpGet httpGet) {
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpGet);
            HttpResult httpResult = new HttpResult();
            for (Header header : response.getAllHeaders()) {
                if (header.getName().toLowerCase().equals("Set-Cookie".toLowerCase())) {
                    httpResult.addCookies(header.getValue().split(";")[0]+";");
                }
            }
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                byte[] data = readInputStream(inputStream);
//                File storeFile = new File("/data/images/2008sohu.gif");
//                FileOutputStream output = new FileOutputStream(storeFile);
//                //得到网络资源的字节数组,并写入文件
//                output.write(data);
//                output.close();
                BASE64Encoder encoder = new BASE64Encoder();
                httpResult.setResult(encoder.encode(data));
            }
            return httpResult;
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        byte[] data =  outStream.toByteArray();
        //关闭输出流
        outStream.close();
        return data;
    }


    public static HttpResult execute(HttpPost httpPost) {
        HttpContext context = HttpClientContext.create();
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpPost, context);
            HttpResult httpResult = new HttpResult();
            for (Header header : response.getAllHeaders()) {
                if (header.getName().toLowerCase().equals("Set-Cookie".toLowerCase())) {
                    httpResult.addCookies(header.getValue().split(";")[0]+";");
                }
            }
            httpResult.setCode(response.getStatusLine().getStatusCode());
            String retString = EntityUtils.toString(response.getEntity());
            httpResult.setResult(retString);
            return httpResult;
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        return null;
    }

}
