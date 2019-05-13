package com.brodiequinlan.sprintrestrospective;

import com.brodiequinlan.sprintrestrospective.database.SqlConnection;
import com.brodiequinlan.sprintrestrospective.models.Project;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTests {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseTests.class);

    @Before
    public void setup() {
        SqlConnection sql = new SqlConnection();
        sql.init();
        sql.DELETE_ALL();
        sql.RESET_INC();
        Assume.assumeTrue(sql.addUser("c", "c").equals("success"));
    }

    @Test
    public void validateUser() {
        SqlConnection sql = new SqlConnection();
        Assume.assumeTrue(sql.addUser("bob", "sagget").equals("success"));
        Assert.assertTrue(sql.validateUser("bob", "sagget"));
        Assert.assertFalse(sql.validateUser("bob", "sagge"));
        sql.close();
    }

    @Test
    public void testAddProject() {
        SqlConnection sql = new SqlConnection();
        Project proj = new Project("bob", "c", "void");
        int id = sql.addProject(proj);
        Assert.assertNotEquals(id, -1);
        Assert.assertNotEquals(id, -2);
        sql.close();
    }


    @Test
    public void testGetProjects() {
        SqlConnection sql = new SqlConnection();
        Project proj = new Project("bob2", "c", "void");
        sql.addProject(proj);
        logger.info(sql.getProjects("c").get(0).name);
        Assert.assertTrue(sql.getProjects("c").size() > 0);
        sql.close();
    }

}
