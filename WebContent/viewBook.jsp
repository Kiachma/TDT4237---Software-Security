<div class="container">
    <h1>Book</h1>
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
			<form class="form-inline" action="addBookToCart.do" method="post">
			    <input type="hidden" name="isbn" value="${book.isbn13}" />
			    <div class="form-group">
				<input  class="form-control " type="text" name="quantity" value="1" />
			    </div>	
			    <input class="btn btn-default" type="submit" value="Add to cart" />

			</form>
		    </div>
		    <h3>Reviews</h3>
		    <c:if test="${not empty customer}" >
		    <div class="col-lg-offset-4 col-lg-8">
			<form class="form-horizontal" role="form" action="addReview.do" method="post">
			    <input type="hidden" name="isbn" value="${book.isbn13}" />
			    <div class="form-group <c:if test="${not empty messages.vote}">has-error</c:if>"> 
				<label class="control-label col-lg-2" for="vote" > Vote (1-5) </label>
				<div class="col-lg-1">
				    <select class="form-control " id ="vote" name="vote">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
				    </select>
				</div>
			    </div>
			    <c:if test="${not empty messages.vote}">
				<span class="error">${messages.vote}</span>
			    </c:if>

			    <div class="form-group <c:if test="${not empty messages.review}">has-error</c:if>"> 
				<label class="control-label col-lg-2" for ="review" >Review</label>
				<div class="col-lg-5">
				    <textarea class="form-control"  id="review" name="review" >
				    </textarea>
				</div>
				<c:if test="${not empty messages.review}">
				    <span class="error">${messages.review}</span>
				</c:if>
			    </div>
			    <div class="form-group">
				<div class="col-lg-5">
				    <button type="submit" class="btn btn-default">Submit</button>
				</div>
			    </div>  
			</form>
		    </div>
		    </c:if>
		    <div class="col-lg-offset-2 col-lg-8">
			<c:forEach items="${book.reviews}" var="review" varStatus="it">
			    <div class="panel panel-default">
				<div class="panel-heading">
				    
				    ${review.rating} - ${review.author.name}
				    
				    <c:if test="${not empty customer}" >
					<c:choose>
					    <c:when test="${empty review.voters[customer.id]}" >
						<div style="display:inline-block">
						    <form id="upVote_${review.id}" class="form-inline" action="rateReview.do" method="post">
							<input type="hidden" name="vote" value="1" />
							<input type="hidden" name="isbn" value="${book.isbn13}" />
							<input type="hidden" name="review" value="${review.id}" />
							<a href="#" onclick="document.getElementById('upVote_${review.id}').submit();" >
							    <span class="glyphicon glyphicon-thumbs-up"></span> 
							</a>
							${review.upVotes}
						    </form>
						</div>
						<div style="display:inline-block">
						    <form id="downVote_${review.id}" class="form-inline" action="rateReview.do" method="post">
							<input type="hidden" name="vote" value="0" />
							<input type="hidden" name="isbn" value="${book.isbn13}" />
							<input type="hidden" name="review" value="${review.id}" />
							<a href="#" onclick="document.getElementById('downVote_${review.id}').submit();" >
							    <span class="glyphicon glyphicon-thumbs-down"></span>
							</a>
							${review.downVotes}
						    </form>
						</div>
						
					    </c:when>
					    <c:otherwise>
						You voted
						<c:choose>
						    <c:when test="${review.voters[customer.id]}">
							<span style="color:blue" class="glyphicon glyphicon-thumbs-up"></span>
							${review.upVotes} 
							<span class="glyphicon glyphicon-thumbs-down"></span>
							${review.downVotes}
						    </c:when>
						    <c:otherwise>
							<span class="glyphicon glyphicon-thumbs-up"></span>
							${review.upVotes} 
							<span style="color:blue" class="glyphicon glyphicon-thumbs-down"></span>
							${review.downVotes}
						    </c:otherwise>
						</c:choose>
					    </c:otherwise>
					</c:choose>
				    </c:if>
				</div>
				<div class="panel-body">
				    ${review.review}
				</div>
			    </div>


			</c:forEach>
		    </div>
		    
		</c:otherwise>
	    </c:choose>
</div>