<div class="container">
    <div>Booklist</div>
    <h1>${booklist.title}</h1>
    <h2>${booklist.description}</h2>
    <c:forEach var="book" items="${booklist.books}" varStatus="counter">
        <div><a href="viewBook.do?isbn=${book.book.isbn13}">${book.book.title.name}</a></div>
    </c:forEach> 

</div>
