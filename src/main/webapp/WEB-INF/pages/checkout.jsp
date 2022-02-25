<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request" />
        <tags:master pageTitle="Checkout">
          <p>
            ORDER
          </p>
          <c:if test="${not empty param.message}">
            <p class="success">
              ${param.message}
            </p>
          </c:if>
          <c:if test="${not empty errors}">
            <p class="error">
              One or more errors occurred during placing order
            </p>
          </c:if>
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
            <c:forEach var="item" items="${order.items}" varStatus="status">
              <tr>
                <td>
                  <img class="product-tile" src="${item.product.imageUrl}">
                </td>
                <td>
                  <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                    ${item.product.description}</a>
                </td>
                <td class="quantity">
                  ${item.quantity}
                </td>
                <td class="price">
                  <fmt:formatNumber value="${item.product.price}" type="currency"
                    currencySymbol="${item.product.currency.symbol}" />
                </td>
              </tr>
            </c:forEach>

            <tr>
              <td></td>
              <td></td>
              <td>
                Subtotal:
              </td>
              <td class="price">
                <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${order.currency.symbol}" />
              </td>
            </tr>
            <tr>
              <td></td>
              <td></td>
              <td>
                Delivery cost:
              </td>
              <td class="price">
                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                  currencySymbol="${order.currency.symbol}" />
              </td>
            </tr>
            <tr>
              <td></td>
              <td></td>
              <td>
                Total cost:
              </td>
              <td class="price">
                <fmt:formatNumber value="${order.totalCost}" type="currency"
                  currencySymbol="${order.currency.symbol}" />
              </td>
            </tr>
            <form action="${pageContext.servletContext.contextPath}/checkout" method="post">
          </table>
          <h2>Your Details</h2>
          <table>
            <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}" />
            <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}" />
            <tags:orderFormRow name="phone" label="Phone number" order="${order}" errors="${errors}" />
            <tags:orderFormRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}" />
            <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}" />
            <tr>
              <td>Payment method<span style="color:red">*</span></td>
              <td>
                <c:set var="error" value="${errors['paymentMethod']}" />
                <select name="paymentMethod">
                  <option value="">Choose method</option>
                  <c:forEach var="paymentMethod" items="${paymentMethods}">
                    <option value="${paymentMethod}" ${param.paymentMethod eq paymentMethod ? 'selected' : '' }>
                      ${paymentMethod}</option>
                  </c:forEach>
                </select>
                <c:if test="${not empty error}">
                  <p class="error">
                    ${error}
                  </p>
                </c:if>
              </td>
            </tr>
          </table>
          <button>Place order</button>
          </form>
        </tags:master>