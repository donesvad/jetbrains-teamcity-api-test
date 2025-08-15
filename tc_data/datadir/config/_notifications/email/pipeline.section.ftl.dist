<#import "pipeline.typography.ftl" as typography>

<#macro section title=null>
  <#if title?has_content><@typography.text text="${title}" bold=true /></#if>
  <#nested />
</#macro>

<#macro code>
  <table align="center" width="100%" id="code-container" style="background:#F7F8FA;border:1px solid #D3D5DB;border-radius:4px;padding:16px 24px" border="0" cellPadding="0" cellSpacing="0" role="presentation">
    <tbody>
    <tr>
      <td>
        <#nested />
      </td>
    </tr>
    </tbody>
  </table>
</#macro>

<#macro gap size=32>
  <table align="center" width="100%" style="margin-top:${size}px" border="0" cellpadding="0" cellspacing="0" role="presentation">
    <tbody>
    <tr>
      <td></td>
    </tr>
    </tbody>
  </table>
</#macro>
