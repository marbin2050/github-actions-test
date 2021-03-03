<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        ${msg("errorTitle")}
    <#elseif section = "form">
        <div id="kc-error-message">
            <p class="instruction">${message.summary}</p>
            <p><a id="backToApplication" href="https://app.caseform.de">${kcSanitize(msg("backToApplication"))?no_esc}</a></p>
        </div>
    </#if>
</@layout.registrationLayout>