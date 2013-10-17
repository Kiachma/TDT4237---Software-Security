<div class="container">
<h1>Change name</h1>
    <div>Current name: ${customer.name}</div>
    <c:if test="${not empty messages}">
        <c:forEach var="message" items="${messages}">
            <div><span class="error">${message.value}</span></div>
        </c:forEach>
    </c:if>
    <form action="changeName.do" method="post">
<<<<<<< HEAD
        <div>
            <label for="name">New name</label> 
            <input type="text" name="name" />
            <c:if test="${not empty messages.name}">
                <span class="error">${messages.name}</span>
            </c:if>
            <c:if test="${not empty messages.illegalName}">
                <span class="error">${messages.illegalName}</span>
            </c:if>
        </div>
=======
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
>>>>>>> 15679c1b6a150fe72b1b156d3554b8fc695d545c
        <div><input type="submit" value="Submit" /></div>
    </form>
</div>