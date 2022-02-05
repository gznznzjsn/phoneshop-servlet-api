<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/popUpStyle.css">
</head>
<body class="product-list">
  <tags:header pageTitle="${pageTitle}" />
  <main>
    <jsp:doBody/>

  </main>
  <tags:footer/>
    <script src="${pageContext.servletContext.contextPath}/scripts/showHistory.js"></script>
</body>
</html>