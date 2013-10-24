<div class="container">
<h1>Change name</h1>
    <div>Current name: <c:out value="${customer.name}" /> /div>
    <c:if test="${not empty messages}">
        <c:forEach var="message" items="${messages}">
            <div><span class="error">${message.value}</span></div>
        </c:forEach>
    </c:if>
    <form action="changeName.do" method="post">
        <table class="general-table">
        	<tr>
	            <td><label for="name">New name</label></td> 
	            <td><input type="text" name="name" /></td>
            </tr>
            <tr>
	        	<td><label for="password">Enter password</label></td>
	        	<td><input id="password" name="password" type="password" value="" /></td>
       		</tr>
        </table>
        <div><input type="submit" value="Submit" /></div>
    </form>
</div>