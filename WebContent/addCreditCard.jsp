<div class="container">
<h1>Add Credit Card</h1>
	<div class="general-form">
	<c:if test="${not empty messages}">
        <c:forEach var="message" items="${messages}">
            <div><span class="error">${message.value}</span></div>
        </c:forEach>
    </c:if>
    <form action="addCreditCard.do" method="post">
    	<table class="general-table">
        <tr>
            <td><label for="creditCardNumber">Credit Card Number: </label></td>
            <td><input id="creditCardNumber" name="creditCardNumber" type="text" value="${values.creditCardNumber}" /></td>
            <c:if test="${not empty messages.creditCardNumber}">
                <td class="error">${messages.creditCardNumber}</td>
            </c:if>
        </tr>
        <tr>
            <td><label for="cardholderName">Cardholder's name: </label></td>
            <td><input id="cardholderName" name="cardholderName" type="text" value="${values.cardholderName}" /></td>
            <c:if test="${not empty messages.cardholderName}">
                <td class="error">${messages.cardholderName}</td>
            </c:if>
        </tr>
        <tr>
            <td><label for="expiryDate">Expiry date: </label></td>
            <td><select id="expiryDate" name="expiryMonth">
                <option value="0">1</option>
                <option value="1">2</option>
                <option value="2">3</option>
                <option value="3">4</option>
                <option value="4">5</option>
                <option value="5">6</option>
                <option value="6">7</option>
                <option value="7">8</option>
                <option value="8">9</option>
                <option value="9">10</option>
                <option value="10">11</option>
                <option value="11">12</option>
            </select>
            /
            <select id="expiryDate" name="expiryYear">
                <c:forEach var="year" items="${years}">
                    <option value="${year}">${year}</option>
                </c:forEach>
            </select>
            </td>
            <c:if test="${not empty messages.expiryDate}">
                <td class="error">${messages.expiryDate}</td>
            </c:if>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
	        	<td><label for="password">Confirm account password to store changes:</label></td>
	        	<td><input id="password" name="password" type="password" value="" /></td>
 		</tr>
        </table>
        <div><input type="submit" value="Submit" /></div>
    </form>
    </div>
</div>