<div class="container">
    <div>Public Booklists</div>
    <c:forEach var="booklist" items="${booklists}" varStatus="counter">
        <div>
            <span><a href="viewBookList.do?id=${booklist.key}">${booklist.value}</a></span>
        </div>
        <br>
    </c:forEach> 
</div>
