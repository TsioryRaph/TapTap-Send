<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="title" value="Relevé d'opérations"/>
</jsp:include>

<div class="container mt-4">
    <div class="card">
        <div class="card-header bg-primary text-white">
            <h3 class="card-title">Relevé d'opérations</h3>
        </div>
        <div class="card-body">
            <div class="row mb-4">
                <div class="col-md-6">
                    <h5>Client</h5>
                    <p>
                        <strong>${client.nom}</strong><br>
                        ${client.numtel}<br>
                        ${client.pays}
                    </p>
                </div>
                <div class="col-md-6 text-end">
                    <h5>Période</h5>
                    <p>
                        <fmt:formatDate value="${periodStart}" pattern="MMMM yyyy"/>
                    </p>
                    <p class="text-muted">
                        Solde actuel: <fmt:formatNumber value="${client.solde}" type="currency"/>
                    </p>
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead class="table-light">
                        <tr>
                            <th>Date</th>
                            <th>Raison</th>
                            <th>Destinataire</th>
                            <th class="text-end">Montant</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${operations}" var="op">
                            <tr>
                                <td><fmt:formatDate value="${op.date}" pattern="dd/MM/yyyy"/></td>
                                <td>${op.raison}</td>
                                <td>${op.nomRecepteur}</td>
                                <td class="text-end text-danger">- <fmt:formatNumber value="${op.montant}" type="currency"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty operations}">
                            <tr>
                                <td colspan="4" class="text-center">Aucune opération pour cette période</td>
                            </tr>
                        </c:if>
                    </tbody>
                    <tfoot>
                        <tr class="table-active">
                            <th colspan="3">Total des opérations</th>
                            <th class="text-end text-danger">- <fmt:formatNumber value="${totalOperations}" type="currency"/></th>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <div class="mt-4">
                <button onclick="window.print()" class="btn btn-primary me-2">
                    <i class="bi bi-printer"></i> Imprimer
                </button>
                <a href="envoyer?action=generatePdf&numtel=${client.numtel}&month=${month}&year=${year}"
                   class="btn btn-success">
                    <i class="bi bi-file-earmark-pdf"></i> Télécharger PDF
                </a>
                <a href="envoyer" class="btn btn-secondary float-end">
                    <i class="bi bi-arrow-left"></i> Retour
                </a>
            </div>
        </div>
        <div class="card-footer text-muted text-center">
            Généré le <fmt:formatDate value="${now}" pattern="dd/MM/yyyy à HH:mm"/>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>