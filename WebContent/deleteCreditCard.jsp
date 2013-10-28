<div class="container">
    <h1>Delete Credit Card</h1>
    <div>Do you want to delete the following credit card?</div>
    <div>
        <div>Credit card number: <c:out value="${creditCard.maskedCreditCardNumber}"/></div>
        <div>Expiry date: <fmt:formatDate value="${creditCard.expiryDate.time}" type="date" dateStyle="short" /></div>
        <div>Cardholder's name: <c:out value="${creditCard.cardholderName}"/></div>
    </div>
    <form action="deleteCreditCard.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <input name="id" value="${creditCard.id}" type="hidden" />
        <br>
        <div>
        	<div>Confirm the account password to delete the card.</div>
        	<div>Password: <input id="password" name="password" type="password" value="" /></div>
     		<div><input type="submit" value="Confirm" /></div>
        </div>
    </form>
</div>