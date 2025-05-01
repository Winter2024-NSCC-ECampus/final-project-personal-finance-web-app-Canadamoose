<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <div class="alert alert-danger">
        <h2>Error: ${pageContext.errorData.statusCode}</h2>
        <p>Message: ${pageContext.exception.message}</p>
        <a href="budgets" class="btn btn-primary">Back to Budgets</a>
    </div>
</div>
</body>
</html>