<html>
<body>
${kcSanitize(msg("passwordResetBodyHtml",link?split("&client_id")[0], linkExpiration, realmName, linkExpirationFormatter(linkExpiration)))?no_esc}
</body>
</html>