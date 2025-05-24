<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<c:set var="pageTitle" value="Ajouter un client" scope="request" />

<h1>Ajouter un client</h1>

<form action="client" method="post">
    <input type="hidden" name="action" value="add">

    <div class="mb-3">
        <label for="numtel" class="form-label">Numéro de téléphone</label>
        <input type="text" class="form-control" id="numtel" name="numtel" required>
    </div>

    <div class="mb-3">
        <label for="nom" class="form-label">Nom complet</label>
        <input type="text" class="form-control" id="nom" name="nom" required>
    </div>

    <div class="mb-3">
        <label for="sexe" class="form-label">Sexe</label>
        <select class="form-select" id="sexe" name="sexe">
            <option value="Masculin">Masculin</option>
            <option value="Féminin">Féminin</option>
        </select>
    </div>

    <div class="mb-3">
        <label for="pays" class="form-label">Pays</label>
        <input type="text" class="form-control" id="pays" name="pays" required>
    </div>

    <div class="mb-3">
        <label for="solde" class="form-label">Solde initial</label>
        <input type="number" class="form-control" id="solde" name="solde" required>
    </div>

    <div class="mb-3">
        <label for="mail" class="form-label">Email</label>
        <input type="email" class="form-control" id="mail" name="mail" required>
    </div>

    <button type="submit" class="btn btn-primary">Ajouter</button>
    <a href="client" class="btn btn-secondary">Annuler</a>
</form>

<%@ include file="/WEB-INF/views/footer.jsp" %>