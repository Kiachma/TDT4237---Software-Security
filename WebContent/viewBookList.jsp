<div class="container">
    <div>Booklist</div>
    <h1><c:out value="${booklist.title}"/></h1>
    <h2><c:out value="${booklist.description}"/></h2>
    <span>
        <div>Public: </div>
        <c:choose>
            <c:when test="${booklist.ispublic}">
                <div>Yes</div>
            </c:when>
            <c:otherwise>
                <div>No</div>
            </c:otherwise>
        </c:choose>
    </span>
    </div>
    <c:forEach var="book" items="${booklist.books}" varStatus="counter">
        <div>
            <span><a href="viewBook.do?isbn=${book.book.isbn13}">${book.book.title.name}</a></span>
            <span><a href="viewBookList.do?id=${booklistkey}&delete_isbn=${book.book.isbn13}">(x)</a> </span>
        </div>
    </c:forEach> 

    
    <br>
    <c:choose>
        <c:when test="${booklist.customerID == customer.id}">
            <div><a href="publishBookList.do?booklistkey=${booklistkey}">PUBLISH LIST</a></div>
            <br>
            <div><a href="deleteBookList.do?booklistkey=${booklistkey}">DELETE LIST</a></div> 
        </c:when>
    </c:choose>
</div>
