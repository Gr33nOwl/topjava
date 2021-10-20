package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.*;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create new meal {} for UserId={}", meal, authUserId());
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update meal {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getAll for UserId={}", authUserId());
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredByDate(String startDate, String endDate, String startTime, String endTime) {
        log.info("get all filtered for UserId={}", authUserId());
        LocalDate sd = startDate == null || startDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalDate ed = endDate == null || endDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDate);
        LocalTime st = startTime == null || startTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime et = endTime == null || endTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTime);
        return MealsUtil.getFilteredTos(service.getFilteredByDate(authUserId(), sd, ed), authUserCaloriesPerDay(), st, et);
    }
}