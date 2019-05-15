package com.brodiequinlan.sprintrestrospective;

import com.brodiequinlan.sprintrestrospective.controllers.FeatureController;
import com.brodiequinlan.sprintrestrospective.controllers.LoginController;
import com.brodiequinlan.sprintrestrospective.controllers.ProjectController;
import com.brodiequinlan.sprintrestrospective.database.SqlConnection;
import com.brodiequinlan.sprintrestrospective.models.Feature;
import com.brodiequinlan.sprintrestrospective.models.Login;
import com.brodiequinlan.sprintrestrospective.models.Project;
import com.brodiequinlan.sprintrestrospective.models.Token;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EndpointTests {
    @Before
    public void setup() {
        SqlConnection sql = new SqlConnection();
        sql.DELETE_ALL();
        sql.RESET_INC();
        Assume.assumeTrue(sql.addUser("c", "c").equals("success"));
        sql.close();
    }

    @Test
    public void getProjectsTest() {
        SqlConnection sql = new SqlConnection();
        Project project = new Project("test", "c", "-1");
        String id = sql.addProject(project);
        Assume.assumeFalse(id.equals("-1"));
        Assume.assumeFalse(id.equals("-2"));
        ProjectController pc = new ProjectController();
        String token = Login.login("c", "c").token;
        List<Project> projects = pc.all(token, "c");
        Assert.assertTrue(projects.size() > 0);
        Assert.assertEquals(projects.get(0).id, id + "");
        sql.close();
    }

    @Test
    public void addProjectTest() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "test2");
        payload.put("owner", "c");
        ProjectController pc = new ProjectController();
        Project p = pc.add(payload);
        Assert.assertNotEquals("-1", p.id);
        Assert.assertNotEquals("-2", p.id);


        payload = new HashMap<>();
        payload.put("name", "test2");

        p = pc.add(payload);
        Assert.assertEquals("-2", p.id);
    }

    @Test
    public void addFeatureTest() {
        SqlConnection sql = new SqlConnection();
        Project project = new Project("test", "c", "-1");
        String id = sql.addProject(project);
        Assume.assumeFalse(id.equals("-1"));
        Assume.assumeFalse(id.equals("-2"));
        FeatureController fc = new FeatureController();
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "test feature");
        payload.put("points", "6");
        String token = Login.login("c", "c").token;
        payload.put("token", token);
        payload.put("username", "c");
        payload.put("project", id);
        Feature feature = fc.add(payload);
        Assert.assertNotEquals("-1", feature.id);
        sql.close();
    }

    @Test
    public void getFeaturesTest() {
        SqlConnection sql = new SqlConnection();
        Project project = new Project("test", "c", "-1");
        String id = sql.addProject(project);
        Assume.assumeFalse(id.equals("-1"));
        Assume.assumeFalse(id.equals("-2"));
        FeatureController fc = new FeatureController();
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "test feature");
        payload.put("points", "6");
        String token = Login.login("c", "c").token;
        payload.put("token", token);
        payload.put("username", "c");
        payload.put("project", id + "");
        Feature feature = fc.add(payload);
        Assume.assumeFalse(feature.id.equals("-1"));
        List<Feature> features = fc.all(id + "", token, "c");
        Assert.assertTrue(features.size() > 0);
        Assert.assertEquals("test feature", features.get(0).name);
        sql.close();
    }

    @Test
    public void loginTest()
    {
        LoginController lc = new LoginController();
        Assert.assertTrue(lc.issue("c", "c").token.length() > 0);
        Assert.assertNotEquals("INVALID_AUTH", lc.issue("c", "c").token);
        Assert.assertEquals("INVALID_AUTH", lc.issue("c", "c1").token);
    }

    @Test
    public void registerTest()
    {
        LoginController lc = new LoginController();
        Assert.assertEquals("INVALID_REG", lc.register("c", "c").token);
        Assert.assertNotEquals("INVALID_REG", lc.register("c1", "c").token);
    }

    @Test
    public void validateTest()
    {
        LoginController lc = new LoginController();
        Token tok = lc.issue("c", "c");
        Assert.assertTrue(lc.validate(tok.token, "c"));
        Assert.assertFalse(lc.validate(tok.token, "c1"));
        Assert.assertFalse(lc.validate("dfkll..234jf", "c"));
    }
}
