<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<c:set var="pageTitle" value="Liste des Taux de change" scope="request" />

<h1>Liste des taux de change</h1>

<a href="taux?action=add" class="btn btn-success mb-3">Ajouter un taux</a>

<table class="table table-striped">
    <thead>
        <tr>
            <th>ID</th>
            <th>Montant 1</th>
            <th>Montant 2</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${tauxList}" var="taux">
            <tr>
                <td>${taux.idtaux}</td>
                <td>${taux.montant1}</td>
                <td>${taux.montant2}</td>
                <td>
                    <a href="taux?action=edit&idtaux=${taux.idtaux}" class="btn btn-primary btn-sm">Modifier</a>
                    <form action="taux" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="idtaux" value="${taux.idtaux}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr?')">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/footer.jsp" %>