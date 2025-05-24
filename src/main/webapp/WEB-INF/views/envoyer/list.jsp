<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<c:set var="pageTitle" value="Liste des transferts" scope="request" />

<h1>Liste des transferts</h1>

<div class="row mb-3">
    <div class="col-md-6">
        <form action="envoyer" method="get" class="row g-3">
            <input type="hidden" name="action" value="searchByDate">
            <div class="col-md-6">
                <input type="date" name="date" class="form-control" value="<fmt:formatDate value="${today}" pattern="yyyy-MM-dd"/>
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-primary">Rechercher par date</button>
            </div>
        </form>
    </div>
    <div class="col-md-6 text-end">
        <a href="envoyer?action=new" class="btn btn-success me-2">Nouveau transfert</a>
        <a href="envoyer?action=showPdfForm" class="btn btn-info">Générer PDF</a>
    </div>
</div>

<table class="table table-striped table-hover">
    <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Envoyeur</th>
            <th>Récepteur</th>
            <th>Montant</th>
            <th>Date</th>
            <th>Raison</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${envois}" var="envoi">
            <tr>
                <td>${envoi.idEnv}</td>
                <td>${envoi.numEnvoyeur}</td>
                <td>${envoi.numRecepteur} (${envoi.nomRecepteur})</td>
                <td><fmt:formatNumber value="${envoi.montant}" type="currency"/></td>
                <td><fmt:formatDate value="${envoi.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td>${envoi.raison}</td>
                <td>
                    <div class="btn-group">
                        <a href="envoyer?action=edit&idEnv=${envoi.idEnv}" class="btn btn-sm btn-primary">Modifier</a>
                        <form action="envoyer" method="post" class="d-inline">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="idEnv" value="${envoi.idEnv}">
                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce transfert?')">Supprimer</button>
                        </form>
                    </div>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/footer.jsp" %>