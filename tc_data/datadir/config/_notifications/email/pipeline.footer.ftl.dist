<#import "pipeline.section.ftl" as section />

<#macro footer>
  <table align="center" width="100%" data-tc-attr="footer" style="text-align:center" border="0" cellPadding="0" cellSpacing="0" role="presentation">
    <tbody>
    <tr>
      <td>
        <table align="center" width="100%" style="width:100%;text-align:center" role="presentation" cellSpacing="0" cellPadding="0" border="0">
          <tbody style="width:100%">
          <tr style="width:100%">
            <#if notificationType??>
                <#assign editURL = link.editBuildTypeNotificationsLink>
                <#assign text = "email notifications for this build configuration">
              <#else>
                <#assign editURL = link.editNotificationsLink>
                <#assign text = "your email notifications on your settings page.">
            </#if>

            <p style="font-size:12px;line-height:1;margin:16px 0;color:#999999;margin-bottom:0"><a target="_blank" href="${editURL}" style="color:#0F5B99;text-decoration:none">Configure</a> ${text}</p>
          </tr>
          </tbody>
        </table>
      </td>
    </tr>
    </tbody>
  </table>
  <@section.gap />
</#macro>
