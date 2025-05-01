<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Transactions</title>
</head>
<body>
<div class="container mt-4">
    <nav class="mb-4">
        <a href="transactions" class="btn btn-outline-primary">Transactions</a>
        <a href="budgets" class="btn btn-outline-primary">Budgets</a>
    </nav>
    <h2>Transactions</h2>

    <form method="POST" action="transactions">
        <div class="mb-3">
            <label class="form-label">Amount*</label>
            <input type="number" name="amount" step="0.01" required
                   class="form-control" min="0.01">
        </div>

        <div class="mb-3">
            <label class="form-label">Type*</label>
            <select name="type" class="form-select" required>
                <option value="">Choose type</option>
                <option value="INCOME">Income</option>
                <option value="EXPENSE">Expense</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Category*</label>
            <input type="text" name="category" required
                   class="form-control" placeholder="e.g. Salary, Rent">
        </div>

        <div class="mb-3">
            <label class="form-label">Tags</label>
            <input type="text" name="tags"
                   class="form-control"
                   placeholder="comma-separated (e.g. food, utilities)">
        </div>

        <button type="submit" class="btn btn-primary">Add Transaction</button>
    </form>

    <!-- Transactions List -->
    <table class="table table-striped mt-4">
        <thead>
        <tr>
            <th>Type</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Date</th>
            <th>Tags</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${transactions}" var="t">
            <tr>
                <td>${t.type}</td>
                <td>${t.category.name}</td>
                <td>$${t.amount}</td>
                <td>${t.date}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty t.tags}">
                            <c:forEach items="${t.tags}" var="tag">
                                <span class="badge bg-secondary">${tag.name}</span>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">No tags</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>