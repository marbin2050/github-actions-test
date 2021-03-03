<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        <#if messageHeader??>
        ${messageHeader}
        <#else>
        ${message.summary}
        </#if>
    <#elseif section = "form">
    <div id="kc-info-message">
        <p class="instruction">${message.summary}<#if requiredActions??><#list requiredActions>: <b><#items as reqActionItem>${msg("requiredAction.${reqActionItem}")}<#sep>, </#items></b></#list><#else></#if></p>
        <#if skipLink??>
            <a class="action-link" href="${msg("applicationUrl")}">${kcSanitize(msg("goToApplication"))?no_esc}</a>
        <#else>
            <#if pageRedirectUri??>
                <p><a href="${pageRedirectUri}">${kcSanitize(msg("backToApplication"))?no_esc}</a></p>
            <#elseif actionUri??>
                <p><a id="action" href="${actionUri}">${kcSanitize(msg("proceedWithAction"))?no_esc}</a></p>
            <#elseif client.baseUrl??>
                <p><a href="${client.baseUrl}">${kcSanitize(msg("backToApplication"))?no_esc}</a></p>
            <#else>
            </#if>
        </#if>
    </div>
    <script>
        var actionUrl = document.getElementById("action").href;
        window.location.replace(actionUrl);
    </script>
    </#if>
</@layout.registrationLayout>