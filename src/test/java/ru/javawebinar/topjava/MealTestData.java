package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;


import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static Meal ADMIN_MEAL_100002 = new Meal(100002, LocalDateTime.of(2020, Month.DECEMBER, 19, 10, 7, 4), "Breakfast", 416);
    public static Meal ADMIN_MEAL_100003 = new Meal(100003, LocalDateTime.of(2021, Month.JULY, 11, 6, 51, 10), "Breakfast", 2128);
    public static Meal ADMIN_MEAL_100004 = new Meal(100004, LocalDateTime.of(2020, Month.NOVEMBER, 12, 5, 31, 33), "Afternoon snack", 1084);
    public static Meal ADMIN_MEAL_100005 = new Meal(100005, LocalDateTime.of(2021, Month.APRIL, 15, 7, 37, 50), "Afternoon snack", 1907);
    public static Meal ADMIN_MEAL_100006 = new Meal(100006, LocalDateTime.of(2021, Month.MARCH, 12, 10, 28, 8), "Breakfast", 637);
    public static Meal USER_MEAL_100007 = new Meal(100007, LocalDateTime.of(2021, Month.APRIL, 27, 17, 25, 54), "Lunch", 1021);
    public static Meal USER_MEAL_100008 = new Meal(100008, LocalDateTime.of(2020, Month.NOVEMBER, 6, 20, 52, 52), "Afternoon snack", 1016);
    public static Meal USER_MEAL_100009 = new Meal(100009, LocalDateTime.of(2021, Month.MARCH, 23, 13, 29, 11), "Lunch", 1488);
    public static Meal USER_MEAL_100010 = new Meal(100010, LocalDateTime.of(2021, Month.MARCH, 24, 18, 5, 52), "Breakfast", 1885);
    public static Meal USER_MEAL_100011 = new Meal(100011, LocalDateTime.of(2021, Month.JULY, 31, 5, 1, 50), "Dinner", 1806);
    public static Meal USER_MEAL_100012 = new Meal(100012, LocalDateTime.of(2021, Month.APRIL, 2, 3, 11, 45), "Lunch", 2141);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }
}
