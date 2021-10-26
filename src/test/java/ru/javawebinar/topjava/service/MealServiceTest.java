package ru.javawebinar.topjava.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;


    @Test
    public void get() {
        Meal meal = service.get(ADMIN_MEAL_100002.getId(), ADMIN_ID);
        assertMatch(meal, ADMIN_MEAL_100002);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_100008.getId(), ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_100008.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_100008.getId(), USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_100008.getId(), ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> expected = Stream.of(USER_MEAL_100009, USER_MEAL_100010)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        List<Meal> actual = service.getBetweenInclusive(LocalDate.of(2021, Month.MARCH, 23), LocalDate.of(2021, Month.MARCH, 24), USER_ID);
        assertMatch(actual, expected);
    }

    @Test
    public void getAll() {
        List<Meal> expected = Stream.of(ADMIN_MEAL_100006, ADMIN_MEAL_100005, ADMIN_MEAL_100004, ADMIN_MEAL_100003, ADMIN_MEAL_100002)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        List<Meal> actual = service.getAll(ADMIN_ID);
        assertMatch(actual, expected);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(USER_MEAL_100008);
        updatedMeal.setCalories(750);
        updatedMeal.setDescription("updatedMeal");
        service.update(updatedMeal, USER_ID);
        assertMatch(updatedMeal, service.get(updatedMeal.getId(), USER_ID));
    }

    @Test
    public void updateNotFound() {
        Meal updatedMeal = new Meal(USER_MEAL_100008);
        updatedMeal.setCalories(750);
        updatedMeal.setDescription("updatedMeal");
        assertThrows(NotFoundException.class, () -> service.update(updatedMeal, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2020, Month.DECEMBER, 10, 11, 35), "Lunch", 1000);
        Meal createdMeal = service.create(newMeal, USER_ID);
        newMeal.setId(createdMeal.getId());
        assertMatch(newMeal, createdMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal meal = service.get(USER_MEAL_100008.getId(), USER_ID);
        Meal duplicateMeal = new Meal(meal);
        duplicateMeal.setId(null);
        assertThrows(DataAccessException.class, () -> service.create(duplicateMeal, USER_ID));
    }


}