<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@ page import="amu.Config"%>
<div class="container">
	<h1>Login</h1>
	<c:choose>
		<c:when test="${empty customer}">
			<div class="general-form">
				<form action="loginCustomer.do" method="post">
					<c:if test="${not empty values.from}">
						<input type="hidden" name="from" value="${values.from}">
					</c:if>
					<table class="general-table">
						<tr>
							<td><label for="email">Email</label></td>
							<td><input id="email" name="email" type="email"
								value="${values.email}" /></td>
							<c:if test="${not empty messages.email}">
								<td><span class="error">${messages.email}</span></td>
							</c:if>
						</tr>
						<tr>
							<td><label for="password">Password</label></td>
							<td><input id="password" name="password" type="password"
								autocomplete="off" /></td>
							<c:if test="${not empty messages.password}">
								<td><span class="error">${messages.password}</span></td>
							</c:if>
						</tr>
						<c:if test="${loginCount>=3}">
							<tr>
								<td>
								<td>
									<%
										ReCaptcha c = ReCaptchaFactory.newReCaptcha(
															Config.RECAPTCHA_PUBLIC_KEY,
															Config.RECAPTCHA_PRIVATE_KEY, false);
													out.print(c.createRecaptchaHtml(null, null));
									%>

								</td>
							</tr>
						</c:if>
					</table>
					<div>
						<input type="submit" value="Submit">
					</div>

				</form>
			</div>
		</c:when>
		<c:otherwise>
			<div>Login successful!</div>
		</c:otherwise>
	</c:choose>
</div>