<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!--  formato data*/-->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Articles</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <style>
        .card-img-top {
            max-width: 150px;
            max-height: 150px;
            margin: auto;
            display: block;
            object-fit: cover; /* Ajusta las imágenes sin distorsión */
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1 class="text-center mb-4">ARTICLES</h1>

        <!-- Filtros -->
        <div class="row mb-4">
            <div class="col-md-4">
                <form method="get" action="${pageContext.request.contextPath}/Web/articles">
                    <!-- Filtro por autor -->
                    <label for="authorFilter" class="form-label">Filter by Author</label>
                    <select class="form-select" id="authorFilter" name="author">
                        <option value="">All Authors</option>
                        <c:set var="seenAuthors" value="${empty seenAuthors ? {} : seenAuthors}" />
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.id}" 
                                <c:if test="${param.author eq author.id}">selected</c:if>>
                                ${author.fullName}
                            </option>
                        </c:forEach>
                    </select>
                    <!-- Filtro por tópico -->
                    <label for="topicFilter" class="form-label mt-3">Filter by Topic</label>
                    <select class="form-select" id="topicFilter" name="topic">
                        <option value="">All Topics</option>
                        <c:forEach var="topic" items="${topics}">
                            <option value="${topic.id}" 
                                <c:if test="${param.topic eq topic.id}">selected</c:if>>
                                ${topic.name}
                            </option>
                        </c:forEach>
                    </select>
                    <!-- Botón de aplicar filtros -->
                    <button type="submit" class="btn btn-primary mt-3">Apply Filters</button>
                </form>
            </div>
        </div>

        <!-- Verificación de la lista de artículos -->
        <div class="alert alert-info">
            <strong>Artículos cargados:</strong>
            <c:choose>
                <c:when test="${not empty articles}">
                    ${articles.size()} artículos disponibles.
                </c:when>
                <c:otherwise>
                    No se encontraron artículos.
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Listado de artículos -->
        <div class="row">
            <c:forEach var="article" items="${articles}">
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <!-- Imagen destacada con enlace -->
                        <a href="${pageContext.request.contextPath}/Web/articles/${article.id}">
                            <img src="${pageContext.request.contextPath}/resources/img/${article.image != null ? article.image : 'ETSEcentrat.png'}" 
                                 class="card-img-top" alt="Article Image">
                        </a>
                        <div class="card-body">
                            <!-- Título con enlace -->
                            <h4 class="card-title font-weight-bold">
                                <a href="${pageContext.request.contextPath}/Web/articles/${article.id}">${article.title}</a>
                                <c:if test="${!article.public}">
                                    <!-- Icono de artículo privado -->
                                    <img src="${pageContext.request.contextPath}/resources/img/imagensob4.png" alt="Private" style="width:20px; height:20px;">
                                </c:if>
                            </h4>
                            <!-- Autor -->
                            <p><strong>Author:</strong> ${article.author.fullName}</p>
                            <!-- Resumen del artículo -->
                            <p><strong>Summary:</strong> 
                                ${fn:substring(article.summary, 0, 100)}...
                            </p>
                            <!-- Tópicos -->
                            <p><strong>Topics:</strong>
                                <c:forEach var="topic" items="${article.topics}">
                                    ${topic.name}
                                </c:forEach>
                            </p>
                            <!-- Fecha de publicación -->
                            <p><strong>Published:</strong> 
                           <fmt:formatDate value="${article.publishDate}" pattern="dd MMM yyyy"/>
                           </p>
 
                            <!-- Visualizaciones -->
                            <p><strong>Views:</strong> ${article.views}</p>
                            <!-- Botón Leer Más -->
                            <a href="${pageContext.request.contextPath}/Web/articles/${article.id}" class="btn btn-primary">Read More</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
<div class="d-flex justify-content-end mt-3">
    <c:choose>
        <c:when test="${not empty sessionScope.username}">
            <!-- Usuario autenticado -->
            <h2 class="text-end" style="font-size: 2rem; font-weight: bold;">
                Benvingut <a href="${pageContext.request.contextPath}/Web/profile" style="color: #007bff; text-decoration: none;">${sessionScope.username}</a>!
                <a href="${pageContext.request.contextPath}/Web/logout" class="btn btn-danger">Logout</a>
            </h2>
        </c:when>
        <c:otherwise>
            <!-- Usuario no autenticado -->
            <a href="${pageContext.request.contextPath}/Web/login" class="btn btn-link" style="font-size: 1.5rem;">Login</a>
        </c:otherwise>
    </c:choose>
</div>


    </div>
</body>
</html>



