<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!--  formato data*/-->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${article.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <style>
        .text-centered {
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container mt-4 text-centered">
        <h1>${article.title}</h1>
        <!-- Imagen destacada -->
        <div class="mb-3">
            <img src="${pageContext.request.contextPath}/resources/img/${article.image != null ? article.image : 'ETSEcentrat.png'}" 
                 alt="${article.title}" 
                 class="img-fluid rounded" 
                 style="max-width: 600px; height: auto;">
        </div>
        <!-- Información del artículo -->
        <p><strong>Author:</strong> ${article.author != null ? article.author.fullName : 'Unknown'}</p>
                          <p><strong>Published:</strong> 
                           <fmt:formatDate value="${article.publishDate}" pattern="dd MMM yyyy"/>
                           </p>        <p><strong>Views:</strong> ${article.views}</p>
        <p><strong>Topics:</strong>
            <c:forEach var="topic" items="${article.topics}">
                <span class="badge bg-primary">${topic.name}</span>
            </c:forEach>
        </p>
        <!-- Contenido completo -->
        <div class="mt-4">
            <p>${article.content}</p>
        </div>
        <a href="${pageContext.request.contextPath}/Web/articles" class="btn btn-primary">Back to Articles</a>

    </div>
</body>
</html>





