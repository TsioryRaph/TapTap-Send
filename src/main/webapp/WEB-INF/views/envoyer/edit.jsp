<jsp:include page="../layout/header.jsp">
    <jsp:param name="title" value="Modifier un transfert"/>
</jsp:include>

<h1>Modifier un transfert</h1>

<form action="envoyer" method="post">
    <input type="hidden" name="action" value="update">

    <div class="mb-3">
        <label for="idEnv" class="form-label">ID du transfert</label>
        <input type="text" class="form-control" id="idEnv" name="idEnv" value="${envoi.idEnv}" readonly>
    </div>

    <div class="mb-3">
        <label for="numEnvoyeur" class="form-label">Envoyeur</label>
        <select class="form-select" id="numEnvoyeur" name="numEnvoyeur" required>
            <c:forEach items="${clients}" var="client">
                <option value="${client.numtel}" ${client.numtel == envoi.numEnvoyeur ? 'selected' : ''}>
                    ${client.numtel} - ${client.nom} (${client.pays})
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label for="numRecepteur" class="form-label">Récepteur</label>
        <select class="form-select" id="numRecepteur" name="numRecepteur" required>
            <c:forEach items="${clients}" var="client">
                <option value="${client.numtel}" ${client.numtel == envoi.numRecepteur ? 'selected' : ''}>
                    ${client.numtel} - ${client.nom} (${client.pays})
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label for="montant" class="form-label">Montant</label>
        <input type="number" class="form-control" id="montant" name="montant" value="${envoi.montant}" required>
    </div>

    <div class="mb-3">
        <label for="date" class="form-label">Date</label>
        <input type="datetime-local" class="form-control" id="date" name="date"
               value="<fmt:formatDate value="${envoi.date}" pattern="yyyy-MM-dd'T'HH:mm"/>" required>
    </div>

    <div class="mb-3">
        <label for="raison" class="form-label">Raison</label>
        <input type="text" class="form-control" id="raison" name="raison" value="${envoi.raison}">
    </div>

    <button type="submit" class="btn btn-primary">Mettre à jour</button>
    <a href="envoyer" class="btn btn-secondary">Annuler</a>
</form>

<jsp:include page="../layout/footer.jsp"/>