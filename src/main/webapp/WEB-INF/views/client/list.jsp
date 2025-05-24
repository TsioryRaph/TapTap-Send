<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<c:set var="pageTitle" value="Liste des clients" scope="request" />

<h1>Liste des clients</h1>

<div class="mb-3">
    <form action="client" method="get" class="row g-3">
        <input type="hidden" name="action" value="search">
        <div class="col-auto">
            <input type="text" name="searchTerm" class="form-control" placeholder="Rechercher...">
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-primary">Rechercher</button>
        </div>
    </form>
</div>

<a href="client?action=add" class="btn btn-success mb-3">Ajouter un client</a>

<table class="table table-striped">
    <thead>
        <tr>
            <th>Numéro</th>
            <th>Nom</th>
            <th>Sexe</th>
            <th>Pays</th>
            <th>Solde</th>
            <th>Email</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${clients}" var="client">
            <tr>
                <td>${client.numtel}</td>
                <td>${client.nom}</td>
                <td>${client.sexe}</td>
                <td>${client.pays}</td>
                <td>${client.solde}</td>
                <td>${client.mail}</td>
                <td>
                    <a href="client?action=edit&numtel=${client.numtel}" class="btn btn-primary btn-sm">Modifier</a>
                    <form action="client" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="numtel" value="${client.numtel}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr?')">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/footer.jsp" %>