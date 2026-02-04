<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center">User Profile</h1>
        <div class="card mt-4">
            <div class="card-body">
                <h4 class="card-title">Welcome, ${sessionScope.profile_name}!</h4>
                <p><strong>ID:</strong> ${sessionScope.profile_id}</p>
                <p><strong>Username:</strong> ${sessionScope.username}</p>
                <p><strong>Email:</strong> ${sessionScope.profile_email}</p>
                
                <a href="${pageContext.request.contextPath}/Web/articles" class="btn btn-primary">Back to Articles</a>
            </div>
        </div>
    </div>
</body>
</html>


