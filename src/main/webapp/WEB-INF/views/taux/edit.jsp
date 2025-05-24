<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<c:set var="pageTitle" value="Modifier un taux de change" scope="request" />

<h1>Modifier un taux de change</h1>

<form action="taux" method="post">
    <input type="hidden" name="action" value="update">

    <div class="mb-3">
        <label for="idtaux" class="form-label">ID du taux</label>
        <input type="text" class="form-control" id="idtaux" name="idtaux"
               value="${taux.idtaux}" readonly>
    </div>

    <div class="mb-3">
        <label for="montant1" class="form-label">Montant 1</label>
        <input type="number" step="0.01" class="form-control" id="montant1"
               name="montant1" value="${taux.montant1}" required>
        <div class="form-text">Exemple: 1 pour 1 Euro</div>
    </div>

    <div class="mb-3">
        <label for="montant2" class="form-label">Montant 2</label>
        <input type="number" step="0.01" class="form-control" id="montant2"
               name="montant2" value="${taux.montant2}" required>
        <div class="form-text">Exemple: 4800 pour 4800 Ariary</div>
    </div>

    <button type="submit" class="btn btn-primary">Mettre à jour</button>
    <a href="taux" class="btn btn-secondary">Annuler</a>
</form>

<%@ include file="/WEB-INF/views/footer.jsp" %>