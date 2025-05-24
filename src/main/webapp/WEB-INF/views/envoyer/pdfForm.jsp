<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<c:set var="pageTitle" value="Générer un PDF" scope="request" />

<div class="container mt-4">
    <h1 class="mb-4">Générer un relevé d'opérations</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="envoyer" method="post" class="needs-validation" novalidate>
        <input type="hidden" name="action" value="generatePdf">

        <div class="row g-3">
            <div class="col-md-6">
                <label for="numtel" class="form-label">Client <span class="text-danger">*</span></label>
                <select class="form-select" id="numtel" name="numtel" required>
                    <option value="">Sélectionnez un client</option>
                    <c:forEach items="${clients}" var="client">
                        <option value="${client.numtel}"
                            ${param.numtel == client.numtel ? 'selected' : ''}>
                            ${client.numtel} - ${client.nom}
                        </option>
                    </c:forEach>
                </select>
                <div class="invalid-feedback">
                    Veuillez sélectionner un client.
                </div>
            </div>

            <div class="col-md-3">
                <label for="month" class="form-label">Mois <span class="text-danger">*</span></label>
                <select class="form-select" id="month" name="month" required>
                    <option value="">Sélectionnez un mois</option>
                    <option value="1" ${1 == currentMonth ? 'selected' : ''}>Janvier</option>
                    <option value="2" ${2 == currentMonth ? 'selected' : ''}>Février</option>
                    <option value="3" ${3 == currentMonth ? 'selected' : ''}>Mars</option>
                    <option value="4" ${4 == currentMonth ? 'selected' : ''}>Avril</option>
                    <option value="5" ${5 == currentMonth ? 'selected' : ''}>Mai</option>
                    <option value="6" ${6 == currentMonth ? 'selected' : ''}>Juin</option>
                    <option value="7" ${7 == currentMonth ? 'selected' : ''}>Juillet</option>
                    <option value="8" ${8 == currentMonth ? 'selected' : ''}>Août</option>
                    <option value="9" ${9 == currentMonth ? 'selected' : ''}>Septembre</option>
                    <option value="10" ${10 == currentMonth ? 'selected' : ''}>Octobre</option>
                    <option value="11" ${11 == currentMonth ? 'selected' : ''}>Novembre</option>
                    <option value="12" ${12 == currentMonth ? 'selected' : ''}>Décembre</option>
                </select>
                <div class="invalid-feedback">
                    Veuillez sélectionner un mois.
                </div>
            </div>

            <div class="col-md-3">
                <label for="year" class="form-label">Année <span class="text-danger">*</span></label>
                <input type="number" class="form-control" id="year" name="year"
                       min="2000" max="2100" value="${currentYear}" required>
                <div class="invalid-feedback">
                    Veuillez entrer une année valide.
                </div>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-file-earmark-pdf"></i> Générer PDF
                </button>
                <a href="envoyer" class="btn btn-secondary">
                    <i class="bi bi-x-circle"></i> Annuler
                </a>
            </div>
        </div>
    </form>
</div>

<script>
// Validation Bootstrap
(function () {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation')

    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }

            form.classList.add('was-validated')
        }, false)
    })
})()
</script>

<%@ include file="/WEB-INF/views/footer.jsp" %>