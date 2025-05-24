<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<c:set var="pageTitle" value="Liste des frais d' envoi" scope="request" />

<h1>Liste des frais d'envoi</h1>

<a href="frais?action=add" class="btn btn-success mb-3">Ajouter des frais</a>

<table class="table table-striped">
    <thead>
        <tr>
            <th>ID</th>
            <th>Montant 1</th>
            <th>Montant 2</th>
            <th>Frais</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${fraisEnvoiList}" var="frais">
            <tr>
                <td>${frais.idfrais}</td>
                <td>${frais.montant1}</td>
                <td>${frais.montant2}</td>
                <td>${frais.frais}</td>
                <td>
                    <a href="frais?action=edit&idfrais=${frais.idfrais}" class="btn btn-primary btn-sm">Modifier</a>
                    <form action="frais" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="idfrais" value="${frais.idfrais}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr?')">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/footer.jsp" %>