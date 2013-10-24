<div class="container">
    <div>Booklist</div>
    <h1><c:out value="${booklist.title}"/></h1>
    <h2><c:out value="${booklist.description}"/></h2>
    <c:forEach var="book" items="${booklist.books}" varStatus="counter">
        <div>
            <span><a href="viewBook.do?isbn=${book.book.isbn13}">${book.book.title.name}</a></span>
            <span><a href="viewBookList.do?id=${booklistkey}&delete_isbn=${book.book.isbn13}">(x)</a> </span>
        </div>
    </c:forEach> 

    <br>
    <br>
    <div><a href="deleteBookList.do?booklistkey=${booklistkey}">DELETE LIST</a></div>
</div>
