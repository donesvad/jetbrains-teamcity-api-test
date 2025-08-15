<#macro heading text="" level=1>
  <p style="font-size:14px;line-height:24px;margin:0;font-weight:600">${text}</p>
</#macro>

<#macro text text="" href="" bold=false>
  <#if text?has_content>
    <p style="font-size:14px;line-height:24px;margin:0;<#if bold>font-weight:600</#if>">
        <#if href?has_content>
          <a href="${href}" target="_blank" style="color:#067df7;text-decoration:none">${text}</a>
        <#else>
          ${text}
        </#if>
    </p>
  </#if>
</#macro>
