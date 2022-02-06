<%@ tag trimDirectiveWhitespaces="true" %>
  <%@ attribute name="pageTitle" required="true" %>
    <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


      <html>

      <head>
        <title>${pageTitle}</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/normalize.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/popUpStyle.css">

      </head>

      <body>
        <div class="container">
          <tags:header pageTitle="${pageTitle}" />
          <main>
            <jsp:doBody />
          </main>
          <tags:footer />
        </div>
        <script src="${pageContext.servletContext.contextPath}/scripts/showHistory.js"></script>
      </body>

      </html>