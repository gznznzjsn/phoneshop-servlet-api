<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <div class="cart">
          <a class="cart-icon" href="${pageContext.servletContext.contextPath}/cart"></a>
          </a>
          <div class="cart-popup">Cart: ${cart.totalQuantity} items,<br> ${cart.totalCost}
            ${cart.items[0].product.currency.symbol}</div>
        </div>