package net.smartlaunch.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CiCd implements Plugin, Configurable {

    private String tag;
    private String app;

    @Override
    public String getName() {
        return "cicd";
    }

    @Override
    public String getSummary() {
        return "gitlab & 雨燕";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.TEXT;
    }

    @Override
    public void setParameters(String... parameters) {
        if (ArrayUtils.isEmpty(parameters)) {
            tag = null;
            app = null;
            return;
        }
        if (parameters.length > 0) {
            app = parameters[0];
        }
        if (parameters.length > 1) {
            tag = parameters[1];
        }
    }

    @Override
    public InputStream execute() {
        try {
            String projectJsonStr = Request.Get("https://git.qutoutiao.net/api/v4/projects?search=" + app)
                    .addHeader("PRIVATE-TOKEN", "iNgvRjWGtyHqW2pZto_f")
                    .execute()
                    .returnContent().asString(StandardCharsets.UTF_8);
            JSONArray projectJsonArray = JSONArray.parseArray(projectJsonStr);
            if (projectJsonArray.size() == 0) {
                return new ByteArrayInputStream("gitlab 中未找到项目".getBytes(StandardCharsets.UTF_8));
            }
            if (projectJsonArray.size() > 1) {
                StringBuilder gitlabProjects = new StringBuilder();
                for (int i = 0; i < projectJsonArray.size(); i++) {
                    gitlabProjects.append(projectJsonArray.getJSONObject(i).getString("name")).append("\n");
                }
                return new ByteArrayInputStream(("gitlab 找到多个项目\n" + gitlabProjects).getBytes(StandardCharsets.UTF_8));
            }
            int projectId = -1;
            for (int i = 0; i < projectJsonArray.size(); i++) {
                JSONObject projectJson = projectJsonArray.getJSONObject(i);
                projectId = projectJson.getInteger("id");
            }

            String pipelineJsonStr = Request.Post("https://git.qutoutiao.net/api/v4/projects/" + projectId + "/pipeline?ref=" + tag)
                    .addHeader("PRIVATE-TOKEN", "iNgvRjWGtyHqW2pZto_f")
                    .execute()
                    .returnContent().asString(StandardCharsets.UTF_8);
            JSONObject pipelineJson = JSONObject.parseObject(pipelineJsonStr);
            int pipelineId = pipelineJson.getInteger("id");

            String pipelineJobsJsonStr = Request.Get("https://git.qutoutiao.net/api/v4/projects/" + projectId + "/pipelines/" + pipelineId + "/jobs")
                    .addHeader("PRIVATE-TOKEN", "iNgvRjWGtyHqW2pZto_f")
                    .execute()
                    .returnContent().asString(StandardCharsets.UTF_8);
            JSONArray pipelineJobsJsonArray = JSONArray.parseArray(pipelineJobsJsonStr);
            Integer jobId = pipelineJobsJsonArray.getJSONObject(0).getInteger("id");
            String commitId = pipelineJobsJsonArray.getJSONObject(0).getJSONObject("commit").getString("short_id");

            String triggerJob = Request.Post("https://git.qutoutiao.net/api/v4/projects/" + projectId + "/jobs/" + jobId + "/play")
                    .addHeader("PRIVATE-TOKEN", "iNgvRjWGtyHqW2pZto_f")
                    .execute()
                    .returnContent().asString(StandardCharsets.UTF_8);

            String jobStatus = null;
            do {
                Thread.sleep(5000);
                String jobInfo = Request.Get("https://git.qutoutiao.net/api/v4/projects/" + projectId + "/jobs/" + jobId)
                        .addHeader("PRIVATE-TOKEN", "iNgvRjWGtyHqW2pZto_f")
                        .execute()
                        .returnContent().asString(StandardCharsets.UTF_8);
                jobStatus = JSONObject.parseObject(jobInfo).getJSONObject("pipeline").getString("status");
            } while (jobStatus.equals("running"));

            if (!"success".equals(jobStatus)) {
                return new ByteArrayInputStream("job 执行失败".getBytes(StandardCharsets.UTF_8));
            }

            String matchedProjectsJsonStr = Request.Get("https://cmdb.qutoutiao.net/api/v0.1/projects?name_like=" + app + "&page=1&count=10")
                    .execute()
                    .returnContent().asString(StandardCharsets.UTF_8);
            JSONArray matchedProjectsArray = JSON.parseObject(matchedProjectsJsonStr).getJSONArray("projects");
            if (matchedProjectsArray.size() == 0) {
                return new ByteArrayInputStream("cmdb 中未找到对应项目".getBytes(StandardCharsets.UTF_8));
            }
            if (matchedProjectsArray.size() > 1) {
                StringBuilder projectNames = new StringBuilder();
                for (int i = 0; i < matchedProjectsArray.size(); i++) {
                    projectNames.append(matchedProjectsArray.getJSONObject(i).getString("project_name")).append("\n");
                }
                return new ByteArrayInputStream(("cmdb 找到多个项目\n" + projectNames).getBytes(StandardCharsets.UTF_8));
            }
            String projectName = matchedProjectsArray.getJSONObject(0).getString("project_name");

            JSONObject json = new JSONObject();
            json.put("appID", projectName);
            json.put("ref", tag + "(" + commitId + ")");
            json.put("env", "dev");
            json.put("deployType", 1);
            json.put("hosts", new JSONArray(Lists.newArrayList("172.25.128.14")));
            json.put("notifier", "dengchao02@qutoutiao.net");
            System.out.println(json.toJSONString());
            Request post = Request.Post("http://cd.qttcs3.cn/cd-service/v1/external/api/deploy")
                    .addHeader("api-token", "e4ab1cb382bf9e5c4d2c05ebf353193f")
                    .bodyString(json.toJSONString(), ContentType.APPLICATION_JSON);
            String result = post.execute().returnContent().asString(StandardCharsets.UTF_8);
            return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
            return new ByteArrayInputStream(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
