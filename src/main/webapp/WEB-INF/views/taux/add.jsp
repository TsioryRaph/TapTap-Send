<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<c:set var="pageTitle" value="Ajouter un taux" scope="request" />

<h1>Ajouter un taux de change</h1>

<form action="taux" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="action" value="add">

    <div class="mb-3">
        <label for="idtaux" class="form-label">ID du taux (ex: EUR-MGA)</label>
        <input type="text" class="form-control" id="idtaux" name="idtaux" required
               pattern="[A-Z]{3}-[A-Z]{3}"
               title="Format: 3 lettres, tiret, 3 lettres (ex: EUR-MGA)">
    </div>

    <div class="mb-3">
        <label for="montant1" class="form-label">Montant 1</label>
        <input type="number" step="0.0001" class="form-control" id="montant1"
               name="montant1" min="0.0001" required>
    </div>

    <div class="mb-3">
        <label for="montant2" class="form-label">Montant 2</label>
        <input type="number" step="0.0001" class="form-control" id="montant2"
               name="montant2" min="0.0001" required>
    </div>

    <button type="submit" class="btn btn-primary">Ajouter</button>
    <a href="taux" class="btn btn-secondary">Annuler</a>
</form>

<script>
function validateForm() {
    const idTaux = document.getElementById('idtaux').value;
    if (!idTaux.match(/^[A-Z]{3}-[A-Z]{3}$/)) {
        alert('Format ID taux invalide. Doit être comme: EUR-MGA');
        return false;
    }
    return true;
}
</script>

<%@ include file="/WEB-INF/views/footer.jsp" %>