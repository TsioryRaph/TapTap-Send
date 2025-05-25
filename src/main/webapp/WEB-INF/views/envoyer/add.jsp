<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<c:set var="pageTitle" value="Nouveau transfert" scope="request" />

<!-- Génération de l'ID de transaction -->
<c:set var="generatedId" value="${fn:replace(java.util.UUID.randomUUID(), '-', '')}" />

<h1 class="mb-4">Nouveau transfert d'argent</h1>

<c:if test="${not empty message}">
    <div class="alert alert-${messageType} alert-dismissible fade show">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/envoyer" method="post" id="transferForm" novalidate>
    <!-- Champs cachés -->
    <input type="hidden" name="action" value="add">
    <input type="hidden" name="idEnv" value="${generatedId}" id="transactionId">

    <div class="row g-3">
        <!-- Section envoyeur -->
        <div class="col-md-6">
            <label for="numEnvoyeur" class="form-label">Envoyeur <span class="text-danger"></span></label>
            <select class="form-select" id="numEnvoyeur" name="numEnvoyeur" required>
                <option value="" disabled selected>-- Sélectionnez un envoyeur --</option>
                <c:forEach items="${clients}" var="client">
                    <option value="${client.numtel}" ${param.numEnvoyeur == client.numtel ? 'selected' : ''}>
                        ${client.numtel} - ${client.nom} (${client.pays})
                    </option>
                </c:forEach>
            </select>
            <div class="invalid-feedback">Veuillez sélectionner un envoyeur</div>
        </div>

        <!-- Section récepteur -->
        <div class="col-md-6">
            <label for="numRecepteur" class="form-label">Récepteur <span class="text-danger"></span></label>
            <select class="form-select" id="numRecepteur" name="numRecepteur" required>
                <option value="" disabled selected>-- Sélectionnez un récepteur --</option>
                <c:forEach items="${clients}" var="client">
                    <option value="${client.numtel}" ${param.numRecepteur == client.numtel ? 'selected' : ''}>
                        ${client.numtel} - ${client.nom} (${client.pays})
                    </option>
                </c:forEach>
            </select>
            <div class="invalid-feedback">Veuillez sélectionner un récepteur</div>
        </div>

        <!-- Section montant -->
        <div class="col-md-6">
            <label for="montant" class="form-label">Montant (€) <span class="text-danger"></span></label>
            <div class="input-group has-validation">
                <input type="number" class="form-control" id="montant" name="montant"
                       value="${not empty param.montant ? param.montant : ''}"
                       min="0.01" step="0.01" required>
                <span class="input-group-text">€</span>
                <div class="invalid-feedback">Veuillez entrer un montant valide</div>
            </div>
        </div>

        <!-- Section date -->
        <div class="col-md-6">
            <label for="date" class="form-label">Date <span class="text-danger"></span></label>
            <input type="datetime-local" class="form-control" id="date" name="date"
                   value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd'T'HH:mm" />" required>
            <div class="invalid-feedback">Veuillez sélectionner une date</div>
        </div>

        <!-- Section raison -->
        <div class="col-12">
            <label for="raison" class="form-label">Raison</label>
            <input type="text" class="form-control" id="raison" name="raison"
                   value="${fn:escapeXml(param.raison)}" maxlength="255"
                   placeholder="Motif du transfert (optionnel)">
        </div>
    </div>

    <!-- Boutons d'action -->
    <div class="d-flex justify-content-between mt-5">
        <div>
            <button type="reset" class="btn btn-outline-secondary me-2">
                <i class="bi bi-arrow-counterclockwise"></i> Réinitialiser
            </button>
            <a href="${pageContext.request.contextPath}/envoyer" class="btn btn-secondary">
                <i class="bi bi-x-circle"></i> Annuler
            </a>
        </div>
        <button type="submit" class="btn btn-primary">
            <i class="bi bi-send-check"></i> Effectuer le transfert
        </button>
    </div>
</form>

<script>
// Validation côté client
(function() {
    'use strict'

    const form = document.getElementById('transferForm')

    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault()
            event.stopPropagation()
        }

        form.classList.add('was-validated')

        // Debug: afficher l'ID de transaction
        console.log('Transaction ID:', document.getElementById('transactionId').value)
    }, false)
})()
</script>

<%@ include file="/WEB-INF/views/footer.jsp" %>