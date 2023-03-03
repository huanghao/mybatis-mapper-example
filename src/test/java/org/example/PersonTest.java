package org.example;

import io.mybatis.mapper.example.Example;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonTest {

    @Autowired
    private PersonMapper personMapper;

    @Test
    public void testInsertOne() {
        Person person = new Person();
        person.setName("alice");
        person.setAge(16);
        Assert.assertEquals(1, personMapper.insert(person));
    }

    @Test
    public void testInsertMany() {
        Person bob = new Person("bob", 1);
        Person cris = new Person();
        cris.setName("cris");
        Assert.assertEquals(2, personMapper.insertList(Arrays.asList(bob, cris)));
    }

    @Test
    public void testSelectByName() {
        List<Person> res = personMapper.wrapper()
                .eq(Person::getName, "alice")
                .list();
        Assert.assertEquals("alice", res.get(0).getName().toLowerCase());
    }

    @Test
    public void testSelectByNameFirstOne() {
        Optional<Person> res = personMapper.wrapper()
                .eq(Person::getName, "alice")
                .first();
        Assert.assertEquals("alice", res.get().getName().toLowerCase());
    }

    @Test
    public void testThereAreManyAlice() {
        try {
            personMapper.wrapper()
                    .eq(Person::getName, "alice")
                    .one();
        } catch (Exception ex) {
            System.out.println(ex);
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void testUpdateSomebodyName() {
        Person john = personMapper.selectByPrimaryKey(1L).orElse(null);
        Assert.assertTrue(john != null);

        String randomName = "john-" + new Date().toString();
        john.setName(randomName);
        int originAge = john.getAge();
        john.setAge(null);

        Assert.assertEquals(1, personMapper.updateByPrimaryKeySelective(john));

        Person johnFromDb = personMapper.selectByPrimaryKey(1L).orElse(null);
        Assert.assertTrue(johnFromDb != null);
        Assert.assertEquals(randomName, johnFromDb.getName());
        Assert.assertTrue(originAge == johnFromDb.getAge());
    }

    @Test
    public void testKevinByExample() {
        Example<Person> example = personMapper.example();
        example.createCriteria()
                .andEqualTo(Person::getName, "kevin");

        Person kevin = new Person();
        kevin.setAge(99);

        Assert.assertEquals(1, personMapper.updateByExampleSelective(kevin, example));
    }

    @Test
    public void testSelectMany() {
        List<Person> res = personMapper.wrapper()
                .in(Person::getName, Arrays.asList("alice", "bob"))
                .lt(Person::getAge, 50)
                .endSql("limit 3")
                .list();
        System.out.println(res);
        Assert.assertTrue(res.size() > 1);
    }

    @Test
    public void testHowManyAlice() {
        long cnt = personMapper.wrapper()
                .eq(Person::getName, "alice")
                .count();
        Assert.assertTrue(cnt > 0);
    }

    @Test
    public void updateAllAliceAndBob() {
        Example<Person> example = personMapper.example();
        example.createCriteria()
                .andIn(Person::getName, Arrays.asList("alice", "bob"));

        Person johndoe = new Person();
        johndoe.setAge(44);

        Assert.assertTrue(personMapper.updateByExampleSelective(johndoe, example) > 0);
    }
}
