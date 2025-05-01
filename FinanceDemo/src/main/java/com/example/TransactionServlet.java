package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
    private TransactionService service = new TransactionService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Create transaction
            Transaction t = new Transaction();
            t.setAmount(new BigDecimal(request.getParameter("amount")));
            t.setType(request.getParameter("type"));
            t.setDate(LocalDate.now());

            // Handle category
            String categoryName = request.getParameter("category");
            Category category = new CategoryService().findOrCreateCategory(
                    em, // Pass existing EntityManager
                    categoryName,
                    CategoryType.valueOf(request.getParameter("type"))
            );
            t.setCategory(category);

            // Handle tags
            String tagsInput = request.getParameter("tags");
            if (tagsInput != null && !tagsInput.trim().isEmpty()) {
                Set<Tag> tags = new TagService().processTags(em, tagsInput);
                t.getTags().addAll(tags);
            }

            // Persist transaction
            em.persist(t);
            tx.commit();

            // Refresh list
            List<Transaction> transactions = new TransactionService().getAllTransactions();
            request.setAttribute("transactions", transactions);
            request.getRequestDispatcher("transactions.jsp").forward(request, response);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            em.close();
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Transaction> transactions = service.getAllTransactions();
            request.setAttribute("transactions", transactions);
            request.getRequestDispatcher("transactions.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}