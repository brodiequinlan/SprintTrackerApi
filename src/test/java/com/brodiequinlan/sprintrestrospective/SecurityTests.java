package com.brodiequinlan.sprintrestrospective;

import com.brodiequinlan.sprintrestrospective.security.Hash;
import com.brodiequinlan.sprintrestrospective.security.Salt;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTests {

    @Test
    public void HashGeneration() {
        Assert.assertEquals("0416A26BA554334286B1954918ECAD7BA6C33575B49DF915FF3367B5CEF7ECD93B1F0B436636667B27B363011543971F1C81C3151D5EF72733501C1FF33C34AF", Objects.requireNonNull(Hash.sha512("bob")).toUpperCase());
    }

    @Test
    public void SaltGeneration() {
        Assert.assertEquals(16, Salt.generateSalt().length());
        Assert.assertNotEquals(Salt.generateSalt(), Salt.generateSalt());
    }

}
