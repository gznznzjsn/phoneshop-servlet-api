<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request" />
        <tags:master pageTitle="Cart List">
          <p>
            CART
          </p>
           <c:if test="${not empty param.message}">
             <p class="success">
               ${param.message}
             </p>
           </c:if>
          <c:if test="${not empty errors}">
            <p class="error">
               One or more errors occurred during update
            </p>
          </c:if>
          <form method="post" action="${pageContext.servletContext.contextPath}/cart">
            <table>
              <thead>
                <tr>
                  <td>Image</td>
                  <td>
                    Description
                  </td>
                  <td class="quantity">
                    Quantity
                  </td>
                  <td class="price">
                    Price
                  </td>
                </tr>
              </thead>
              <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                  <td>
                    <img class="product-tile" src="${item.product.imageUrl}">
                  </td>
                  <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                      ${item.product.description}</a>
                  </td>
                  <td class="quantity">
                    <fmt:formatNumber value="${item.quantity}" var="quantity" />
                    <c:set var="error" value="${errors[item.product.id]}"/>
                    <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : quantity}" class="quantity"/>
                    <c:if test="${not empty error}">
                      <p class="error">
                        ${errors[item.product.id]}
                      </p>
                    </c:if>
                    <input type="hidden" name="productId" value="${item.product.id}"/>
                  </td>
                  <td class="price">
                    <fmt:formatNumber value="${item.product.price}" type="currency"
                      currencySymbol="${item.product.currency.symbol}" />
                  </td>
                  <td>
                  <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
                  </td>
                </tr>
              </c:forEach>
            </table>
                       <c:if test="${not empty cart.items}">
              <button>Update</button>
              </c:if>
          </form>
          <form id="deleteCartItem" method="post"></form>
        </tags:master>