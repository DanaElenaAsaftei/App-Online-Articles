<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h1 class="text-center">Login</h1>
        
        <!-- Mostrar mensaje de error si las credenciales no son correctas -->
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">
                Invalid username or password. Please try again.
            </div>
        </c:if>

        <!-- Formulario de autenticación -->
        <form action="${pageContext.request.contextPath}/Web/login" method="post" class="mt-4">
            <div class="mb-3">
                <label for="username" class="form-label">Username:</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
    </div>
</body>
</html>


