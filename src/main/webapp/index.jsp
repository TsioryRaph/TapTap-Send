<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TapTapSend - Accueil</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .dashboard {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
            margin: 40px 0;
        }
        .dashboard-item {
            border: 1px solid #dee2e6;
            border-radius: 10px;
            padding: 25px;
            text-align: center;
            transition: all 0.3s ease;
            background: white;
        }
        .dashboard-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
            border-color: #0d6efd;
        }
        .dashboard-icon {
            font-size: 2.5rem;
            margin-bottom: 15px;
            color: #0d6efd;
        }
        .welcome-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 40px;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/header.jsp" %>

    <div class="container my-5">
        <div class="welcome-section">
            <h1 class="display-4">Bienvenue sur TapTapSend</h1>
            <p class="lead">Plateforme de gestion de transfert d'argent en ligne</p>

            <!-- Redirection automatique vers le dashboard si l'utilisateur est connecté -->
            <c:if test="${not empty sessionScope.user}">
                <div class="alert alert-info">
                    Vous êtes déjà connecté. Redirection vers le tableau de bord...
                </div>
                <script>
                    setTimeout(function() {
                        window.location.href = "${pageContext.request.contextPath}/dashboard";
                    }, 2000);
                </script>
            </c:if>
        </div>

        <div class="dashboard">
            <div class="dashboard-item">
                <div class="dashboard-icon">
                    <i class="bi bi-people-fill"></i>
                </div>
                <h3>Gestion des Clients</h3>
                <p>Créez et gérez vos clients</p>
                <a href="${pageContext.request.contextPath}/client" class="btn btn-primary">
                    <i class="bi bi-box-arrow-in-right"></i> Accéder
                </a>
            </div>

            <div class="dashboard-item">
                <div class="dashboard-icon">
                    <i class="bi bi-currency-exchange"></i>
                </div>
                <h3>Taux de change</h3>
                <p>Configurez les taux de conversion</p>
                <a href="${pageContext.request.contextPath}/taux" class="btn btn-primary">
                    <i class="bi bi-box-arrow-in-right"></i> Accéder
                </a>
            </div>

            <div class="dashboard-item">
                <div class="dashboard-icon">
                    <i class="bi bi-cash-coin"></i>
                </div>
                <h3>Frais d'envoi</h3>
                <p>Paramétrez les frais de transfert</p>
                <a href="${pageContext.request.contextPath}/fraisEnvoi" class="btn btn-primary">
                    <i class="bi bi-box-arrow-in-right"></i> Accéder
                </a>
            </div>

            <div class="dashboard-item">
                <div class="dashboard-icon">
                    <i class="bi bi-send-fill"></i>
                </div>
                <h3>Transferts</h3>
                <p>Effectuez des envois d'argent</p>
                <a href="${pageContext.request.contextPath}/envoyer" class="btn btn-primary">
                    <i class="bi bi-box-arrow-in-right"></i> Accéder
                </a>
            </div>

            <div class="dashboard-item">
                <div class="dashboard-icon">
                    <i class="bi bi-file-earmark-text-fill"></i>
                </div>
                <h3>Relevés</h3>
                <p>Générez des relevés PDF</p>
                <a href="${pageContext.request.contextPath}/releve" class="btn btn-primary">
                    <i class="bi bi-box-arrow-in-right"></i> Accéder
                </a>
            </div>

            <c:if test="${empty sessionScope.user}">
                <div class="dashboard-item">
                    <div class="dashboard-icon">
                        <i class="bi bi-person-fill"></i>
                    </div>
                    <h3>Connexion</h3>
                    <p>Accédez à votre espace</p>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-success">
                        <i class="bi bi-box-arrow-in-right"></i> Se connecter
                    </a>
                </div>
            </c:if>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>