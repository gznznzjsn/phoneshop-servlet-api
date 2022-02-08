<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <jsp:useBean id="product" type="com.es.phoneshop.model.Product" scope="request" />
        <tags:master pageTitle="Product Details">
          <p>
            ${product.description}
          </p>

          <table>
            <tr>
              <td>Image</td>
              <td>
                <img src="${product.imageUrl}">
              </td>
            </tr>
            <tr>
              <td>Code</td>
              <td>
                ${product.code}
              </td>
            </tr>
            <tr>
              <td>Stock</td>
              <td>
                ${product.stock}
              </td>
            </tr>
            <tr>
              <td>Price</td>
              <td>
                <fmt:formatNumber value="${product.price}" type="currency"
                  currencySymbol="${product.currency.symbol}" />
                <button class="history-button">price-history</button>

              </td>
            </tr>
          </table>
          <div class="history-background">
            <div class="price-history">
              <h2>Price History</h2>
              <h3>${product.description}</h3>
              <table class="price-table">
                <thead>
                  <tr>
                    <td>
                      <strong>Start date</strong>
                    </td>
                    <td>
                      <strong>Price</strong>
                    </td>
                  </tr>
                </thead>
                <c:forEach var="priceHistoryBin" items="${product.priceHistory}">
                  <tr>
                    <td>
                      ${priceHistoryBin.date.dayOfMonth}.
                      ${priceHistoryBin.date.monthValue}.
                      ${priceHistoryBin.date.year}

                    </td>
                    <td>
                      <fmt:formatNumber value="${priceHistoryBin.price}" type="currency"
                        currencySymbol="${product.currency.symbol}" />
                    </td>
                  </tr>
                </c:forEach>
              </table>

            </div>
          </div>
        </tags:master>