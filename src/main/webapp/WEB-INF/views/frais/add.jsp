<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<c:set var="pageTitle" value="Ajouter des frais d'envoi" scope="request" />

<h1>Ajouter des frais d'envoi</h1>

<form action="frais" method="post">
    <input type="hidden" name="action" value="add">

    <div class="mb-3">
        <label for="idfrais" class="form-label">ID des frais (ex: EUR-MGA)</label>
        <input type="text" class="form-control" id="idfrais" name="idfrais" required
               placeholder="Format: DEVISE1-DEVISE2 (ex: EUR-MGA)">
    </div>

    <div class="mb-3">
        <label for="montant1" class="form-label">Montant 1</label>
        <input type="number" step="0.01" class="form-control" id="montant1" name="montant1" required>
        <div class="form-text">Montant dans la devise d'origine (ex: 1 Euro)</div>
    </div>

    <div class="mb-3">
        <label for="montant2" class="form-label">Montant 2</label>
        <input type="number" step="0.01" class="form-control" id="montant2" name="montant2" required>
        <div class="form-text">Montant dans la devise cible (ex: 4800 Ariary)</div>
    </div>

    <div class="mb-3">
        <label for="frais" class="form-label">Frais d'envoi</label>
        <input type="number" step="0.01" class="form-control" id="frais" name="frais" required>
        <div class="form-text">Frais dans la devise d'origine (ex: 0.5 Euro)</div>
    </div>

    <button type="submit" class="btn btn-primary">Ajouter</button>
    <a href="frais" class="btn btn-secondary">Annuler</a>
</form>

<%@ include file="/WEB-INF/views/footer.jsp" %>