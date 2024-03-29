<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@ page import="amu.Config"%>
<div class="container">
    <h1>Register</h1>
    <c:choose>
        <c:when test="${empty sessionScope.customer}">
            <c:choose>
                <c:when test="${not empty register_success}">
                    <div>
                        You've successfully created a user. Maybe you should <a href="loginCustomer.do">sign in</a>?
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${not empty register_error}">
                        <div>
                            ${register_error}
                        </div>
                    </c:if>
                    <div class="general-form">
                        <form action="registerCustomer.do" method="post">
                            <table class="general-table">
                                <tr>
                                    <td><label for="email">Email</label></td>
                                    <td><input id="email" name="email" type="email" /></td>
                                    <c:if test="${not empty messages.email}">
					<td><span class="error">${messages.email}</span></td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td><label for="name">Name</label></td>
                                    <td><input id="name" name="name" type="text" /></td>
                                    <c:if test="${not empty messages.name}">
					<td><span class="error">${messages.name}</span></td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td><label for="password">Password</label></td>
                                    <td><input id="password" name="password" type="password" autocomplete="off" /></td>
                                    <c:if test="${not empty messages.password}">
					<td><span class="error">${messages.password}</span></td>
                                    </c:if>
                                </tr>
                                <tr>
                                	<td><label for="captcha">Prove your humanity</label></td>
									<td>
										<%
											ReCaptcha c = ReCaptchaFactory.newReCaptcha(
																Config.RECAPTCHA_PUBLIC_KEY,
																Config.RECAPTCHA_PRIVATE_KEY, false);
														out.print(c.createRecaptchaHtml(null, null));
										%>
									</td>
									<c:if test="${not empty messages.captcha}">
										<td><span class="error">${messages.captcha}</span></td>
                                    </c:if>
								</tr>
                            </table>
                            <div><input type="submit" value="Submit"></div>
                            <c:if test="${not empty messages.error}">
                            	<br><br>
                            	<span class="error">${messages.error}</span>
                           	</c:if>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <div>
                You're already logged in. Now, don't be greedy, one account should be enough.
            </div>
        </c:otherwise>
    </c:choose>
</div>