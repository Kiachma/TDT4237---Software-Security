<div class="container">
<h1>Add Address</h1>
    <form action="addAddress.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${not empty values.from}">
            <input type="hidden" name="from" value="${values.from}">
        </c:if>
        <div>
            <div><label for="address">Add address: </label></div>
            <textarea id="address" name="address" rows="5" cols="20"><c:out value="${address.address}" /> </textarea>
        </div>
        <br>
        <div>
        	<div>Confirm account password to add address:</div>
        	<div><input id="password" name="password" type="password" value=""></div>
        </div>
        <div><input type="submit" value="Submit" /></div>
    </form>
</div>