package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryImpl implements MealRepository {

    private Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private AtomicInteger mealId = new AtomicInteger();

    public MealRepositoryImpl() {
        MealsUtil.getMeals().forEach(this::save);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getById(int id) {
        return mealMap.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        meal.setId(mealId.incrementAndGet());
        return mealMap.put(meal.getId(), meal);
    }

    @Override
    public Meal update(Meal meal) {
        return mealMap.replace(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }
}
