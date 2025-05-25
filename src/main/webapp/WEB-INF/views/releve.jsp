<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Génération de relevé - TapTapSend</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .releve-container {
            max-width: 600px;
            margin: 30px auto;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/header.jsp" %>

    <div class="container">
        <div class="releve-container">
            <h2 class="text-center mb-4">Générer un relevé</h2>

            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/releve" method="post">
                <div class="mb-3">
                    <label for="numClient" class="form-label">Numéro de client</label>
                    <input type="text" class="form-control" id="numClient" name="numClient" required>
                </div>

                <div class="mb-3">
                    <label for="periode" class="form-label">Période</label>
                    <select class="form-select" id="periode" name="periode" required>
                        <option value="">Sélectionnez une période</option>
                        <option value="mois">Ce mois</option>
                        <option value="trimestre">Ce trimestre</option>
                        <option value="annee">Cette année</option>
                        <option value="personnalise">Période personnalisée</option>
                    </select>
                </div>

                <div class="mb-3" id="dateDebutContainer" style="display: none;">
                    <label for="dateDebut" class="form-label">Date de début</label>
                    <input type="date" class="form-control" id="dateDebut" name="dateDebut">
                </div>

                <div class="mb-3" id="dateFinContainer" style="display: none;">
                    <label for="dateFin" class="form-label">Date de fin</label>
                    <input type="date" class="form-control" id="dateFin" name="dateFin">
                </div>

                <button type="submit" class="btn btn-primary w-100">
                    <i class="bi bi-file-earmark-pdf"></i> Générer le PDF
                </button>
            </form>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Affiche/masque les champs de date selon la sélection
        document.getElementById('periode').addEventListener('change', function() {
            const isCustom = this.value === 'personnalise';
            document.getElementById('dateDebutContainer').style.display = isCustom ? 'block' : 'none';
            document.getElementById('dateFinContainer').style.display = isCustom ? 'block' : 'none';
        });
    </script>
</body>
</html>