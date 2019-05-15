package com.brodiequinlan.sprintrestrospective;

import com.brodiequinlan.sprintrestrospective.database.SqlConnection;
import com.brodiequinlan.sprintrestrospective.models.Feature;
import com.brodiequinlan.sprintrestrospective.models.Project;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTests {

    @Before
    public void setup() {
        SqlConnection sql = new SqlConnection();
        sql.init();
        sql.DELETE_ALL();
        sql.RESET_INC();
        Assume.assumeTrue(sql.addUser("c", "c").equals("success"));
    }

    @Test
    public void validateUserTest() {
        SqlConnection sql = new SqlConnection();
        Assume.assumeTrue(sql.addUser("bob", "sagget").equals("success"));
        Assert.assertTrue(sql.validateUser("bob", "sagget"));
        Assert.assertFalse(sql.validateUser("bob", "sagge"));
        sql.close();
    }

    @Test
    public void getProjectTest() {
        SqlConnection sql = new SqlConnection();
        Project proj = new Project("bob", "c", "void");
        String id = sql.addProject(proj);
        Assert.assertNotEquals(id, "-1");
        Assert.assertNotEquals(id, "-2");
        sql.close();
    }

    @Test
    public void getProjectsTest() {
        SqlConnection sql = new SqlConnection();
        Project proj = new Project("bob2", "c", "void");
        sql.addProject(proj);
        Assert.assertTrue(sql.getProjects("c").size() > 0);
        sql.close();
    }

    @Test
    public void getFeaturesTest()
    {
        SqlConnection sql = new SqlConnection();
        Project proj = new Project("testp","c","void");
        String id = sql.addProject(proj);
        Assume.assumeFalse(id ==  "-1");
        Assume.assumeFalse(id == "-2");

        Feature feat = new Feature("-1","testf","6","c",new ArrayList<>());
        feat = sql.addFeature(feat, "c", id);
        Assume.assumeFalse(feat.id.equals("-1"));
        Assume.assumeFalse(feat.id.equals("-2"));

        List<Feature> features = sql.getFeatures(id);

        Assert.assertEquals(features.size(), 1);
        Assert.assertEquals(features.get(0),feat);

        sql.close();
    }
    @Test
    public void addFeatureTest()
    {
        SqlConnection sql = new SqlConnection();

        Project proj = new Project("testp","c","void");
        String id = sql.addProject(proj);
        Assume.assumeFalse(id ==  "-1");
        Assume.assumeFalse(id == "-2");

        Feature feat = new Feature("-1","testf","6","c",new ArrayList<>());
        feat = sql.addFeature(feat, "c", id);
        Assert.assertFalse(feat.id.equals("-1"));
        Assert.assertFalse(feat.id.equals("-2"));

        sql.close();
    }

}
