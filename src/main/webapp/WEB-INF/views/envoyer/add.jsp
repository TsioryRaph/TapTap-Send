<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<c:set var="pageTitle" value="Nouveau transfert" scope="request" />

<h1>Nouveau transfert d'argent</h1>

<c:if test="${not empty message}">
    <div class="alert alert-${messageType}">${message}</div>
</c:if>

<form action="${pageContext.request.contextPath}/envoyer" method="post">
    <input type="hidden" name="action" value="transferer">

    <div class="mb-3">
        <label for="numEnvoyeur" class="form-label">Envoyeur</label>
        <select class="form-select" id="numEnvoyeur" name="numEnvoyeur" required>
            <option value="">-- Sélectionnez un envoyeur --</option>
            <c:forEach items="${clients}" var="client">
                <option value="${client.numtel}" ${param.numEnvoyeur == client.numtel ? 'selected' : ''}>
                    ${client.numtel} - ${client.nom} (${client.pays})
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label for="numRecepteur" class="form-label">Récepteur</label>
        <select class="form-select" id="numRecepteur" name="numRecepteur" required>
            <option value="">-- Sélectionnez un récepteur --</option>
            <c:forEach items="${clients}" var="client">
                <option value="${client.numtel}" ${param.numRecepteur == client.numtel ? 'selected' : ''}>
                    ${client.numtel} - ${client.nom} (${client.pays})
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label for="montant" class="form-label">Montant</label>
        <input type="number" class="form-control" id="montant" name="montant"
               value="${param.montant}" min="1" required>
    </div>

    <div class="mb-3">
        <label for="date" class="form-label">Date</label>
        <input type="datetime-local" class="form-control" id="date" name="date"
               value="<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd'T'HH:mm"/>" required>
    </div>

    <div class="mb-3">
        <label for="raison" class="form-label">Raison</label>
        <input type="text" class="form-control" id="raison" name="raison" value="${param.raison}">
    </div>

    <button type="submit" class="btn btn-primary">Effectuer le transfert</button>
    <a href="${pageContext.request.contextPath}/envoyer?action=list" class="btn btn-secondary">Annuler</a>
</form>

<%@ include file="/WEB-INF/views/footer.jsp" %>