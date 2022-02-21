<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <div class="cart">
          <a class="cart-icon" href="${pageContext.servletContext.contextPath}/cart"></a>
          </a>
          <div class="cart-popup">
            <c:if test="${empty cart.items}">
              Cart is empty
            </c:if>
            <c:if test="${not empty cart.items}">
              Cart: ${cart.totalQuantity} items,<br>
              <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${cart.currency.symbol}" />
            </c:if>
          </div>
        </div>