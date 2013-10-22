<div class="container">
    <h2>New Book-List</h2>
    <form action="addBookList.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        
        <div>
            <div><label for="title">Title:</label></div>
            <input id="title" name="title" value=""></input>
            <div><label for="description">Description</label></div>
            <textarea id="description" name="description" cols="10" rows="5"></textarea>
        </div>
        <div><input type="submit" value="Submit"</div>
    </form>
</div>