package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@WebServlet("/budgets")
public class BudgetServlet extends HttpServlet {
    private BudgetService budgetService = new BudgetService();
    private CategoryService categoryService = new CategoryService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String selectedType = request.getParameter("type");
            List<Category> categories = Collections.emptyList();

            if (selectedType != null && !selectedType.isEmpty()) {
                CategoryType type = CategoryType.valueOf(selectedType);
                categories = categoryService.getCategoriesByType(type);
            }

            List<Budget> budgets = budgetService.getAllBudgets();

            request.setAttribute("categories", categories);
            request.setAttribute("budgets", budgets);

            request.setAttribute("selectedType", selectedType);
            request.setAttribute("amount", request.getParameter("amount"));
            request.setAttribute("period", request.getParameter("period"));

        } catch (Exception e) {
            request.setAttribute("error", "Error loading data: " + e.getMessage());
        }

        request.getRequestDispatcher("budgets.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String error = null;
        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            String categoryIdParam = request.getParameter("category");
            String amountParam = request.getParameter("amount");
            String periodParam = request.getParameter("period");
            String typeParam = request.getParameter("type");

            if (categoryIdParam == null || categoryIdParam.isEmpty()) {
                error = "Please select a category";
            } else if (amountParam == null || amountParam.isEmpty()) {
                error = "Amount cannot be empty";
            } else if (periodParam == null || periodParam.isEmpty()) {
                error = "Month must be selected";
            }

            if (error != null) {
                request.setAttribute("error", error);
                doGet(request, response);
                return;
            }

            Long categoryId = Long.parseLong(categoryIdParam);
            BigDecimal amount = new BigDecimal(amountParam);
            YearMonth period = YearMonth.parse(periodParam);
            CategoryType type = CategoryType.valueOf(typeParam);

            tx.begin();

            Category category = em.find(Category.class, categoryId);
            if (category == null) {
                error = "Selected category not found";
                throw new ServletException(error);
            }

            Budget budget = new Budget();
            budget.setCategory(category);
            budget.setAmount(amount);
            budget.setPeriod(period);

            em.persist(budget);
            tx.commit();

            response.sendRedirect("budgets");
            return;

        } catch (NumberFormatException e) {
            error = "Invalid number format: " + e.getMessage();
        } catch (DateTimeParseException e) {
            error = "Invalid month format (use YYYY-MM)";
        } catch (IllegalArgumentException e) {
            error = "Invalid category type";
        } catch (Exception e) {
            error = "Error saving budget: " + e.getMessage();
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }

        if (error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
        }
    }
}