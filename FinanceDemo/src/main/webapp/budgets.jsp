<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Budget Planning</title>
    <style>
        .budget-form { margin-bottom: 2rem; }
    </style>
</head>
<body>
<div class="container mt-4">
    <nav class="mb-4">
        <a href="transactions" class="btn btn-outline-primary">Transactions</a>
        <a href="budgets" class="btn btn-primary">Budgets</a>
    </nav>

    <h2 class="mb-4">Budget Planning</h2>

    <!-- Error Message Display -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Budget Creation Section -->
    <div class="card budget-form">
        <div class="card-body">
            <h5 class="card-title">Create New Budget</h5>

            <!-- Type Selection Form -->
            <form method="GET" action="budgets" class="mb-4">
                <div class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label">Category Type</label>
                        <select name="type" class="form-select"
                                onchange="this.form.submit()" required>
                            <option value="">Select Type...</option>
                            <option value="EXPENSE" ${param.type == 'EXPENSE' ? 'selected' : ''}>
                                Expense Limit
                            </option>
                            <option value="INCOME" ${param.type == 'INCOME' ? 'selected' : ''}>
                                Income Target
                            </option>
                        </select>
                    </div>
                </div>
            </form>

            <!-- Budget Entry Form -->
            <form method="POST" action="budgets">
                <div class="row g-3">
                    <input type="hidden" name="type" value="${param.type}">

                    <div class="col-md-4">
                        <label class="form-label">Category</label>
                        <select name="category" class="form-select" required
                        ${empty categories ? 'disabled' : ''}>
                            <option value="">Select Category...</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.id}" ${param.category == cat.id ? 'selected' : ''}>
                                        ${cat.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label">Amount</label>
                        <input type="number" name="amount" step="0.01" min="0.01"
                               class="form-control" required
                               value="${not empty param.amount ? param.amount : ''}">
                    </div>

                    <div class="col-md-3">
                        <label class="form-label">Month</label>
                        <input type="month" name="period" class="form-control" required
                               value="${not empty param.period ? param.period : ''}">
                    </div>

                    <div class="col-md-2 align-self-end">
                        <button type="submit" class="btn btn-success w-100">Set Budget</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Simplified Budget List -->
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Current Budgets</h5>

            <c:choose>
                <c:when test="${empty budgets}">
                    <div class="alert alert-info">
                        No budgets set yet. Create your first budget using the form above.
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>Category</th>
                            <th>Type</th>
                            <th>Amount</th>
                            <th>Month</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${budgets}" var="budget">
                            <tr>
                                <td>${budget.category.name}</td>
                                <td>
                                        <span class="badge ${budget.category.type == 'INCOME' ? 'bg-success' : 'bg-danger'}">
                                                ${budget.category.type}
                                        </span>
                                </td>
                                <td><fmt:formatNumber value="${budget.amount}" type="currency"/></td>
                                <td>${budget.period}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>