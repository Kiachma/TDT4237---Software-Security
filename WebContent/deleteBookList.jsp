<div class="container">
    <h1>Delete Book-List</h1>
    <div>Do you want to delete the following book-list?</div>
    <pre><c:out value="${booklist.title}" /></pre>
    <form action="deleteBookList.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <input name="booklistkey" value="${booklistkey}" type="hidden" />
        <div>Confirm account password to delete book-list:</div>
        <div><input id="password" name="password" type="password" value=""></div>
        
        <div><input type="submit" value="Confirm" /></div>
    </form>
</div>
