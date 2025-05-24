<jsp:include page="../layout/header.jsp">
    <jsp:param name="title" value="Générer PDF"/>
</jsp:include>

<h1>Générer un relevé d'opérations</h1>

<form action="envoyer" method="post">
    <input type="hidden" name="action" value="generatePdf">

    <div class="mb-3">
        <label for="numtel" class="form-label">Client</label>
        <select class="form-select" id="numtel" name="numtel" required>
            <c:forEach items="${clients}" var="client">
                <option value="${client.numtel}">${client.numtel} - ${client.nom}</option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label for="month" class="form-label">Mois</label>
        <select class="form-select" id="month" name="month" required>
            <c:forEach begin="1" end="12" var="m">
                <option value="${m}" ${m == currentMonth ? 'selected' : ''}>
                    <fmt:formatDate value="${m}" pattern="MMMM" var="monthName"/>
                    ${monthName}
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label for="year" class="form-label">Année</label>
        <input type="number" class="form-control" id="year" name="year"
               min="2000" max="2100" value="${currentYear}" required>
    </div>

    <button type="submit" class="btn btn-primary">Générer PDF</button>
    <a href="envoyer" class="btn btn-secondary">Annuler</a>
</form>

<jsp:include page="../layout/footer.jsp"/>