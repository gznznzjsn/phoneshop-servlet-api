<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request" />
        <tags:master pageTitle="Order overview">
          <p>
            ORDER OVERVIEW
          </p>

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
          </table>
          <h2>Your Details</h2>
          <table>
            <tr>
              <td>First name</td>
              <td>${order.firstName}</td>
            </tr>
            <tr>
              <td>Last name</td>
              <td>${order.lastName}</td>
            </tr>
            <tr>
              <td>Phone number</td>
              <td>${order.phone}</td>
            </tr>
            <tr>
              <td>Delivery date</td>
              <td>${order.deliveryDate}</td>
            </tr>
            <tr>
              <td>Delivery address</td>
              <td>${order.deliveryAddress}</td>
            </tr>
            <tr>
              <td>Payment method</td>
              <td>${order.paymentMethod}</td>
            </tr>
          </table>
        </tags:master>