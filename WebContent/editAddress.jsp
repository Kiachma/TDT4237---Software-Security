<div class="container">
    <h1>Edit Address</h1>
    <form action="editAddress.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <input name="id" value="${address.id}" type="hidden" />
        <div>
            <div><label for="address">Edit address: </label></div>
            <textarea id="address" name="address" rows="5" cols="40">${address.address}</textarea>
        </div>
        <br>
        <div>
        	<div>Confirm account password to change the address:</div>
        	<div><input id="password" name="password" type="password" value=""></div>
        </div>
        <div><input type="submit" value="Submit" /></div>
    </form>
</div>