<div class="container">
    <h1>Order options</h1>
    <div>Order id: <c:out value="${order.id}"/></div>
    <form class="form-inline" role="form" method="post">
	<table class="table table-striped table-bordered table-condensed">
	    <t:head>
		<tr>
		    <th>
			Book name
		    </th>
		    <th>
			ISBN10
		    </th>
		    <th>
			ISBN13
		    </th>
		    <th>
		       á price
		    </th>
		    <th>
			Quantity
		    </th>
		    <th>
			Price
		    </th>
		</tr>
	    </t:head>
	    <t:body>

		<c:forEach var="orderitem" items="${order.orderItems}" varStatus="counter">
		    <tr>
			<td>
			    ${orderitem.book.title.name}
			</td>
			<td>
			    ${orderitem.book.isbn10}
			</td>
			<td>
			    ${orderitem.book.isbn13}
			</td>
			<td>
			    ${orderitem.book.price}
			</td>
			<td>
			    <div style="margin-bottom:0px" class="form-group">
				<input type="text" class="form-control input-sm" name="quantity_${orderitem.orderItemId}" id="quantity_${orderitem.orderItemId}" value="${orderitem.quantity}">
			      </div>

			</td>
			<td>
			    ${orderitem.totalPrice}
			</td>
		    </tr>




		</c:forEach>


	    </t:body>
	</table>
	<button type="button" class="pull-right btn btn-default">Update</button>
    </form>
   
</div>
