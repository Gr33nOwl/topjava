package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(ru.javawebinar.topjava.web.MealServlet.class);
    private MealRepository repository;

    public MealServlet() {
        super();
        repository = new MealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.debug("redirect to meals");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        action = action == null ? "" : action;
        switch (action.toLowerCase()) {
            case "delete":
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                log.debug("delete meal");
                repository.delete(mealId);
                response.sendRedirect("meals");
                break;
            case "edit":
                mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = repository.getById(mealId);
                log.debug("edit meal");
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                return;
            case "create":
                Meal newMeal = new Meal(null, null, 0);
                log.debug("create new meal");
                request.setAttribute("meal", newMeal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                return;
            default:
                List<MealTo> list = MealsUtil.filteredByStreams(repository.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
                log.debug("show meal list");
                request.setAttribute("list", list);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        int mealId = Integer.parseInt(request.getParameter("mealId"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);
        if (mealId == 0) {
            repository.save(meal);
        } else {
            meal.setId(mealId);
            repository.update(meal);
        }
        response.sendRedirect("meals");
    }
}
