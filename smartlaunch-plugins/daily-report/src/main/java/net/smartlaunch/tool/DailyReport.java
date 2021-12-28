package net.smartlaunch.tool;

import com.alibaba.fastjson.JSON;
import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.client.fluent.Request;
import org.joda.time.DateTime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DailyReport implements Plugin, Configurable {

    @Override
    public String getName() {
        return "dr";
    }

    @Override
    public String getSummary() {
        return "daily report";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.TEXT;
    }

    @Override
    public void setParameters(String... parameters) {
        metricsArray = parameters;
    }

    private String[] metricsArray;

    private String[] defaultMetricsArray = {"push_pre", "push_post", "lottery", "chat", "gift", "room", "index"};

    @Override
    public InputStream execute() {
        DecimalFormat df = new DecimalFormat("0.00");
        String[] array = ArrayUtils.isNotEmpty(metricsArray) ? metricsArray : defaultMetricsArray;
        List<Diff> diffs = Arrays.stream(array).map(e -> Diff.builder().metrics(e).build()).collect(Collectors.toList());
        List<String> results = diffs.parallelStream().map(e -> e.getMetrics() + " 环比: " + df.format(e.build().compare() * 100) + "%").collect(Collectors.toList());
        StringBuilder stb = new StringBuilder();
        results.forEach(e -> stb.append(e).append(System.lineSeparator()));
        return new ByteArrayInputStream(stb.toString().getBytes(StandardCharsets.UTF_8));
    }

    static class Diff {
        private String prevUrl;
        private String currUrl;
        String metrics;
        DateTime now = DateTime.now().minusHours(8);
        Date currTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0).toDate();
        DateTime last = DateTime.now().minusHours(8).minusDays(1);
        Date prevTime = new DateTime(last.getYear(), last.getMonthOfYear(), last.getDayOfMonth(), 0, 0, 0).toDate();
        private double diffVal;
        private double diffPercentage;

        public String getMetrics() {
            return metrics;
        }

        public double getDiffVal() {
            return diffVal;
        }

        public double getDiffPercentage() {
            return diffPercentage;
        }

        public static Diff builder() {
            return new Diff();
        }

        public Diff metrics(String metrics) {
            this.metrics = metrics;
            return this;
        }

        public Diff build() {
            long diffInSeconds = Math.abs(currTime.getTime() - prevTime.getTime()) / 1000;
            String prevTimeStr = new DateTime(prevTime).toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            prevUrl = "http://thanos.qtt6.cn/api/v1/query" +
                    "?query=" + getQuery(diffInSeconds) +
                    "&dedup=true" +
                    "&partial_response=true" +
                    "&time=" + prevTimeStr;

            String currTimeStr = new DateTime(currTime).toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            currUrl = "http://thanos.qtt6.cn/api/v1/query" +
                    "?query=" + getQuery(diffInSeconds) +
                    "&dedup=true" +
                    "&partial_response=true" +
                    "&time=" + currTimeStr;
            return this;
        }

        private String getQuery(long diffInSeconds) {
            try {
                return URLEncoder.encode("clamp_min(sum(" + metrics + ") - sum(" + metrics + " offset " + diffInSeconds + "s), 0)", StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        public double compare() {
            try {
                String prevResp = Request.Get(prevUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                String currResp = Request.Get(currUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                System.out.println("prev: " + prevUrl + ": " + prevResp);
                System.out.println("curr: " + currUrl + ": " + currResp);
                double prevVal = Double.parseDouble(JSON.parseObject(prevResp).getJSONObject("data").getJSONArray("result").getJSONObject(0).getJSONArray("value").getString(1));
                double currVal = Double.parseDouble(JSON.parseObject(currResp).getJSONObject("data").getJSONArray("result").getJSONObject(0).getJSONArray("value").getString(1));
                diffVal = currVal - prevVal;
                System.out.println(prevVal + " / " + currVal + "/" + diffVal);
                if (prevVal == 0) {
                    return 1;
                }
                diffPercentage = diffVal / prevVal;
                return diffPercentage;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
