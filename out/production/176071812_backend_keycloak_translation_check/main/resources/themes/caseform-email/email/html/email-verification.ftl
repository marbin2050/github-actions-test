<html>
<body>
${kcSanitize(msg("emailVerificationBodyHtml",link, linkExpiration, realmName, linkExpirationFormatter(linkExpiration), user.getFirstName(), user.getLastName()))?no_esc}
</body>
</html>
