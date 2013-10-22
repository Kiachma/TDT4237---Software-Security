<div class="container">
    <h1>Book</h1>
    <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="container">${message}</span>
                </div>
            </c:forEach>
    </c:if>
    <c:choose>
        <c:when test="${empty book}">
            <h2>Book not found!</h2>
            <div class = "index-item"><a href="debug/list_books.jsp">List books</a></div>
        </c:when>
        <c:otherwise>
            <h2>${book.title.name}</h2>
            <div>
                <ul>
                    <li>
                        <b>Authors:</b> 
                        <c:forEach items="${book.author}" var="author" varStatus="it">
                            ${author.name}<c:if test="${!it.last}">, </c:if>
                        </c:forEach>
                    </li>
                    <li><b>Publisher:</b> ${book.publisher.name}</li>
                    <li><b>Published:</b> ${book.published}</li>
                    <li><b>Edition:</b> ${book.edition} (${book.binding})</li>
                    <li><b>ISBN:</b> ${book.isbn13}</li>
                    <li><b>Price:</b> ${book.price}</li>
                </ul>
            </div>
            <div>
                ${book.description}
            </div>
            <div>
                <form action="addBookToCart.do" method="post">
                    <input type="hidden" name="isbn" value="${book.isbn13}" />
                    <input type="text" name="quantity" value="1" />
                    <input type="submit" value="Add to cart" />
                </form>
            </div>
        </c:otherwise>
    </c:choose>
    <br>
    <br>
    <c:if test="${not empty customer}" >
        <form action="viewBook.do" method="post">
            <div>Add to one of your book-lists</div>
            <select id="booklist_selection" name="booklist_selection">
                <c:forEach var="booklist" items="${booklists}" varStatus="counter">
                    <option value="${booklist.key}">${booklist.value}</option>
                </c:forEach>
                <input type="hidden" name="book_id" value="${book.title.id}">
            </select>
            <div><input type="submit" value="Add"></div>
        </form>
    </c:if>
    
</div>