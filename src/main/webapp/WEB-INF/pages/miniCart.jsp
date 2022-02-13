<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <!-- <p>Cart: ${cart.totalQuantity} items, ${cart.totalCost} dollars</p> -->
        <a class="cart" href="${pageContext.servletContext.contextPath}/cart"></a>