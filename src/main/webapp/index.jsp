<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TapTapSend - Accueil</title>
    <style>
        .dashboard {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin: 30px 0;
        }
        .dashboard-item {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            text-align: center;
            transition: all 0.3s ease;
        }
        .dashboard-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .button {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Bienvenue sur TapTapSend</h2>
        <p>Plateforme de gestion de transfert d'argent en ligne</p>

        <!-- Redirection automatique vers le dashboard si l'utilisateur est connecté -->
        <c:if test="${not empty sessionScope.user}">
            <script>
                window.location.href = "${pageContext.request.contextPath}/dashboard";
            </script>
        </c:if>

        <div class="dashboard">
            <div class="dashboard-item">
                <h3>Clients</h3>
                <p>Gérez vos clients</p>
                <a href="${pageContext.request.contextPath}/client" class="button">Accéder</a>
            </div>

            <div class="dashboard-item">
                <h3>Taux de change</h3>
                <p>Configurez les taux</p>
                <a href="${pageContext.request.contextPath}/taux" class="button">Accéder</a>
            </div>

            <div class="dashboard-item">
                <h3>Frais d'envoi</h3>
                <p>Gérez les frais</p>
                <a href="${pageContext.request.contextPath}/fraisEnvoi" class="button">Accéder</a>
            </div>

            <div class="dashboard-item">
                <h3>Envois</h3>
                <p>Effectuez des transferts</p>
                <a href="${pageContext.request.contextPath}/envoyer" class="button">Accéder</a>
            </div>

            <div class="dashboard-item">
                <h3>Relevés</h3>
                <p>Générez des relevés</p>
                <a href="${pageContext.request.contextPath}/releve" class="button">Accéder</a>
            </div>
        </div>
    </div>
</body>
</html>

<%@ include file="/WEB-INF/views/footer.jsp" %>