package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, 1));
        this.save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 30, 10, 0), "Завтрак", 500), 2);
        this.save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 30, 13, 0), "Обед", 1000), 2);
        this.save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0), "Ужин", 500), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for user {}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (!isPresent(userId, meal.getId())) {
            return null;
        } else {
            return repository.get(userId).put(meal.getId(), meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} for user {}", id, userId);
        if (!isPresent(userId, id)) {
            return false;
        } else {
            return repository.get(userId).remove(id) != null;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for userId={}", id, userId);
        if (!isPresent(userId, id)) {
            return null;
        } else {
            return repository.get(userId).get(id);
        }

    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public Collection<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate));
    }

    public Collection<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        if(repository.get(userId) != null){
        return repository.get(userId)
                .values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        }
        else {
            return Collections.emptyList();
        }
    }

    boolean isPresent(int userId, int id) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        boolean result = userMealMap != null && userMealMap.get(id) != null;
        if (!result) log.info("meal with id={} not found for userId={}", id, userId);
        return result;
    }
}

